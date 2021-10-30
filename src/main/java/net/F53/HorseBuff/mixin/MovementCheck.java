package net.F53.HorseBuff.mixin;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import net.minecraft.entity.passive.HorseBaseEntity;

// Disable movement checks for Horses, fixing MC-100830
@Mixin(ServerPlayNetworkHandler.class)
public class MovementCheck {
	@Shadow public ServerPlayerEntity player;

	@ModifyConstant(method = "onVehicleMove(Lnet/minecraft/network/packet/c2s/play/VehicleMoveC2SPacket;)V", constant = @Constant(doubleValue = 0.0625D))
	private double horseNoMovementCheck(double value){
		if (this.player.getRootVehicle() instanceof HorseBaseEntity)
			return Double.POSITIVE_INFINITY;
		return value;
	}
}
