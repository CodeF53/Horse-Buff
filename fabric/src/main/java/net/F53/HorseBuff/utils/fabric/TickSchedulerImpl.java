package net.F53.HorseBuff.utils.fabric;

import net.F53.HorseBuff.utils.TickScheduler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.ArrayList;

public class TickSchedulerImpl {
    public static void initialize() {
        TickScheduler.toRun = new ArrayList<>();
        TickScheduler.runNextTick = new ArrayList<>();

        ServerTickEvents.END_SERVER_TICK.register((server)->{
            TickScheduler.toRun.addAll(TickScheduler.runNextTick);
            TickScheduler.runNextTick.clear();
            while (TickScheduler.toRun.size()>0){
                TickScheduler.toRun.get(0).run();
                TickScheduler.toRun.remove(0);
            }
        });
    }
}
