package net.F53.HorseBuff.mixin.Client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.F53.HorseBuff.HorseBuffInit.isJeb;

import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Variant;

@Mixin(HorseRenderer.class)
public class JebHorseTintable {
    @Redirect(method = "getTexture(Lnet/minecraft/entity/passive/HorseEntity;)Lnet/minecraft/util/Identifier;",
    at = @At(value = "INVOKE", target = "net/minecraft/entity/passive/HorseEntity.getColor ()Lnet/minecraft/entity/passive/HorseColor;"))
    Variant jebHorseTintable(Horse instance){
        if (isJeb(instance)){
            return Variant.WHITE;
        }
        return instance.getVariant();
    }
}
