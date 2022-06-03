package net.f53.horsebuff.mixin.Client;

import net.f53.horsebuff.HorseBuffInit;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;

import net.minecraft.entity.passive.EntityHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

import static net.f53.horsebuff.HorseBuffInit.getOpacity;
import static net.f53.horsebuff.HorseBuffInit.isJeb;

@Mixin(RendererLivingEntity.class)
public abstract class HorseRenderer<T extends EntityLivingBase> {

    private float opacity;

    private float r;
    private float g;
    private float b;

    @Inject(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At("HEAD"))
    void fetchOpacityAndJeb(T livingEntity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        HorseBuffInit.LOGGER.info("doing a thing");
        opacity = 1;
        r = 1;
        g = 1;
        b = 1;
        if (livingEntity instanceof EntityHorse) {
            // TODO: Config
            if (/*ModConfig.getInstance().pitchFade.enabled && */livingEntity.ridingEntity==Minecraft.getMinecraft().thePlayer) {
                opacity = getOpacity();
                HorseBuffInit.LOGGER.info("player is riding horse");
            }
            // getHorseType() Returns the horse type. 0 = Normal, 1 = Donkey, 2 = Mule, 3 = Undead Horse, 4 = Skeleton Horse
            //   only normal horses get to be jeb
            if (((EntityHorse)livingEntity).getHorseType() == 0 && isJeb(livingEntity)) {
                float hueOffset = (livingEntity.getUniqueID().hashCode()%5000)/5000f + (System.currentTimeMillis()%5000)/5000f;
                Color color = new Color(Color.HSBtoRGB(hueOffset, 0.8f, 1));
                r = color.getRed()/255f;
                g = color.getGreen()/255f;
                b = color.getBlue()/255f;
                HorseBuffInit.LOGGER.info("horse is jeb");
            }
        }
    }

    // TODO: figure out render layers
    /*
    @Shadow protected abstract RenderLayer getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline);
    @Redirect(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
    at = @At(value = "INVOKE", target = "net/minecraft/client/render/entity/LivingEntityRenderer.getRenderLayer (Lnet/minecraft/entity/LivingEntity;ZZZ)Lnet/minecraft/client/render/RenderLayer;"))
    RenderLayer makeRenderLayerTranslucent(LivingEntityRenderer<T, ? extends EntityModel<T>> instance, T entity, boolean showBody, boolean translucent, boolean showOutline) {
        if (entity instanceof HorseBaseEntity) {
            return RenderLayer.getItemEntityTranslucentCull(instance.getTexture(entity));
        }
        return getRenderLayer(entity, showBody, translucent, showOutline);
    }
     */

    // GlStateManager.color(r, g, b, a);
    @Inject(method = "renderModel",
            at = @At(value = "INVOKE", target = "net/minecraft/client/model/ModelBase.render (Lnet/minecraft/entity/Entity;FFFFFF)V"))
    void setOpacityAndChromaForRender(T livingEntity, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float scaleFactor, CallbackInfo ci){
        GlStateManager.color(r, g, b, opacity);
        HorseBuffInit.LOGGER.info("modifying horse");
    }
}

/*
package net.F53.HorseBuff.mixin.Client;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.HorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.awt.*;

import static net.F53.HorseBuff.HorseBuffInit.*;

@Mixin(LivingEntityRenderer.class)
public abstract class HorseRenderer<T extends LivingEntity, M extends EntityModel<T>> {


    @ModifyArgs(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
    at = @At(value = "INVOKE", target = "net/minecraft/client/render/entity/model/EntityModel.render (Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    void setOpacityAndChromaForRender(Args args){
        args.set(4, r);
        args.set(5, g);
        args.set(6, b);

        args.set(7, opacity);
    }
}
 */