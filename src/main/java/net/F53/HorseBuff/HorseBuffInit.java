package net.F53.HorseBuff;

import net.fabricmc.api.ModInitializer;

import net.F53.HorseBuff.config.ModConfig;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class HorseBuffInit implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("HorseBuff");
	public static ArrayList<Runnable> runNextTick;
	public static ArrayList<Runnable> toRun;

	@Override
	public void onInitialize() {
		LOGGER.info("Horse Buff Initialized");
		ModConfig.init();

		// this makes it super simple to run code next tick, just add a lambda runnable to the end of runNextTick
		toRun = new ArrayList<>();
		runNextTick = new ArrayList<>();
		ServerTickEvents.END_SERVER_TICK.register((server)->{
			while (toRun.size()>0){
				toRun.get(0).run();
				toRun.remove(0);
			}
			toRun.addAll(runNextTick);
			runNextTick.clear();
		});
	}

	// Schedules player to be teleported to and mounted on a horse
	public static void tpAndRemount(Entity player, Entity horse) {
		runNextTick.add(() -> {
			player.unsetRemoved();
			player.setPosition(horse.getPos());
			player.detach();
			horse.detach();

			runNextTick.add(() -> {
				player.startRiding(horse, true);
			});
		});
	}
}
