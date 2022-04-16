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

    @ConfigEntry.Gui.Tooltip
    public boolean noWander = true;

    @ConfigEntry.Gui.Tooltip
    public boolean fairBreeds = true;

    @ConfigEntry.Gui.Tooltip
    public boolean portalPatch = true;

    @ConfigEntry.Gui.Tooltip
    public boolean pitchFade = true;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 45)
    public int horseHeadAngleOffset = 0;

    public boolean jeb_Horses = true;

    public static void init() {
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
    }

    public static ModConfig getInstance() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
