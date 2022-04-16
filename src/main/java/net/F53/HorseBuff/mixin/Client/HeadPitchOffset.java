package net.F53.HorseBuff.mixin.Client;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(HorseEntityModel.class)
public class HeadPitchOffset<T extends LivingEntity, M extends EntityModel<T>> {
    @Shadow @Final protected ModelPart head;

    // Thanks dorianpb#9929 for the tip on where to mixin
    @Inject(method = "setAngles(Lnet/minecraft/entity/passive/HorseBaseEntity;FFFFF)V", at = @At("TAIL"))
    void headPitch(HorseBaseEntity horseBaseEntity, float f, float g, float h, float i, float j, CallbackInfo ci){
        if (horseBaseEntity.hasPassenger(MinecraftClient.getInstance().player))
            this.head.pitch += ModConfig.getInstance().horseHeadAngleOffset/100f;
    }
}
