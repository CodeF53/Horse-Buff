package net.F53.HorseBuff.mixin.Client;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Llama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.F53.HorseBuff.utils.RenderUtils.getOpacity;

import com.mojang.blaze3d.vertex.PoseStack;

@Mixin(LlamaDecorLayer.class)
public class TransparentLlamaDecor {

    private float opacity;

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Llama;FFFFFF)V",
    at = @At("HEAD"))
    void fetchOpacity(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, Llama llamaEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        if (ModConfig.getInstance().pitchFade.enabled && llamaEntity.hasPassenger(Minecraft.getInstance().player)) {
            LocalPlayer player = Minecraft.getInstance().player;
            assert player != null;
            opacity = getOpacity(player);
        } else {
            opacity = 1;
        }
    }

    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Llama;FFFFFF)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;entityCutoutNoCull(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"))
    RenderType makeRenderLayerTranslucent(ResourceLocation texture) {
        return RenderType.itemEntityTranslucentCull(texture);
    }

    @ModifyConstant(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Llama;FFFFFF)V",
    constant = @Constant(floatValue = 1.0f))
    float setOpacityForRender(float value){
        return opacity;
    }
}
