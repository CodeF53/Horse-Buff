package net.F53.HorseBuff.utils;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;

public class RenderUtils {
    public static boolean isJeb(Entity horse){
        return ModConfig.getInstance().jeb_Horses && horse.hasCustomName() && "jeb_".equals(horse.getName().getString());
    }

    public static float getOpacity(ClientPlayerEntity player){
        if (MinecraftClient.getInstance().options.getPerspective().isFirstPerson()) {
            int fadeStartAngle = ModConfig.getInstance().pitchFade.startAngle;
            int fadeEndAngle = ModConfig.getInstance().pitchFade.endAngle;
            int minOpacity = 100 - ModConfig.getInstance().pitchFade.maxTransparency;
            float rate = (100f - minOpacity) / (fadeStartAngle - fadeEndAngle);

            // ItemEntityTranslucentCull rendering is stupid, it stops rendering when transparency <= 10
            minOpacity += 10;

            return (Math.max(Math.min(100, rate * (player.renderPitch - fadeEndAngle)), minOpacity)) / 100f;
        }
        return 1;
    }
}
