package HorseBuff.utils.forge;

import net.F53.HorseBuff.utils.TickScheduler;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public class TickSchedulerImpl {
    public static void initialize() {
        TickScheduler.toRun = new ArrayList<>();
        TickScheduler.runNextTick = new ArrayList<>();
    }

    @SubscribeEvent
    public void onTick(ServerTickEvent event){
        if (event.phase == TickEvent.Phase.END) {
            TickScheduler.toRun.addAll(TickScheduler.runNextTick);
            TickScheduler.runNextTick.clear();
            while (TickScheduler.toRun.size()>0){
                TickScheduler.toRun.get(0).run();
                TickScheduler.toRun.remove(0);
            }
        }
    }
}
