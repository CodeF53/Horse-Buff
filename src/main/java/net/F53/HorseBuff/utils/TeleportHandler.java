package net.F53.HorseBuff.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;

import static net.F53.HorseBuff.HorseBuffInit.LOGGER;
import static net.F53.HorseBuff.utils.TickScheduler.runNextTick;

public class TeleportHandler {
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
