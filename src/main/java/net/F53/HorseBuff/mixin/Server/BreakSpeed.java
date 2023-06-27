package net.F53.HorseBuff.mixin.Server;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// Disable breakspeed debuff for not being grounded (while on horseback)
@Mixin(value = PlayerEntity.class, priority = 960)
public abstract class BreakSpeed extends LivingEntity {
    protected BreakSpeed(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method = "getBlockBreakingSpeed", at = @At("RETURN"))
    private float horseBreakSpeed(float original) {
        if (!this.onGround && this.getRootVehicle() instanceof AbstractHorseEntity && ModConfig.getInstance().breakSpeed)
            return original * 5f;
        return original;
    }
}
