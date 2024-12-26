package net.F53.HorseBuff.mixin.Client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.F53.HorseBuff.render.entity.model.ExtendedRideableEquippableEntityModel;
import net.F53.HorseBuff.utils.RenderUtils;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.render.entity.model.LlamaEntityModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = EquipmentRenderer.class, priority = 960)
public abstract class TransparentEquipment {

    @WrapOperation(method = "render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getArmorCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    RenderLayer makeRenderLayerTranslucent(Identifier texture, Operation<RenderLayer> original, @Local(argsOnly = true) Model model, @Share("alpha") LocalIntRef alpha) {
        if(model instanceof ExtendedRideableEquippableEntityModel extendedRideableEquippableEntityModel) {
            alpha.set(RenderUtils.getAlpha(extendedRideableEquippableEntityModel.horsebuff$isPlayerPassenger()));
        }
        if (alpha.get() == 255) return original.call(texture);
        return RenderLayer.getItemEntityTranslucentCull(texture);
    }

    @ModifyArg(method = "render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/Model;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"),
            index = 4)
    int setOpacityForRender(int color, @Local(argsOnly = true) Model model, @Share("alpha") LocalIntRef alpha) {
        if(model instanceof HorseEntityModel || model instanceof LlamaEntityModel) {
            return ColorHelper.withAlpha(Math.min(Math.max(0, ColorHelper.getAlpha(color)), alpha.get()), color);
        }
        return color;
    }
}
