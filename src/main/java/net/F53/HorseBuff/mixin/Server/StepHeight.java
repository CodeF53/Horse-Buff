package net.F53.HorseBuff.mixin.Server;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.entity.passive.AbstractHorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

// Increases step-height for horses by 10%, allowing you to ride up a path to a full block\
// video displaying issue this fixes: https://user-images.githubusercontent.com/37855219/167529335-bea46c66-4ee2-4e3f-9391-f8cf2960d58a.mp4
@Mixin(value = AbstractHorseEntity.class, priority = 960)
public class StepHeight {
	@ModifyConstant(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", constant = @Constant(floatValue = 1.0f))
	private float horseHigherStepHeight(float value) {
		if (ModConfig.getInstance().stepHeight)
			return 1.1f;
		return value;
	}
}
