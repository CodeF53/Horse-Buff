package net.F53.HorseBuff.mixin.Server;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class)
public class Swim {
    @Inject(method = "travelControlled", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;travel(Lnet/minecraft/util/math/Vec3d;)V", shift = At.Shift.BEFORE))
    private void fakeSwim(PlayerEntity controllingPlayer, Vec3d movementInput, CallbackInfo ci) {
        AbstractHorseEntity horseEntity = (AbstractHorseEntity) (Object) this;

        if (shouldSwim(horseEntity)) {
            if (horseEntity.getFluidHeight(FluidTags.WATER) > horseEntity.getSwimHeight()) {
                horseEntity.addVelocity(0, 0.08, 0);
            }
        }
    }

    @Unique
    private boolean shouldSwim(AbstractHorseEntity horseEntity) {
        if (horseEntity instanceof HorseEntity) {
            return ModConfig.getInstance().swimHorse;
        } else if (horseEntity instanceof MuleEntity) {
            return ModConfig.getInstance().swimHorse;
        } else if (horseEntity instanceof DonkeyEntity) {
            return ModConfig.getInstance().swimHorse;

        } else if (horseEntity instanceof CamelEntity) {
            return ModConfig.getInstance().swimCamel;

        } else if (horseEntity instanceof SkeletonHorseEntity) {
            return ModConfig.getInstance().swimDead;
        } else if (horseEntity instanceof ZombieHorseEntity) {
            return ModConfig.getInstance().swimDead;
        }
        return false;
    }
}
