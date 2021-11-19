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

    public static void init() {
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
    }

    public static ModConfig getInstance() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
