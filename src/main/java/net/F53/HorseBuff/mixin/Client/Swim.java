package net.F53.HorseBuff.mixin.Client;

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
        if (!((Object)this instanceof AbstractHorseEntity)) {return;}
        AbstractHorseEntity horseInstance = (AbstractHorseEntity) (Object) this;
        if (!shouldSwim(horseInstance)) {return;}

        if (horseInstance.getFluidHeight(FluidTags.WATER) > horseInstance.getSwimHeight()) {
            horseInstance.addVelocity(0, 0.08, 0);
        }
    }

    @Unique
    private boolean shouldSwim(AbstractHorseEntity horseInstance) {
        if (horseInstance instanceof HorseEntity ||
            horseInstance instanceof DonkeyEntity ||
            horseInstance instanceof MuleEntity) {
            return ModConfig.getInstance().swimHorse;
        }

        if (horseInstance instanceof SkeletonHorseEntity ||
            horseInstance instanceof ZombieHorseEntity) {
            return ModConfig.getInstance().swimDead;
        }

        if (horseInstance instanceof CamelEntity) {
            return ModConfig.getInstance().swimCamel;
        }

        return false; // you should never be able to reach this, but if you do it defaults to vanilla behavior
    }
}
