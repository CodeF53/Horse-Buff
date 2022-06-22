package net.F53.HorseBuff.utils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TickSchedulerInitializer {
    public static void initialize() {
        TickScheduler.initialize();
        MinecraftForge.EVENT_BUS.register(TickSchedulerInitializer.class);
    }

    @SubscribeEvent
    public static void endServerTick(TickEvent.ServerTickEvent event){
        if (event.phase == TickEvent.Phase.END) {
            TickScheduler.endServerTick();
        }
    }
}
