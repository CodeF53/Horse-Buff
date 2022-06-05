package net.F53.HorseBuff.mixin.PortalHorse;


import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.NetherPortalBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


// allow Entities w/ Vehicles and Vehicles to be set as in nether portal
@Mixin(NetherPortalBlock.class)
public class OnCollideNether {
    @Redirect(method = "onEntityCollision(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.hasVehicle ()Z"))
    public boolean hasVehicle(Entity instance){
        if (ModConfig.getInstance().portalPatch) {
            return false;
        }
        return instance.isPassenger();
    }

    @Redirect(method = "onEntityCollision(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.hasPassengers ()Z"))
    public boolean isVehicle(Entity instance){
        if (ModConfig.getInstance().portalPatch) {
            return false;
        }
        return instance.isVehicle();
    }
}
