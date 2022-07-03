package net.F53.HorseBuff.utils;

import java.util.ArrayList;


public class TickScheduler {
    public static ArrayList<Runnable> runNextTick;
    public static ArrayList<Runnable> toRun;

    public static void initialize() {
        TickScheduler.toRun = new ArrayList<>();
        TickScheduler.runNextTick = new ArrayList<>();
    }

    public static void endServerTick(){
        toRun.addAll(TickScheduler.runNextTick);
        runNextTick.clear();
        while (toRun.size()>0){
            toRun.get(0).run();
            toRun.remove(0);
        }
    }
}