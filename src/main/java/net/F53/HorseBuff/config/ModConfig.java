package net.F53.HorseBuff.config;

import net.F53.HorseBuff.ModInfo;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.*;


@Config(name = ModInfo.MODID)
public class ModConfig implements ConfigData{
    // Config Structure
    //    Saddled Horse Don't Wander
    //    Breeding Changes

    @ConfigEntry.Gui.Tooltip
    public static boolean noWander = true;

    @ConfigEntry.Gui.Tooltip
    public static boolean fairBreeds = true;
}
