package net.F53.HorseBuff.mixin.Server;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

// Disable movement checks for Horses, fixing MC-100830
@Mixin(ServerGamePacketListenerImpl.class)
public class MovementCheck {
	@Shadow public ServerPlayer player;

	@ModifyConstant(method = "handleMoveVehicle", constant = @Constant(doubleValue = 0.0625D))
	private double horseNoMovementCheck(double value){
		if (this.player.getRootVehicle() instanceof AbstractHorse && ModConfig.getInstance().rubberBand)
			return Double.POSITIVE_INFINITY;
		return value;
	}
}
