package net.F53.HorseBuff.mixin.Server;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Make breeding fair
@Mixin(AbstractHorse.class)
abstract class FairBreeds extends Animal {

    protected FairBreeds(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "setOffspringAttributes", at = @At(value = "TAIL"))
    protected void setOffspringAttributes(AgeableMob mate, AbstractHorse child, CallbackInfo ci) {
        if (ModConfig.getInstance().fairBreeds) {
            // Logic - Set stat to average parent stat, +/- some random amount, limited to vanilla min/max values

            // Health
            // 15-30
            double Health = Logic(this.getAttributeBaseValue(Attributes.MAX_HEALTH), mate.getAttributeBaseValue(Attributes.MAX_HEALTH), 15, 30);
            child.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Health);

            // Jump
            // 0.4-1.0
            double JumpStrength = Logic(this.getAttributeBaseValue(Attributes.JUMP_STRENGTH), mate.getAttributeBaseValue(Attributes.JUMP_STRENGTH), 0.4, 1.0);
            child.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(JumpStrength);

            // Movement
            // 0.1125 - 0.3375
            double MovementSpeed = Logic(this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED), mate.getAttributeBaseValue(Attributes.MOVEMENT_SPEED), 0.1125, 0.3375);
            child.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(MovementSpeed);
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