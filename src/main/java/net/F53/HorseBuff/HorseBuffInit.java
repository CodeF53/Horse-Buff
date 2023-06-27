package net.F53.HorseBuff;

import net.F53.HorseBuff.utils.TickScheduler;
import net.fabricmc.api.ModInitializer;

import net.F53.HorseBuff.config.ModConfig;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.UUID;

public class HorseBuffInit implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("HorseBuff");

	@Override
	public void onInitialize() {
		LOGGER.info("Horse Buff Initialized");
		ModConfig.init();
		TickScheduler.initialize();
	}
}
