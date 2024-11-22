package net.F53.HorseBuff.mixin.Client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.feature.HorseMarkingFeatureRenderer;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static net.F53.HorseBuff.utils.RenderUtils.getAlpha;

@Mixin(value = HorseMarkingFeatureRenderer.class, priority = 960)
public class TransparentMarkings {
    @WrapOperation(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getEntityTranslucent(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    RenderLayer makeRenderLayerTranslucent(Identifier texture, Operation<RenderLayer> original, @Local(argsOnly = true) HorseEntity horseEntity, @Share("alpha") LocalIntRef alpha) {
        alpha.set(getAlpha(horseEntity));
        if (alpha.get() == 255) return original.call(texture);
        return RenderLayer.getItemEntityTranslucentCull(texture);
    }

    @WrapOperation(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/HorseEntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V"))
    void modifyOverlayAlpha(HorseEntityModel<?> instance, MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, Operation<Void> original, @Share("alpha") LocalIntRef alpha) {
        instance.render(matrixStack, vertexConsumer, light, overlay, ColorHelper.withAlpha(alpha.get(), 0xFFFFFF));
    }
}
