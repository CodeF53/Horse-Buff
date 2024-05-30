package net.F53.HorseBuff.mixin.Client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.F53.HorseBuff.utils.RenderUtils;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.ColorHelper.Argb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntityRenderer.class, priority = 960)
public abstract class HorseRenderer {
    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    void getAlpha(CallbackInfo ci, @Local(argsOnly = true) LivingEntity entity, @Share("alpha") LocalIntRef alpha) {
        if (entity instanceof AbstractHorseEntity)
            alpha.set(RenderUtils.getAlpha(entity));
    }

    @ModifyArg(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"),
            index = 4)
    int setOpacityAndChromaForRender(int color, @Local(argsOnly = true, ordinal = 1) float tickDelta, @Local(argsOnly = true) LivingEntity entity, @Share("alpha") LocalIntRef alpha) {
        if (!(entity instanceof AbstractHorseEntity)) return color;
        if (RenderUtils.isJeb(entity)) {
            // see net/minecraft/client/render/entity/feature/SheepWoolFeatureRenderer
            int dyeIndex = entity.age / 25 + entity.getId();
            int numDyes = DyeColor.values().length;
            int currentDye = SheepEntity.getRgbColor(DyeColor.byId(dyeIndex % numDyes));
            int nextDye = SheepEntity.getRgbColor(DyeColor.byId((dyeIndex + 1) % numDyes));
            float dyeTransitionProgress = ((float) (entity.age % 25) + tickDelta) / 25.0f;
            color = Argb.lerp(dyeTransitionProgress, currentDye, nextDye);
            // increase brightness by a bit because the horse texture is a bit dark
            color = Argb.getArgb(Math.min(Argb.getRed(color) * 2, 255), Math.min(Argb.getGreen(color) * 2, 255), Math.min(Argb.getBlue(color) * 2, 255));
        }
        return Argb.withAlpha(alpha.get(), color);
    }

    @ModifyArg(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getRenderLayer(Lnet/minecraft/entity/LivingEntity;ZZZ)Lnet/minecraft/client/render/RenderLayer;"),
            index = 2)
    boolean makeRenderLayerTranslucent(boolean translucent, @Local(argsOnly = true) LivingEntity entity, @Share("alpha") LocalIntRef alphaRef) {
        if (translucent) return true;
        if (entity instanceof AbstractHorseEntity)
            return alphaRef.get() != 255;
        return false;
    }
}
