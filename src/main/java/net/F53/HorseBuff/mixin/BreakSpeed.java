package net.F53.HorseBuff.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import net.minecraft.entity.passive.HorseBaseEntity;

// Disable breakspeed debuff
@Mixin(PlayerEntity.class)
public abstract class BreakSpeed extends LivingEntity {
    protected BreakSpeed(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyConstant(method = "getBlockBreakingSpeed(Lnet/minecraft/block/BlockState;)F", constant = @Constant(floatValue = 5.0F))
    private float HorseBreakSpeed(float speedMultiplier){
        if (this.getRootVehicle() instanceof HorseBaseEntity)
            return 1.0F;
        return speedMultiplier;
    }
}
