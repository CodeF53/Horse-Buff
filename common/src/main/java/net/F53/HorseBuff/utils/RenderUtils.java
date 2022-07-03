package net.F53.HorseBuff.utils;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;

public class RenderUtils {
    public static boolean isJeb(Entity horse){
        return ModConfig.getInstance().jeb_Horses && horse.hasCustomName() && "jeb_".equals(horse.getName().getContents());
    }

    public static float getOpacity(LocalPlayer player){
        if (Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
            int fadeStartAngle = ModConfig.getInstance().pitchFade.startAngle;
            int fadeEndAngle = ModConfig.getInstance().pitchFade.endAngle;
            int minOpacity = 100 - ModConfig.getInstance().pitchFade.maxTransparency;
            float rate = (100f - minOpacity) / (fadeStartAngle - fadeEndAngle);

            // ItemEntityTranslucentCull rendering is stupid, it stops rendering when transparency <= 10
            minOpacity += 10;

            return (Math.max(Math.min(100, rate * (player.xBob - fadeEndAngle)), minOpacity)) / 100f;
        }
        return 1;
    }
}
