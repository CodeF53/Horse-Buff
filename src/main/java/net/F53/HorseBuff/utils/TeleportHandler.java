package net.F53.HorseBuff.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.server.world.ServerWorld;

import static net.F53.HorseBuff.HorseBuffInit.LOGGER;

public class TeleportHandler {
    public static void tpAndRemount(AbstractHorseEntity vehicle, ServerWorld destination, boolean isEnd) {
        // Get player
        Entity player = vehicle.getControllingPassenger();
        assert player != null;

        // Change player Dim
        player.resetPortalCooldown();
        player = player.moveToWorld(destination);

        // Change vehicle Dim
        vehicle.resetPortalCooldown();
        vehicle = (AbstractHorseEntity) vehicle.moveToWorld(destination);

        // Remount
        if (vehicle != null && player != null) {
            AbstractHorseEntity finalVehicle = vehicle;
            Entity finalPlayer = player;
            TickScheduler.runNextTick.add(() -> {
                if (isEnd)
                    finalPlayer.teleport(finalVehicle.getX(), finalVehicle.getY(), finalVehicle.getZ());
                finalPlayer.startRiding(finalVehicle, true);
            });
        } else {
            LOGGER.error("Something likely went wrong, game likely failed to generate a teleport target");
        }
    }
}
