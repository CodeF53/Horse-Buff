package net.F53.HorseBuff.mixin.PortalHorse;

import net.F53.HorseBuff.HorseBuffInit;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class TickNether {

    @Shadow protected boolean inNetherPortal;

    @Shadow protected int netherPortalTime;

    @Shadow protected abstract void tickNetherPortalCooldown();

    @Shadow public abstract boolean hasVehicle();

    @Inject(method = "tickNetherPortal()V", at = @At("HEAD"))
    public void riderTravel(CallbackInfo ci){
        Entity player = (Entity)(Object)this;
        if (player.world instanceof ServerWorld && player instanceof PlayerEntity){
            if (player.hasVehicle()){
                int maxPortalTime = player.getMaxNetherPortalTime();
                if (inNetherPortal) {
                    MinecraftServer minecraftServer = ((ServerWorld)player.world).getServer();
                    ServerWorld serverWorld2 = minecraftServer.getWorld(player.world.getRegistryKey() == World.NETHER ? World.OVERWORLD : World.NETHER);

                    if (serverWorld2 != null && minecraftServer.isNetherAllowed() && netherPortalTime++ >= maxPortalTime) {
                        // Get Vehicle
                        Entity vehicle = player.getVehicle();
                        assert vehicle != null;

                        HorseBuffInit.LOGGER.info("TP- Got Vehicle");

                        // Split Vehicle and Player
                        player.detach();
                        HorseBuffInit.LOGGER.info("TP- Split");

                        // Fetch old coordinates
                        Vec3d oldCords = player.getPos();

                        // Change Player Dim
                        netherPortalTime = maxPortalTime;
                        player.resetNetherPortalCooldown();
                        player.moveToWorld(serverWorld2);
                        player.unsetRemoved();

                        HorseBuffInit.LOGGER.info("TP- Moved Player to new dimension");

                        // Change Vehicle Dim
                        vehicle.resetNetherPortalCooldown();
                        Entity newVehicle = vehicle.moveToWorld(serverWorld2);
                        newVehicle.unsetRemoved();

                        HorseBuffInit.LOGGER.info("TP- Moved Vehicle to new dimension");

                        // Make Entity remount Vehicle
                        player.startRiding(newVehicle, true);

                        HorseBuffInit.LOGGER.info("TP- Remounted Player on vehicle");

                        // Fetch new coordinates
                        Vec3d newCords = player.getPos();

                        HorseBuffInit.LOGGER.info("TP- Teleport Complete!\n\tOld Coords:" + oldCords + "\n\tNew Coords:"+newCords);
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
}