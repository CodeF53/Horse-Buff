package net.F53.HorseBuff.mixin.Client;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.List;

import static net.F53.HorseBuff.HorseBuffInit.isJeb;

@Mixin(LivingEntityRenderer.class)
public abstract class HorseRenderer<T extends LivingEntity, M extends EntityModel<T>> {
    @Shadow
    protected M model;

    @Final
    @Shadow
    protected List<FeatureRenderer<T, M>> features;

    @Shadow
    protected abstract boolean isVisible(T entity);

    @Shadow
    protected abstract float getAnimationCounter(T entity, float tickDelta);

    @Shadow
    protected abstract RenderLayer getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline);


    @Shadow protected abstract float getAnimationProgress(T entity, float tickDelta);

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "net/minecraft/client/render/entity/LivingEntityRenderer.isVisible (Lnet/minecraft/entity/LivingEntity;)Z", shift = At.Shift.AFTER))
    void pitchFade(T livingEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
        if (livingEntity instanceof HorseBaseEntity && isVisible(livingEntity)) {
            boolean shouldRender = false;

            float r = 1;
            float g = 1;
            float b = 1;
            float opacity = 1;

            int overlay = LivingEntityRenderer.getOverlay(livingEntity, getAnimationCounter(livingEntity, g));
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            RenderLayer renderLayer = getRenderLayer(livingEntity, false, true, false);

            if (livingEntity.hasPassenger(MinecraftClient.getInstance().player) && ModConfig.getInstance().pitchFade.enabled) {
                shouldRender = true;
                //As a player looks down, opacity of horse decreases.
                //100% at fadeStartAngle degrees
                //10% at fadeEndAngle+ degrees
                int fadeStartAngle = ModConfig.getInstance().pitchFade.startAngle;
                int fadeEndAngle = ModConfig.getInstance().pitchFade.endAngle;
                int minOpacity = 100 - ModConfig.getInstance().pitchFade.maxTransparency;
                float rate = (100f-minOpacity)/(fadeStartAngle-fadeEndAngle);

                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                assert player != null;
                opacity = (Math.max(Math.min(100, rate * (player.renderPitch - fadeEndAngle)), minOpacity)) / 100;
            }
            if (isJeb(livingEntity)) {
                shouldRender = true;
                float hueOffset = (livingEntity.getUuid().hashCode()%5000)/5000f + (System.currentTimeMillis()%5000)/5000f;
                Color color = new Color(Color.HSBtoRGB(hueOffset, 0.8f, 1));
                r = color.getRed()/255f;
                g = color.getGreen()/255f;
                b = color.getBlue()/255f;
            }
            if (shouldRender) {
                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
                // for some reason opacity with this technique doesn't go below 10
                this.model.render(matrixStack, vertexConsumer, light, overlay, r, g, b, opacity);

                // we need to render armor again if it has it because z-fighting
                // at this point we know it's a horse with a mount on it, so a lot of checks from vanilla code can be removed
                // only do this if we actually have armor (small optimization)
                if (((HorseBaseEntity) livingEntity).hasArmorInSlot()) {
                    // calculate limbAngle and limbDistance for ourselves, because despite coming after them being defined, we don't get access to them.
                    float limbDistance = MathHelper.lerp(g, (livingEntity).lastLimbDistance, livingEntity.limbDistance);
                    float limbAngle = (livingEntity).limbAngle - livingEntity.limbDistance * (1.0f - tickDelta);
                    if (limbDistance > 1.0f) { limbDistance = 1.0f; }

                    for (FeatureRenderer<T, M> feature : this.features) {
                        feature.render(matrixStack, vertexConsumerProvider, light, livingEntity, limbAngle,
                                limbDistance, tickDelta, getAnimationProgress(livingEntity,tickDelta),
                                livingEntity.getHeadYaw(), livingEntity.getPitch());
                    }
                }
            }
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
            if (livingEntity.hasPassenger(MinecraftClient.getInstance().player) && ModConfig.getInstance().pitchFade.enabled)
                return false;
        }
        return isVisible(livingEntity);
    }
}
