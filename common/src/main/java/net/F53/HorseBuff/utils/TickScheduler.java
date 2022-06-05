package net.F53.HorseBuff.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.util.ArrayList;

// TODO: Will need a forge copy of this code that switches ServerTickEvents for whatever equivalent that it has
public class TickScheduler {
    public static ArrayList<Runnable> runNextTick;
    public static ArrayList<Runnable> toRun;

    @ExpectPlatform
    public static void initialize() {
        throw new AssertionError();
    }
}