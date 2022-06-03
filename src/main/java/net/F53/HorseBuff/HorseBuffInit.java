package net.f53.horsebuff;

import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Mod(modid = "horsebuff", name = "Horse Buff", version = "2.0.0")
public class HorseBuffInit {
	public static final Logger LOGGER = LogManager.getLogger("HorseBuff");

	@EventHandler
	public void init(FMLInitializationEvent event) {
		LOGGER.info("Horse Buff Initialized");
	}

	public static boolean isJeb(Entity horse){
		// TODO: config
		return /*ModConfig.getInstance().jeb_Horses &&*/ horse.hasCustomName() && "jeb_".equals(horse.getName());
	}

	public static float getOpacity(){
		LOGGER.info("TPP Value = " + Minecraft.getMinecraft().gameSettings.thirdPersonView);
		// TODO
		// TODO: config
		/*if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
			int fadeStartAngle = ModConfig.getInstance().pitchFade.startAngle;
			int fadeEndAngle = ModConfig.getInstance().pitchFade.endAngle;
			int minOpacity = 100 - ModConfig.getInstance().pitchFade.maxTransparency;
			float rate = (100f - minOpacity) / (fadeStartAngle - fadeEndAngle);

			// ItemEntityTranslucentCull rendering is stupid, it stops rendering when transparency <= 10
			minOpacity += 10;

			return (Math.max(Math.min(100, rate * (player.renderPitch - fadeEndAngle)), minOpacity)) / 100f;
		}*/
		return 0.5F;
	}
}

/*
package net.F53.HorseBuff;

import net.fabricmc.api.ModInitializer;

import net.F53.HorseBuff.config.ModConfig;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
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
			toRun.addAll(runNextTick);
			runNextTick.clear();
			while (toRun.size()>0){
				toRun.get(0).run();
				toRun.remove(0);
			}
		});
	}

	// Schedules player to be teleported to and mounted on its vehicle
	public static void tpAndRemount(UUID playerUUID, UUID vehicleUUID, ServerWorld destination, int depth) {
		runNextTick.add(() -> {
			PlayerEntity player = destination.getPlayerByUuid(playerUUID);
			Entity vehicle = destination.getEntity(vehicleUUID);

			// wait until both the player and vehicle are actually in the dimension
			if (vehicle == null || player == null){
				// notify user that something is going really wrong every full second we wait for arrivals
				if (depth % 20 == 0 && depth != 0) {
					String missingEntities = vehicle == player ? "the horse and the player" : vehicle == null? "the horse" : "the player";
					LOGGER.error("Something likely went wrong, HorseBuff has been waiting for " + depth/20 +
							" seconds for " + missingEntities + " to arrive in the dimension: " + destination);
				}

				runNextTick.add(() -> tpAndRemount(playerUUID, vehicleUUID, destination, depth+1));
			} else {
				player.unsetRemoved();
				vehicle.unsetRemoved();

				player.setPosition(vehicle.getPos());
				player.startRiding(vehicle, true);
			}
		});
	}
}
 */