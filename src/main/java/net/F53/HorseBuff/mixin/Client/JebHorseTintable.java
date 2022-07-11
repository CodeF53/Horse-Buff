package net.F53.HorseBuff.mixin.Client;

import net.minecraft.client.render.entity.HorseEntityRenderer;
import net.minecraft.entity.passive.HorseColor;
import net.minecraft.entity.passive.HorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.F53.HorseBuff.HorseBuffInit.isJeb;

@Mixin(value = HorseEntityRenderer.class, priority = 1050)
public class JebHorseTintable {
    @Redirect(method = "getTexture(Lnet/minecraft/entity/passive/HorseEntity;)Lnet/minecraft/util/Identifier;",
    at = @At(value = "INVOKE", target = "net/minecraft/entity/passive/HorseEntity.getColor ()Lnet/minecraft/entity/passive/HorseColor;"))
    HorseColor jebHorseTintable(HorseEntity instance){
        if (isJeb(instance)){
            return HorseColor.WHITE;
        }
        return instance.getColor();
    }
}
