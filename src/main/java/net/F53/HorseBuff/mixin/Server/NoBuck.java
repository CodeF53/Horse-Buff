package net.F53.HorseBuff.mixin.Server;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.entity.passive.AbstractHorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AbstractHorseEntity.class, priority = 960)
public class NoBuck {
    @ModifyReturnValue(method = "isAngry", at = @At("RETURN"))
    private boolean isAngry(boolean original) {
        AbstractHorseEntity instance = ((AbstractHorseEntity)(Object)this);
        if (ModConfig.getInstance().noBuck
            && !instance.jumping
            && instance.isTame()
            && instance.getControllingPassenger() != null) {
            return false;
        }
        return original;
    }
}
