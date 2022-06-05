package net.F53.HorseBuff.mixin.Server;

import net.F53.HorseBuff.HorseBuffInit;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public class clearFutureTickEvents {
    @Inject(method = "close()V", at = @At("HEAD"))
    public void clearFutureTickEvents(CallbackInfo ci) {
        HorseBuffInit.runNextTick.clear();
        HorseBuffInit.toRun.clear();
    }
}
