package net.F53.HorseBuff.mixin.Client;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Horse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.F53.HorseBuff.HorseBuffInit.getOpacity;

import com.mojang.blaze3d.vertex.PoseStack;

@Mixin(HorseArmorLayer.class)
public class TransparentArmor {

    private float opacity;

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
    at = @At("HEAD"))
    void fetchOpacity(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, Horse horseEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
        if (horseEntity.isWearingArmor() && ModConfig.getInstance().pitchFade.enabled && horseEntity.hasPassenger(Minecraft.getInstance().player)) {
            LocalPlayer player = Minecraft.getInstance().player;
            assert player != null;
            opacity = getOpacity(player);
        } else {
            opacity = 1;
        }
    }

    @Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
    at = @At(value = "INVOKE", target = "net/minecraft/client/render/RenderLayer.getEntityCutoutNoCull (Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    RenderType makeRenderLayerTranslucent(ResourceLocation texture) {
        return RenderType.itemEntityTranslucentCull(texture);
    }

    @ModifyConstant(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
    constant = @Constant(floatValue = 1.0f))
    float setOpacityForRender(float value){
        return opacity;
    }
}