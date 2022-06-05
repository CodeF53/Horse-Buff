package net.F53.HorseBuff.mixin.Client;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.awt.*;

import static net.F53.HorseBuff.HorseBuffInit.*;

import com.mojang.blaze3d.vertex.PoseStack;

@Mixin(LivingEntityRenderer.class)
public abstract class HorseRenderer<T extends LivingEntity, M extends EntityModel<T>> {

    private float opacity;

    private float r;
    private float g;
    private float b;

    @Shadow protected abstract RenderType getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline);

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
    at = @At("HEAD"))
    void fetchOpacityAndJeb(T livingEntity, float yaw, float tickDelta, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, CallbackInfo ci) {
        opacity = 1;
        r = 1;
        g = 1;
        b = 1;
        if (livingEntity instanceof AbstractHorse) {
            if (ModConfig.getInstance().pitchFade.enabled && livingEntity.hasPassenger(Minecraft.getInstance().player)) {
                LocalPlayer player = Minecraft.getInstance().player;
                assert player != null;
                opacity = getOpacity(player);
            }
            if (livingEntity instanceof Horse && isJeb(livingEntity)) {
                float hueOffset = (livingEntity.getUUID().hashCode()%5000)/5000f + (System.currentTimeMillis()%5000)/5000f;
                Color color = new Color(Color.HSBtoRGB(hueOffset, 0.8f, 1));
                r = color.getRed()/255f;
                g = color.getGreen()/255f;
                b = color.getBlue()/255f;
            }
        }
    }

    @Redirect(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
    at = @At(value = "INVOKE", target = "net/minecraft/client/render/entity/LivingEntityRenderer.getRenderLayer (Lnet/minecraft/entity/LivingEntity;ZZZ)Lnet/minecraft/client/render/RenderLayer;"))
    RenderType makeRenderLayerTranslucent(LivingEntityRenderer<T, ? extends EntityModel<T>> instance, T entity, boolean showBody, boolean translucent, boolean showOutline) {
        if (entity instanceof AbstractHorse) {
            return RenderType.itemEntityTranslucentCull(instance.getTextureLocation(entity));
        }
        return getRenderLayer(entity, showBody, translucent, showOutline);
    }

    @ModifyArgs(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
    at = @At(value = "INVOKE", target = "net/minecraft/client/render/entity/model/EntityModel.render (Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    void setOpacityAndChromaForRender(Args args){
        args.set(4, r);
        args.set(5, g);
        args.set(6, b);

        args.set(7, opacity);
    }
}
