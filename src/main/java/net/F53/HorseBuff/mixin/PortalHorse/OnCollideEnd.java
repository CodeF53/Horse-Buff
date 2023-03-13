package net.F53.HorseBuff.mixin.PortalHorse;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.F53.HorseBuff.HorseBuffInit;

import java.util.UUID;

@Mixin(EndPortalBlock.class)
public class OnCollideEnd {
    @WrapOperation(method = "onEntityCollision(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.moveToWorld (Lnet/minecraft/server/world/ServerWorld;)Lnet/minecraft/entity/Entity;"))
    public Entity bringRider(Entity vehicle, ServerWorld destination, Operation<Entity> original){
        if (ModConfig.getInstance().portalPatch && vehicle.hasPassengers() && vehicle.getFirstPassenger() instanceof PlayerEntity) {
            // Get player
            Entity player = vehicle.getControllingPassenger();
            assert player != null;

            // Get UUIDs
            UUID vehicleUUID = vehicle.getUuid();
            UUID playerUUID = player.getUuid();

            // Change player Dim
            player.moveToWorld(destination);

            // Change vehicle Dim
            vehicle.moveToWorld(destination);

            // Safely rejoin player and vehicle once the game is ready
            HorseBuffInit.tpAndRemount(playerUUID, vehicleUUID, destination, 0);

            return vehicle;
        } else {
            return original.call(vehicle, destination);
        }
    }
}
