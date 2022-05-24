package net.F53.HorseBuff.mixin.Client;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HorseMarkingFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.F53.HorseBuff.HorseBuffInit.getOpacity;

@Mixin(HorseMarkingFeatureRenderer.class)
public class TransparentMarkings {

    private float opacity;

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
    at = @At("HEAD"))
    void fetchOpacity(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, HorseEntity horseEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
        if (ModConfig.getInstance().pitchFade.enabled && horseEntity.hasPassenger(MinecraftClient.getInstance().player)) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            assert player != null;
            opacity = getOpacity(player);
        } else {
            opacity = 1;
        }
    }

    @Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
            at = @At(value = "INVOKE", target = "net/minecraft/client/render/RenderLayer.getEntityTranslucent (Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    RenderLayer makeRenderLayerTranslucent(Identifier texture) {
        return RenderLayer.getItemEntityTranslucentCull(texture);
    }

    @ModifyConstant(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
    constant = @Constant(floatValue = 1.0f))
    float setOpacityForRender(float value){
        return opacity;
    }
}
