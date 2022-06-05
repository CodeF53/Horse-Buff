package net.F53.HorseBuff.mixin.Server;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// Lower wander speed for saddled horses
@Mixin(AbstractHorse.class)
public abstract class NoWander extends Mob {
    protected NoWander(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow public abstract boolean isSaddled();

    @ModifyArg(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AnimalEntity;travel(Lnet/minecraft/util/math/Vec3d;)V", ordinal = 0))
    private Vec3 lowerWanderSpeed(Vec3 input) {
        if (ModConfig.getInstance().noWander && isSaddled() && this.getLeashHolder() == null)
            return(Vec3.ZERO);
        return input;
    }
}