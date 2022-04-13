package net.F53.HorseBuff.mixin.PortalHorse;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.F53.HorseBuff.HorseBuffInit;

@Mixin(EndPortalBlock.class)
public class OnCollideEnd {

    // Allow entities with passengers
    // Has to be vehicle bringing player, otherwise you enter end with a burning horse
    @Redirect(method = "onEntityCollision(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.hasPassengers ()Z"))
    public boolean isVehicle(Entity instance){
        if (ModConfig.getInstance().portalPatch) {
            return false;
        }
        return instance.hasPassengers();
    }

    @Redirect(method = "onEntityCollision(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.moveToWorld (Lnet/minecraft/server/world/ServerWorld;)Lnet/minecraft/entity/Entity;"))
    public Entity bringRider(Entity vehicle, ServerWorld destination){
        if (ModConfig.getInstance().portalPatch && vehicle.hasPassengers() && vehicle.getFirstPassenger() instanceof PlayerEntity) {
            // Get Player
            Entity player = vehicle.getPrimaryPassenger();
            assert player != null;

            // Split vehicle and player
            player.detach();

            // Change vehicle Dim
            vehicle = vehicle.moveToWorld(destination);
            assert vehicle != null;
            vehicle.unsetRemoved();

            // Change player Dim
            player.moveToWorld(destination);
            player.unsetRemoved();

            // If we are moving from the End to the overworld, the player gets teleported to their bed,
            // while their horse goes to world spawn, we have to make them meet
            if (destination.getRegistryKey() == World.OVERWORLD) {
                // Next tick bring the player over to the vehicle and remount
                HorseBuffInit.tpAndRemount(player, vehicle);
            } else {
                player.startRiding(vehicle, true);
            }

            return vehicle;
        }
        return vehicle.moveToWorld(destination);
    }
}
