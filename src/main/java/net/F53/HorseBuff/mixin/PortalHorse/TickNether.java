package net.F53.HorseBuff.mixin.PortalHorse;

import net.F53.HorseBuff.HorseBuffInit;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class TickNether {

    @Shadow protected boolean isInsidePortal;

    @Shadow protected int portalTime;

    @Shadow protected abstract void processPortalCooldown();

    @Shadow public abstract boolean isPassenger();

    @Inject(method = "handleNetherPortal", at = @At("HEAD"))
    public void riderTravel(CallbackInfo ci){
        Entity player = (Entity)(Object)this;
        if (player.level instanceof ServerLevel && player instanceof Player){
            if (player.getVehicle() != null){
                int maxPortalTime = player.getPortalWaitTime();
                if (isInsidePortal) {
                    MinecraftServer minecraftServer = ((ServerLevel)player.level).getServer();
                    ServerLevel destination = minecraftServer.getLevel(player.level.dimension() == Level.NETHER ? Level.OVERWORLD : Level.NETHER);
                    if (destination != null && minecraftServer.isNetherEnabled() && portalTime++ >= maxPortalTime) {
                        // Get Vehicle
                        Entity vehicle = player.getVehicle();

                        // Split
                        vehicle.unRide();

                        // in some cases some of these values are null, causing problems, just don't teleport if they are null
                        if (player.position() != null && vehicle.position() != null) {
                            // Get UUIDs
                            UUID vehicleUUID = vehicle.getUUID();
                            UUID playerUUID = player.getUUID();

                            // Change player Dim
                            player.setPortalCooldown();
                            player.changeDimension(destination);

                            // Change vehicle Dim
                            vehicle.setPortalCooldown();
                            vehicle.changeDimension(destination);

                            // Safely rejoin player and vehicle once the game is ready
                            HorseBuffInit.tpAndRemount(playerUUID, vehicleUUID, destination, 0);
                        }
                    }
                    isInsidePortal = false;
                }
                else {
                    if (this.portalTime > 0) {
                        this.portalTime -= 4;
                    }
                    if (this.portalTime < 0) {
                        this.portalTime = 0;
                    }
                }
                processPortalCooldown();
            }
        }
    }

    // elsewhere, we allow vehicles to be marked as in nether portal, so we have to deny them teleporting
    @Redirect(method = "handleNetherPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isPassenger()Z"))
    public boolean denyVehicleTravel(Entity instance){
        // if portalPatch, deny travel
        if (instance.isVehicle() && ModConfig.getInstance().portalPatch){
            return true;
        }
        return instance.isPassenger();
    }

    @ModifyConstant(method = "handleNetherPortal", constant = @Constant(intValue = 4))
    public int netherPortalTime(int constant){
        if (this.isPassenger()){
            return 0;
        }
        return constant;
    }
}