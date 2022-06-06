package HorseBuff;

import me.shedaniel.autoconfig.ConfigData;

import net.F53.HorseBuff.config.ModConfig;
import net.F53.HorseBuff.utils.TickScheduler;

import net.minecraftforge.fml.common.Mod;

@Mod("horsebuff")
public class HorseBuffInit {
	public HorseBuffInit() {
		ModConfig.init();
		TickScheduler.initialize();
	}
}
