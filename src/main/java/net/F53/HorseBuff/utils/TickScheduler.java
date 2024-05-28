package net.F53.HorseBuff.utils;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.ArrayList;

public class TickScheduler {
    public static ArrayList<Runnable> runNextTick;
    public static ArrayList<Runnable> toRun;

    public static void initialize() {
        toRun = new ArrayList<>();
        runNextTick = new ArrayList<>();
        ServerTickEvents.END_SERVER_TICK.register(server -> endServerTick());
    }

    public static void endServerTick() {
        toRun.addAll(runNextTick);
        runNextTick.clear();
        while (!toRun.isEmpty()) {
            toRun.get(0).run();
            toRun.remove(0);
        }
    }
}
