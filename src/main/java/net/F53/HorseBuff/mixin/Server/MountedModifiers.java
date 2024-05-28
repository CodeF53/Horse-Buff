package net.F53.HorseBuff.mixin.Server;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

// adds DataPack attribute modifiers to player & horse while mounted to
// - increase horse's StepHeight by 10% (when enabled)
// - remove BreakSpeed debuff from not being grounded (when enabled)
@Mixin(value = PlayerEntity.class, priority = 960)
public abstract class MountedModifiers extends LivingEntity {
    protected MountedModifiers(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    EntityAttributeModifier mountedStepHeight = new EntityAttributeModifier("HorseBuff-MountedStepHeight", 0.1, EntityAttributeModifier.Operation.ADD_VALUE);
    @Unique
    EntityAttributeModifier mountedBreakSpeed = new EntityAttributeModifier("HorseBuff-MountedBreakSpeed", 5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    @Override
    public boolean startRiding(Entity entity, boolean force) {
        boolean result = super.startRiding(entity, force);
        if (!(super.getWorld() instanceof ServerWorld && entity instanceof AbstractHorseEntity horse))
            return result;

        if (ModConfig.getInstance().stepHeight) {
            EntityAttributeInstance stepHeight = horse.getAttributeInstance(EntityAttributes.GENERIC_STEP_HEIGHT);
            if (stepHeight != null) stepHeight.addTemporaryModifier(mountedStepHeight);
        }

        if (ModConfig.getInstance().breakSpeed) {
            EntityAttributeInstance breakSpeed = getAttributeInstance(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED);
            if (breakSpeed != null) breakSpeed.addTemporaryModifier(mountedBreakSpeed);
        }
        return result;
    }

    @Override
    public boolean startRiding(Entity entity) {
        return this.startRiding(entity, false);
    }

    @Override
    public void stopRiding() {
        if (!(super.getWorld() instanceof ServerWorld && getVehicle() instanceof AbstractHorseEntity horse)) {
            super.stopRiding();
            return;
        }

        EntityAttributeInstance stepHeight = horse.getAttributeInstance(EntityAttributes.GENERIC_STEP_HEIGHT);
        if (stepHeight != null) stepHeight.removeModifier(mountedStepHeight);

        EntityAttributeInstance breakSpeed = getAttributeInstance(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED);
        if (breakSpeed != null) breakSpeed.removeModifier(mountedBreakSpeed);

        super.stopRiding();
    }
}
