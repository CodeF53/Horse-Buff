package net.F53.HorseBuff.mixin.Server;

import net.F53.HorseBuff.utils.TickScheduler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = World.class, priority = 960)
public class ClearFutureTickEvents {
    @Inject(method = "close()V", at = @At("HEAD"))
    public void clearFutureTickEvents(CallbackInfo ci) {
        TickScheduler.runNextTick.clear();
        TickScheduler.toRun.clear();
    }
}
