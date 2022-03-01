package net.F53.HorseBuff.mixin.PortalHorse;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndPortalBlock.class)
public class OnCollideEnd {
    //Allow entities with passengers
    @Redirect(method = "onEntityCollision(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.hasPassengers ()Z"))
    public boolean isVehicle(Entity instance){
        if (ModConfig.getInstance().portalPatch) {
            return false;
        }
        return instance.hasPassengers();
    }

    @Redirect(method = "onEntityCollision(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.moveToWorld (Lnet/minecraft/server/world/ServerWorld;)Lnet/minecraft/entity/Entity;"))
    public Entity hgdfytiop(Entity instance, ServerWorld destination){
        if (instance.hasPassengers() && instance.getFirstPassenger() instanceof PlayerEntity) {
            Entity rider = instance.getFirstPassenger();

            Entity newInstance = instance.moveToWorld(destination);
            Entity newRider = rider.moveToWorld(destination);

            newInstance.setPosition(newRider.getPos());
            newRider.startRiding(newInstance, true);

            return newInstance;
        }
        return instance.moveToWorld(destination);
    }
}
