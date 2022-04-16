package net.F53.HorseBuff.mixin.Client;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class PitchFade<T extends LivingEntity, M extends EntityModel<T>> {
    @Shadow protected M model;

    @Shadow protected abstract boolean isVisible(T entity);

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "net/minecraft/client/render/entity/LivingEntityRenderer.isVisible (Lnet/minecraft/entity/LivingEntity;)Z", shift = At.Shift.AFTER))
    void pitchFade(T livingEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci){
        if (livingEntity instanceof HorseBaseEntity && livingEntity.hasPassenger(MinecraftClient.getInstance().player)) {
            if (ModConfig.getInstance().pitchFade) {
                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                assert player != null;

                //As a player looks down, opacity of horse should decrease.
                //100% at 30 degrees
                //10% at 50+ degrees
                float Opacity = (Math.max(Math.min(100, -4.5f * (player.renderPitch - 50)), 10)) / 100;
                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentCull
                        (((LivingEntityRenderer) (Object) this).getTexture(livingEntity)));
                this.model.render(matrixStack, vertexConsumer, light, 0, 1.0f, 1.0f, 1.0f, Opacity);
            }
        }
    }

    @Redirect(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
    at = @At(value = "INVOKE", target = "net/minecraft/client/render/entity/LivingEntityRenderer.isVisible (Lnet/minecraft/entity/LivingEntity;)Z"))
    boolean dontRender(LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> instance, T livingEntity) {
        if ((livingEntity instanceof HorseBaseEntity) && livingEntity.hasPassenger(MinecraftClient.getInstance().player)) {
            if (ModConfig.getInstance().pitchFade) {
                return false;
            }
        }
        return isVisible(livingEntity);
    }
}
