package net.F53.HorseBuff.mixin.Server;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

// Lower wander speed for saddled horses
@SuppressWarnings("unused")
@Mixin(AbstractHorse.class)
public abstract class NoWander extends Mob {
    protected NoWander(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow public abstract boolean isSaddled();

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/animal/Animal.travel (Lnet/minecraft/world/phys/Vec3;)V"))
    private Vec3 disableWandering(Vec3 input) {
        if (ModConfig.getInstance().noWander && isSaddled() && this.getLeashHolder() == null)
            return(Vec3.ZERO);
        return input;
    }
}
