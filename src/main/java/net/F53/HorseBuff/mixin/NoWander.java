package net.F53.HorseBuff.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import net.minecraft.entity.passive.HorseBaseEntity;

// Lower wander speed for saddled horses
@Mixin(HorseBaseEntity.class)
public abstract class NoWander {
    @Shadow public abstract boolean isSaddled();

    @ModifyArg(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AnimalEntity;travel(Lnet/minecraft/util/math/Vec3d;)V", ordinal = 0))
    private void lowerWanderSpeed(Args args) {
        if (isSaddled())
            args.set(0, Vec3d.ZERO);
    }
}
