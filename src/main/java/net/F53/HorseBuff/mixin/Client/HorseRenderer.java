package net.F53.HorseBuff.mixin.Client;

import net.F53.HorseBuff.HorseBuffInit;
import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

import static net.F53.HorseBuff.HorseBuffInit.isJeb;

@Mixin(LivingEntityRenderer.class)
public abstract class HorseRenderer<T extends LivingEntity, M extends EntityModel<T>> {
    @Shadow
    protected M model;

    @Shadow
    protected abstract boolean isVisible(T entity);

    @Shadow
    protected abstract float getAnimationCounter(T entity, float tickDelta);

    @Shadow
    protected abstract RenderLayer getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline);

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "net/minecraft/client/render/entity/LivingEntityRenderer.isVisible (Lnet/minecraft/entity/LivingEntity;)Z", shift = At.Shift.AFTER))
    void pitchFade(T livingEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
        if (livingEntity instanceof HorseBaseEntity && isVisible(livingEntity)) {
            float r = 1;
            float g = 1;
            float b = 1;
            float opacity = 1;

            int overlay = LivingEntityRenderer.getOverlay(livingEntity, getAnimationCounter(livingEntity, g));
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            RenderLayer renderLayer = RenderLayer.getEntityTranslucentCull((((LivingEntityRenderer) (Object) this).getTexture(livingEntity)));

            if (livingEntity.hasPassenger(MinecraftClient.getInstance().player) && ModConfig.getInstance().pitchFade) {
                //As a player looks down, opacity of horse should decrease.
                //100% at 30 degrees
                //10% at 50+ degrees
                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                assert player != null;
                opacity = (Math.max(Math.min(100, -4.5f * (player.renderPitch - 50)), 10)) / 100;
            } if (isJeb(livingEntity)) {
                // TODO: desynchronize horse chroma
                // adding the ID is supposed to do that, but it doesnt work
                float hueOffset = livingEntity.getId() + (System.currentTimeMillis()%5000)/5000f;
                HorseBuffInit.LOGGER.info("hueOffset = "+hueOffset);
                Color color = new Color(Color.HSBtoRGB(hueOffset, 1, 1));
                r = color.getRed()/255f;
                g = color.getGreen()/255f;
                b = color.getBlue()/255f;
            }
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);

            this.model.render(matrixStack, vertexConsumer, light, overlay, r, g, b, opacity);
        }
    }

    @Redirect(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "net/minecraft/client/render/entity/LivingEntityRenderer.isVisible (Lnet/minecraft/entity/LivingEntity;)Z"))
    boolean dontRender(LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> instance, T livingEntity) {
        if (!isVisible(livingEntity)){
            return false;
        }
        if (livingEntity instanceof HorseBaseEntity) {
            if (isJeb(livingEntity))
                return false;
            if (livingEntity.hasPassenger(MinecraftClient.getInstance().player) && ModConfig.getInstance().pitchFade)
                return false;
        }
        return isVisible(livingEntity);
    }
}
