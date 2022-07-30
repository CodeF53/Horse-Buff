package net.F53.HorseBuff.mixin.Server;

import net.F53.HorseBuff.config.ModConfig;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

// Disable breakspeed debuff
@SuppressWarnings("unused")
@Mixin(Player.class)
public abstract class BreakSpeed extends LivingEntity {
    protected BreakSpeed(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyConstant(method = "getDestroySpeed", constant = @Constant(floatValue = 5.0F, ordinal = 1))
    private float HorseBreakSpeed(float speedMultiplier){
        if (this.isPassenger() && this.getVehicle() instanceof AbstractHorse && ModConfig.getInstance().breakSpeed)
            return 1.0F;
        return speedMultiplier;
    }
}
