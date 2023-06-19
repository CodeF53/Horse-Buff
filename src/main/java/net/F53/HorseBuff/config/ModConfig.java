package net.F53.HorseBuff.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.F53.HorseBuff.ModInfo;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.*;


@Config(name = ModInfo.MODID)
public class ModConfig implements ConfigData{
    // Config Structure
    //    Saddled Horse Don't Wander
    //    Breeding Changes
    @ConfigEntry.Category("Server")
    @ConfigEntry.Gui.Tooltip
    public boolean noWander = true;

    @ConfigEntry.Category("Server")
    @ConfigEntry.Gui.Tooltip
    public boolean fairBreeds = true;

    @ConfigEntry.Category("Server")
    @ConfigEntry.Gui.Tooltip
    public boolean portalPatch = true;

    @ConfigEntry.Category("Server")
    @ConfigEntry.Gui.Tooltip
    public boolean rubberBand = true;

    @ConfigEntry.Category("Server")
    @ConfigEntry.Gui.Tooltip
    public boolean breakSpeed = true;

    @ConfigEntry.Category("Server")
    @ConfigEntry.Gui.Tooltip
    public boolean stepHeight = false;

    @ConfigEntry.Category("Server")
    @ConfigEntry.Gui.Tooltip
    public boolean noBuck = true;

    @ConfigEntry.Category("Server")
    @ConfigEntry.Gui.Tooltip
    public boolean swim = true;

    @ConfigEntry.Category("Client")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    public FadeConfig pitchFade = new FadeConfig();

    public static class FadeConfig {
        public boolean enabled = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 90)
        public int startAngle = 30;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 0, max = 90)
        public int endAngle = 50;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(min = 50, max = 100)
        public int maxTransparency = 90;
    }

    @ConfigEntry.Category("Client")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 45)
    public int horseHeadAngleOffset = 0;

    @ConfigEntry.Category("Client")
    @ConfigEntry.Gui.Tooltip
    public boolean jeb_Horses = true;

    public static void init() {
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
    }

    public static ModConfig getInstance() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
