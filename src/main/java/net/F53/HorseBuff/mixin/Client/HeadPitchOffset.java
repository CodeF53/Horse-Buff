package net.F53.HorseBuff.mixin.Client;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(HorseModel.class)
public class HeadPitchOffset<T extends LivingEntity, M extends EntityModel<T>> {
    @Shadow @Final protected ModelPart head;

    // Thanks dorianpb#9929 for the tip on where to mixin
    @Inject(method = "setAngles(Lnet/minecraft/entity/passive/HorseBaseEntity;FFFFF)V", at = @At("TAIL"))
    void headPitch(AbstractHorse horseBaseEntity, float f, float g, float h, float i, float j, CallbackInfo ci){
        if (horseBaseEntity.hasPassenger(Minecraft.getInstance().player) && Minecraft.getInstance().options.getCameraType().isFirstPerson())
            this.head.xRot = Math.min(this.head.xRot + ModConfig.getInstance().horseHeadAngleOffset/100f, 1.5f);
    }
}
