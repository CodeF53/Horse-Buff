package net.F53.HorseBuff.mixin.PortalHorse;

import com.llamalad7.mixinextras.sugar.Local;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

import static net.F53.HorseBuff.utils.TeleportHandler.tpAndRemount;

@Mixin(Entity.class)
public abstract class TickNether implements Nameable, EntityLike, CommandOutput {
    @Shadow protected abstract void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition);

    @Redirect(method = "tickPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;isNetherAllowed()Z"))
    private boolean tickNether(MinecraftServer minecraftServer, @Local(ordinal = 1) ServerWorld destination) {
        // ensure we don't do any teleportation when nether isn't even allowed
        if (!minecraftServer.isNetherAllowed())
            return false;

        // ensure horse
        if (!((Object) this instanceof AbstractHorseEntity vehicle))
            return true;

        // ensure Patch is enabled and player is controlling
        if (!(ModConfig.getInstance().portalPatch
            && vehicle.hasControllingPassenger()
            && vehicle.getControllingPassenger() instanceof PlayerEntity player))
            return true;

        // manually tick player portal time (it cant go up naturally due to being mounted)
        // when player could go, go
        if (player.netherPortalTime++ >= player.getMaxNetherPortalTime()) {
            // reset portal statuses, so we don't immediately teleport back
            player.inNetherPortal = false;
            vehicle.inNetherPortal = false;
            tpAndRemount(vehicle, destination, false);
        }

        // prevent vanilla handling teleportation
        return false;
    }
}
