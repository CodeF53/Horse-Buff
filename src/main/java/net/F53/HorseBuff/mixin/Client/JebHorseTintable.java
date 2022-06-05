package net.F53.HorseBuff.mixin.Client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.F53.HorseBuff.utils.RenderUtils.isJeb;

import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Variant;

@Mixin(HorseRenderer.class)
public class JebHorseTintable {
    @Redirect(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/horse/Horse;)Lnet/minecraft/resources/ResourceLocation;",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/Horse;getVariant()Lnet/minecraft/world/entity/animal/horse/Variant;"))
    Variant jebHorseTintable(Horse instance){
        if (isJeb(instance)){
            return Variant.WHITE;
        }
        return instance.getVariant();
    }
}
