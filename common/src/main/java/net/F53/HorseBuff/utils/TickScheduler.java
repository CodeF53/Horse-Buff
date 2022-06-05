package net.F53.HorseBuff.utils;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.ArrayList;

// TODO: Will need a forge copy of this code that switches ServerTickEvents for whatever equivalent that it has
public class TickScheduler {
    public static ArrayList<Runnable> runNextTick;
    public static ArrayList<Runnable> toRun;

    public static void initialize() {
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
}