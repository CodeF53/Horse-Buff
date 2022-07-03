package net.F53.HorseBuff.utils;

import net.F53.HorseBuff.utils.TickScheduler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class TickSchedulerInitializer {
    public static void initialize() {
        TickScheduler.initialize();

        ServerTickEvents.END_SERVER_TICK.register((server)-> TickScheduler.endServerTick());
    }
}
