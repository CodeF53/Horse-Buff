package net.F53.HorseBuff.mixin.Server;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BoatEntity.class)
public abstract class AllowRaft {
    @ModifyReturnValue(method = "isSmallerThanBoat", at = @At("RETURN"))
    private boolean allowRaft(boolean original, @Local(argsOnly = true)Entity entity){
        if (entity instanceof AbstractHorseEntity && ((BoatEntity)(Object)this).getVariant() == BoatEntity.Type.BAMBOO)
            return true;
        return original;
    }
}
