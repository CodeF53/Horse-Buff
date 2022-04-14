package net.F53.HorseBuff;

import net.fabricmc.api.ModInitializer;

import net.F53.HorseBuff.config.ModConfig;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.UUID;

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

	// Schedules player to be teleported to and mounted on its vehicle
	public static void tpAndRemount(UUID playerUUID, UUID VehicleUUID, ServerWorld destination) {
		runNextTick.add(() -> {
			PlayerEntity player = destination.getPlayerByUuid(playerUUID);
			Entity vehicle = destination.getEntity(VehicleUUID);
			assert player != null;
			assert vehicle != null;
			player.unsetRemoved();
			vehicle.unsetRemoved();

			player.setPosition(vehicle.getPos());

			player.startRiding(vehicle, true);
		});
	}
}
