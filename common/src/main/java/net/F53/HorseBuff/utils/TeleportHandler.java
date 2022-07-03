package net.F53.HorseBuff.utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;

import java.util.UUID;

public class TeleportHandler {
    // Schedules player to be teleported to and mounted on its vehicle
    public static void tpAndRemount(UUID playerUUID, UUID vehicleUUID, ServerLevel destination, int depth) {
        TickScheduler.runNextTick.add(() -> {
            Player player = destination.getPlayerByUUID(playerUUID);
            Entity vehicle = destination.getEntity(vehicleUUID);

            // wait until both the player and vehicle are actually in the dimension
            if (vehicle == null || player == null){
                // notify user that something is going really wrong every full second we wait for arrivals
                if (depth % 20 == 0 && depth != 0) {
                    String missingEntities = vehicle == player ? "the horse and the player" : vehicle == null? "the horse" : "the player";
                    LogManager.getLogger("HorseBuff").error("Something likely went wrong, HorseBuff has been waiting for " + depth/20 +
                            " seconds for " + missingEntities + " to arrive in the dimension: " + destination);
                }

                TickScheduler.runNextTick.add(() -> tpAndRemount(playerUUID, vehicleUUID, destination, depth+1));
            } else {
                player.unsetRemoved();
                vehicle.unsetRemoved();

                player.setPos(vehicle.position());
                player.startRiding(vehicle, true);
            }
        });
    }
}
