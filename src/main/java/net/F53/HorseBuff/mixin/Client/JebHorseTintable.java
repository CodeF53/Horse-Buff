package net.F53.HorseBuff.mixin.Client;

import net.minecraft.client.render.entity.HorseEntityRenderer;
import net.minecraft.client.render.entity.state.HorseEntityRenderState;
import net.minecraft.entity.passive.HorseColor;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

import static net.F53.HorseBuff.utils.RenderUtils.isJeb;

@Mixin(value = HorseEntityRenderer.class, priority = 960)
public class JebHorseTintable {
    @Final
    @Shadow
    private static Map<HorseColor, Identifier> TEXTURES;

    @Redirect(method = "getTexture(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;)Lnet/minecraft/util/Identifier;",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/HorseEntityRenderer;getTexture(Lnet/minecraft/client/render/entity/state/HorseEntityRenderState;)Lnet/minecraft/util/Identifier;"))
    Identifier jebHorseTintable(HorseEntityRenderer instance, HorseEntityRenderState horseEntityRenderState){
        if (isJeb(horseEntityRenderState)){
            return TEXTURES.get(HorseColor.WHITE);
        }
        return TEXTURES.get(horseEntityRenderState.color);
    }
}
