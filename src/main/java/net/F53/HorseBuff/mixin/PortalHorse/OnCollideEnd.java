package net.F53.HorseBuff.mixin.PortalHorse;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.EndPortalBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.F53.HorseBuff.HorseBuffInit;

import java.util.UUID;

@Mixin(EndPortalBlock.class)
public class OnCollideEnd {

    // Allow entities with passengers
    // Has to be vehicle bringing player, otherwise you enter end with a burning horse
    @Redirect(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isVehicle()Z"))
    public boolean isVehicle(Entity instance){
        if (ModConfig.getInstance().portalPatch) {
            return false;
        }
        return instance.isVehicle();
    }

    @Redirect(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;changeDimension(Lnet/minecraft/server/level/ServerLevel;)Lnet/minecraft/world/entity/Entity;"))
    public Entity bringRider(Entity vehicle, ServerLevel destination){
        if (ModConfig.getInstance().portalPatch && vehicle.isVehicle() && vehicle.getFirstPassenger() instanceof Player) {
            // Get player
            Entity player = vehicle.getControllingPassenger();
            assert player != null;

            // Get UUIDs
            UUID vehicleUUID = vehicle.getUUID();
            UUID playerUUID = player.getUUID();

            // Change player Dim
            player.changeDimension(destination);

            // Change vehicle Dim
            vehicle.changeDimension(destination);

            // Safely rejoin player and vehicle once the game is ready
            HorseBuffInit.tpAndRemount(playerUUID, vehicleUUID, destination, 0);

            return vehicle;
        }
        return vehicle.changeDimension(destination);
    }
}
