package net.F53.HorseBuff.mixin.Server;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AbstractHorseEntity.class, priority = 960)
public class NoBuck {
    @ModifyReturnValue(method = "isAngry", at = @At("RETURN"))
    private boolean isAngry(boolean original) {
        AbstractHorseEntity instance = hb$thiz();
        if (ModConfig.getInstance().noBuck
            && !instance.jumping
            && instance.isTame()
            && instance.getControllingPassenger() != null) {
            return false;
        }
        return original;
    }

    @Unique
    private AbstractHorseEntity hb$thiz() {
        return ((AbstractHorseEntity)(Object)this);
    }
}
