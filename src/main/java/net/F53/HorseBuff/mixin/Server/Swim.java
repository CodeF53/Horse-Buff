package net.F53.HorseBuff.mixin.Server;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class)
public class Swim {
    @Inject(method = "travelControlled", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;travel(Lnet/minecraft/util/math/Vec3d;)V", shift = At.Shift.BEFORE))
    private void fakeSwim(LivingEntity controllingPassenger, Vec3d movementInput, CallbackInfo ci) {
        if (ModConfig.getInstance().swim && ((LivingEntity)(Object)this instanceof AbstractHorseEntity)) {
            AbstractHorseEntity instance = ((AbstractHorseEntity)(Object)this);
            if (instance.getFluidHeight(FluidTags.WATER) > instance.getSwimHeight()) {
                instance.addVelocity(0, 0.08, 0);
            }
        }
    }
}
