package net.F53.HorseBuff.mixin.Server;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// Lower wander speed for saddled horses
@Mixin(value = AbstractHorseEntity.class, priority = 1050)
public abstract class NoWander extends MobEntity {
    protected NoWander(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract boolean isSaddled();

    @ModifyArg(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AnimalEntity;travel(Lnet/minecraft/util/math/Vec3d;)V", ordinal = 0))
    private Vec3d lowerWanderSpeed(Vec3d input) {
        if (ModConfig.getInstance().noWander && isSaddled() && this.getHoldingEntity() == null)
            return(Vec3d.ZERO);
        return input;
    }
}
