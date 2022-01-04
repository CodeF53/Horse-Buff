package net.F53.HorseBuff.mixin.PortalHorse;


import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


// allow entities w/ vehicles to be set as in nether portal
@Mixin(NetherPortalBlock.class)
public class OnCollideNether {
    @Redirect(method = "onEntityCollision(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.hasVehicle ()Z"))
    public boolean riding(Entity instance){
        return !ModConfig.getInstance().portalPatch;
    }
}
