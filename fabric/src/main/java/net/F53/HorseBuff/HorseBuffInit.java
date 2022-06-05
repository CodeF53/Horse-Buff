package net.F53.HorseBuff;

import net.fabricmc.api.ModInitializer;

import net.F53.HorseBuff.config.ModConfig;
import net.F53.HorseBuff.utils.TickScheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HorseBuffInit implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("HorseBuff");

	@Override
	public void onInitialize() {
		LOGGER.info("Horse Buff Initialized");
		ModConfig.init();
		TickScheduler.initialize();
	}
}
