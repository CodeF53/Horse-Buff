package net.F53.HorseBuff.mixin.PortalHorse;

import net.F53.HorseBuff.HorseBuffInit;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.minecraft.entity.damage.DamageSource.IN_WALL;

// Makes vehicles invulnerable to Suffocation and Fall damage while colliding with a nether portal.
@Mixin(LivingEntity.class)
public abstract class PortalChoke extends Entity {
    public PortalChoke(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At(value = "INVOKE", target = "net/minecraft/entity/LivingEntity.isInvulnerableTo (Lnet/minecraft/entity/damage/DamageSource;)Z"))
    public boolean damage(LivingEntity instance, DamageSource damageSource) {
        // if portalPatch is enabled
        if (ModConfig.getInstance().portalPatch) {
            // is in a portal
            if (inNetherPortal) {
                // is a vehicle being ridden by a player
                HorseBuffInit.LOGGER.info("inNether");
                if (instance.hasPassengers() && instance.getPassengerList().get(0) instanceof PlayerEntity) {
                    // is damage a source we want to cancel
                    // ie suffocation or falling
                    if (damageSource.isFromFalling() || damageSource == IN_WALL) {
                        HorseBuffInit.LOGGER.info("marked invulnerable to - " + damageSource.getName());
                        return true;
                    }
                }
            }
        }
        HorseBuffInit.LOGGER.info("not marked invulnerable to - " + damageSource.getName());

        return instance.isInvulnerableTo(damageSource);
    }
}
