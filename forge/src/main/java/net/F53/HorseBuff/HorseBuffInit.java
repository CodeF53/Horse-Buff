package net.F53.HorseBuff;

import net.F53.HorseBuff.Server.BreakSpeed;
import net.F53.HorseBuff.utils.TickSchedulerInitializer;
import me.shedaniel.autoconfig.AutoConfig;
import net.F53.HorseBuff.config.ModConfig;

import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("horsebuff")
public class HorseBuffInit {
	public static final Logger LOGGER = LogManager.getLogger("net/F53/HorseBuff");
	public HorseBuffInit() {
		MinecraftForge.EVENT_BUS.register(BreakSpeed.class);
		TickSchedulerInitializer.initialize();
		ModConfig.init();

		// bind the Config button in the forge mod menu to our config screen
		ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () ->
				new ConfigGuiHandler.ConfigGuiFactory((client, parent) ->
						AutoConfig.getConfigScreen(ModConfig.class, parent).get()));
	}
}
