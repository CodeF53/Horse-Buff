package net.F53.HorseBuff.mixin.PortalHorse;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static net.F53.HorseBuff.utils.TeleportHandler.tpAndRemount;

@Mixin(EndPortalBlock.class)
public class OnCollideEnd {
    @WrapOperation(method = "onEntityCollision(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.moveToWorld (Lnet/minecraft/server/world/ServerWorld;)Lnet/minecraft/entity/Entity;"))
    public Entity bringRider(Entity vehicle, ServerWorld destination, Operation<Entity> original) {
        // guard clause to make vanilla handle natural teleportations
        if (!(ModConfig.getInstance().portalPatch
                && vehicle instanceof AbstractHorseEntity
                && vehicle.hasControllingPassenger()))
            return original.call(vehicle, destination);

        // Teleport then safely rejoin player and vehicle once the game is ready
        tpAndRemount((AbstractHorseEntity) vehicle, destination, true);

        return vehicle;
    }
}
