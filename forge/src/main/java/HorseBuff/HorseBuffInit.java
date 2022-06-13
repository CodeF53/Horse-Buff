package HorseBuff;

import HorseBuff.Server.BreakSpeed;
import HorseBuff.utils.TickSchedulerInitializer;
import me.shedaniel.autoconfig.AutoConfig;
import net.F53.HorseBuff.config.ModConfig;

import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("horsebuff")
public class HorseBuffInit {
	public static final Logger LOGGER = LogManager.getLogger("HorseBuff");
	public HorseBuffInit() {
		MinecraftForge.EVENT_BUS.register(BreakSpeed.class);
		TickSchedulerInitializer.initialize();
		ModConfig.init();
	}
}
