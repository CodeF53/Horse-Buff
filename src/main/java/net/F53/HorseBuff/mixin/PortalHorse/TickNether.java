package net.F53.HorseBuff.mixin.PortalHorse;


import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.AreaHelper;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class TickNether {

    @Shadow protected boolean inNetherPortal;

    @Shadow protected int netherPortalTime;

    @Shadow protected BlockPos lastNetherPortalPosition;

    @Shadow protected abstract void tickNetherPortalCooldown();

    @Shadow public abstract boolean hasVehicle();

    @Inject(method = "tickNetherPortal()V", at = @At("HEAD"))
    public void riderTravel(CallbackInfo ci){
        Entity thisEntity = (Entity)(Object)this;
        if (thisEntity.world instanceof ServerWorld && thisEntity instanceof PlayerEntity){
            if (thisEntity.hasVehicle()){
                int maxPortalTime = thisEntity.getMaxNetherPortalTime();
                if (inNetherPortal) {
                    MinecraftServer minecraftServer = ((ServerWorld)thisEntity.world).getServer();
                    ServerWorld serverWorld2 = minecraftServer.getWorld(thisEntity.world.getRegistryKey() == World.NETHER ? World.OVERWORLD : World.NETHER);

                    if (serverWorld2 != null && minecraftServer.isNetherAllowed() && netherPortalTime++ >= maxPortalTime) {
                        // Get Vehicle
                        Entity oldVehicle = thisEntity.getVehicle();
                        assert oldVehicle != null;

                        // Change Entity Dim
                        netherPortalTime = maxPortalTime;
                        thisEntity.resetNetherPortalCooldown();
                        thisEntity.moveToWorld(serverWorld2);

                        // Change Vehicle Dim
                        Entity newVehicle = oldVehicle.getType().create(serverWorld2);
                        assert newVehicle != null;
                        newVehicle.copyFrom(oldVehicle);

                        // Get Proper Vehicle Position
                        TeleportTarget teleportTarget = getVehicleTeleportTarget(oldVehicle, serverWorld2);
                        newVehicle.refreshPositionAndAngles(teleportTarget.position.x, teleportTarget.position.y, teleportTarget.position.z, teleportTarget.yaw, newVehicle.getPitch());
                        newVehicle.setVelocity(teleportTarget.velocity);
                        serverWorld2.onDimensionChanged(newVehicle);
                        oldVehicle.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);

                        // Make Entity remount Vehicle
                        thisEntity.startRiding(newVehicle, true);
                    }
                    inNetherPortal = false;
                }
                else {
                    if (this.netherPortalTime > 0) {
                        this.netherPortalTime -= 4;
                    }
                    if (this.netherPortalTime < 0) {
                        this.netherPortalTime = 0;
                    }
                }
                tickNetherPortalCooldown();
            }
        }
    }

    // elsewhere, we allow vehicles to be marked as in nether portal, so we have to deny them teleporting
    @Redirect(method = "tickNetherPortal()V", at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.hasVehicle ()Z"))
    public boolean denyVehicleTravel(Entity instance){
        // if portalPatch, deny travel
        if (instance.hasPassengers() && ModConfig.getInstance().portalPatch){
            return true;
        }
        return instance.hasVehicle();
    }

    @ModifyConstant(method = "tickNetherPortal()V", constant = @Constant(intValue = 4))
    public int netherPortalTime(int constant){
        if (this.hasVehicle()){
            return 0;
        }
        return constant;
    }

    // gives teleport target for given entity (assumes nether)
    // made by copying code, and whenever something didn't have access, just copying that method
    // for lastNetherPortalPosition, that is taken from the rider
    @Nullable
    protected TeleportTarget getVehicleTeleportTarget(Entity Vehicle, ServerWorld destination) {
        boolean blockPos = destination.getRegistryKey() == World.NETHER;
        if (Vehicle.world.getRegistryKey() != World.NETHER && !blockPos) {
            return null;
        } else {
            WorldBorder worldBorder = destination.getWorldBorder();
            double d = Math.max(-2.9999872E7D, worldBorder.getBoundWest() + 16.0D);
            double e = Math.max(-2.9999872E7D, worldBorder.getBoundNorth() + 16.0D);
            double f = Math.min(2.9999872E7D, worldBorder.getBoundEast() - 16.0D);
            double g = Math.min(2.9999872E7D, worldBorder.getBoundSouth() - 16.0D);
            double h = DimensionType.getCoordinateScaleFactor(Vehicle.world.getDimension(), destination.getDimension());
            BlockPos blockPos2 = new BlockPos(MathHelper.clamp(Vehicle.getX() * h, d, f), Vehicle.getY(), MathHelper.clamp(Vehicle.getZ() * h, e, g));

            return (TeleportTarget)destination.getPortalForcer().getPortalRect(blockPos2, blockPos, worldBorder).map((rect) -> {
                BlockState blockState = Vehicle.world.getBlockState(lastNetherPortalPosition);
                Direction.Axis axis;
                Vec3d vec3d;
                if (blockState.contains(Properties.HORIZONTAL_AXIS)) {
                    axis = (Direction.Axis)blockState.get(Properties.HORIZONTAL_AXIS);
                    BlockLocating.Rectangle rectangle = BlockLocating.getLargestRectangle(lastNetherPortalPosition, axis, 21, Direction.Axis.Y, 21, (blockPosa) -> {
                        return Vehicle.world.getBlockState(blockPosa) == blockState;
                    });
                    vec3d = AreaHelper.entityPosInPortal(rectangle, axis, Vehicle.getPos(), Vehicle.getDimensions(Vehicle.getPose()));
                } else {
                    axis = Direction.Axis.X;
                    vec3d = new Vec3d(0.5D, 0.0D, 0.0D);
                }

                return AreaHelper.getNetherTeleportTarget(destination, rect, axis, vec3d, Vehicle.getDimensions(Vehicle.getPose()), Vehicle.getVelocity(), Vehicle.getYaw(), Vehicle.getPitch());
            }).orElse((TeleportTarget) null);
        }
    }
}