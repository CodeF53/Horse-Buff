package net.f53.horsebuff.mixin.Server;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EntityHorse.class)
public abstract class NoWander extends EntityAnimal {
    public NoWander(World worldIn) {
        super(worldIn);
    }

    @Shadow public abstract boolean isHorseSaddled();

    @ModifyArgs(method = "moveEntityWithHeading", at = @At(value = "INVOKE", target = "net/minecraft/entity/passive/EntityAnimal.moveEntityWithHeading (FF)V", ordinal = 1))
    private void lowerWanderSpeed(Args args) {
        // TODO: Config
        if (/*ModConfig.getInstance().noWander && */this.isHorseSaddled()) {
            args.set(0, 0F);
            args.set(1, 0F);
        }
    }
}
