package net.F53.HorseBuff.utils;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public class RenderUtils {
    public static boolean isJeb(Entity entity) {
        return ModConfig.getInstance().jeb_Horses && entity.hasCustomName() && "jeb_".equals(entity.getName().getString());
    }

    public static int getAlpha(Entity horse) {
        ModConfig.FadeConfig pitchFadeConfig = ModConfig.getInstance().pitchFade;
        if (!pitchFadeConfig.enabled) return 255;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || !client.options.getPerspective().isFirstPerson() || !horse.hasPassenger(client.player))
            return 255;

        int minAlpha = 255 - Math.round(pitchFadeConfig.maxTransparency * 2.25f);
        int rate = (255 - minAlpha) / (pitchFadeConfig.startAngle - pitchFadeConfig.endAngle);
        int unclampedAlpha = Math.round(rate * (client.player.renderPitch - pitchFadeConfig.endAngle));

        return Math.min(Math.max(unclampedAlpha, minAlpha), 255);
    }
}
