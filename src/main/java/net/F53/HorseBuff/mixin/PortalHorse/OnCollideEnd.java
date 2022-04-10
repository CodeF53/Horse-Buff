package net.F53.HorseBuff.mixin.PortalHorse;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.source.BiomeAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.F53.HorseBuff.HorseBuffInit;

import java.util.Iterator;

@Mixin(EndPortalBlock.class)
public class OnCollideEnd {
    /* disabled until player is moved to horse

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
            if (destination.getRegistryKey() == World.OVERWORLD) {
                // Cant use vanilla moveToWorld because it doesn't let us setpos afterwards
                this.inTeleportationState = true;
                ServerWorld serverWorld = this.getWorld();
                RegistryKey<World> registryKey = serverWorld.getRegistryKey();
                this.getWorld().removePlayer(this, Entity.RemovalReason.CHANGED_DIMENSION);
                this.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.GAME_WON, this.seenCredits ? 0.0F : 1.0F));
                this.seenCredits = true;
            } else {
                player.moveToWorld(destination);
                player.unsetRemoved();
            }

            // Make player remount Vehicle
            player.startRiding(vehicle, true);

            return vehicle;
        }
        return vehicle.moveToWorld(destination);
    }

     */
}
