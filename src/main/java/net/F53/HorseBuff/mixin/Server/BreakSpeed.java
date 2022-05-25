package net.F53.HorseBuff.mixin.Server;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

// Disable breakspeed debuff
@Mixin(PlayerEntity.class)
public abstract class BreakSpeed extends LivingEntity {
    protected BreakSpeed(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyConstant(method = "getBlockBreakingSpeed(Lnet/minecraft/block/BlockState;)F", constant = @Constant(floatValue = 5.0F))
    private float HorseBreakSpeed(float speedMultiplier){
        if (this.getRootVehicle() instanceof AbstractHorseEntity && ModConfig.getInstance().breakSpeed)
            return 1.0F;
        return speedMultiplier;
    }
}
