package net.F53.HorseBuff.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.F53.HorseBuff.config.ModConfig.fairBreeds;

// Make breeding fair
@Mixin(HorseBaseEntity.class)
abstract class HorseChildAttributeMixin extends AnimalEntity {

    protected HorseChildAttributeMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "setChildAttributes", at = @At(value = "TAIL"))
    protected void onSetChildAttributes(PassiveEntity mate, HorseBaseEntity child, CallbackInfo ci) {
        if (fairBreeds) {
            // Logic - Set stat to average parent stat, +/- some random amount, limited to vanilla min/max values

            // Health
            // 15-30
            double Health = Logic(this.getAttributeBaseValue(EntityAttributes.GENERIC_MAX_HEALTH), mate.getAttributeBaseValue(EntityAttributes.GENERIC_MAX_HEALTH), 15, 30);
            child.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(Health);

            // Jump
            // 0.4-1.0
            double JumpStrength = Logic(this.getAttributeBaseValue(EntityAttributes.HORSE_JUMP_STRENGTH), mate.getAttributeBaseValue(EntityAttributes.HORSE_JUMP_STRENGTH), 0.4, 1.0);
            child.getAttributeInstance(EntityAttributes.HORSE_JUMP_STRENGTH).setBaseValue(JumpStrength);

            // Movement
            // 0.1125 - 0.3375
            double MovementSpeed = Logic(this.getAttributeBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED), mate.getAttributeBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED), 0.1125, 0.3375);
            child.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(MovementSpeed);
        }
    }

    private double Logic(double A, double B, double min, double max){
        double variance = (max-min)/10;

        return clamp((A+B)/2 + ((Math.random() * (variance * 2)) - variance), min, max);
    }

    private double clamp(double value, double min, double max){
        return value > max ? max : Math.max(value, min);
    }
}