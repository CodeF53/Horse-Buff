package net.F53.HorseBuff.mixin.Server;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// Lower wander speed for saddled horses
@Mixin(value = LivingEntity.class, priority = 960)
public abstract class NoWander {
    @ModifyArg(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;travel(Lnet/minecraft/util/math/Vec3d;)V"))
    private Vec3d lowerWanderSpeed(Vec3d input) {
        if (ModConfig.getInstance().noWander
          && (hb$thiz() instanceof AbstractHorseEntity horse
          && horse.isSaddled()))
            return(Vec3d.ZERO);
        return input;
    }

    @Unique
    private LivingEntity hb$thiz() {
        return ((LivingEntity)(Object)this);
    }
}
