package net.F53.HorseBuff.Server;

import net.F53.HorseBuff.config.ModConfig;

import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BreakSpeed {
    @SubscribeEvent
    public static void blockBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getPlayer();
        // cancels out breakspeed debuff given while riding horses
        if (player.isPassenger() && player.getVehicle() instanceof AbstractHorse && ModConfig.getInstance().breakSpeed) {
            event.setNewSpeed(event.getOriginalSpeed() * 5.0F);
        }
    }
}
