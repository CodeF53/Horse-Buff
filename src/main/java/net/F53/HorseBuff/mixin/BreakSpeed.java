package net.F53.HorseBuff.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import net.minecraft.entity.passive.HorseBaseEntity;

// Disable breakspeed debuff
@Mixin(PlayerEntity.class)
public class BreakSpeed {
    /*public ServerPlayerEntity player;

    @ModifyConstant(method = "getBlockBreakingSpeed(Lnet/minecraft/block/BlockState;)F", constant = @Constant(floatValue = 5.0F))
    private float HorseBreakSpeed(float speedMultiplier){
        if (this.player.getRootVehicle() instanceof HorseBaseEntity)
            return 1.0F;
        return speedMultiplier;
    }*/
}
