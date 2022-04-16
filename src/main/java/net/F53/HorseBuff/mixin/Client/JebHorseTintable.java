package net.F53.HorseBuff.mixin.Client;

import net.minecraft.client.render.entity.HorseEntityRenderer;
import net.minecraft.entity.passive.HorseColor;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

import static net.F53.HorseBuff.HorseBuffInit.isJeb;

@Mixin(HorseEntityRenderer.class)
public class JebHorseTintable {
    @Shadow @Final private static Map<HorseColor, Identifier> TEXTURES;

    @Redirect(method = "getTexture(Lnet/minecraft/entity/passive/HorseEntity;)Lnet/minecraft/util/Identifier;",
    at = @At(value = "INVOKE", target = "net/minecraft/entity/passive/HorseEntity.getColor ()Lnet/minecraft/entity/passive/HorseColor;"))
    HorseColor jebHorseTintable(HorseEntity instance){
        if (isJeb(instance)){
            return HorseColor.WHITE;
        }
        return instance.getColor();
    }
}
