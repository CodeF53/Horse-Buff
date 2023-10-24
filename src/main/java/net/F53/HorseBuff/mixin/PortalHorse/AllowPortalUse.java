package net.F53.HorseBuff.mixin.PortalHorse;

import net.F53.HorseBuff.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractHorseEntity.class)
public abstract class AllowPortalUse extends LivingEntity {
    @Shadow public abstract @Nullable LivingEntity getControllingPassenger();

    // constructor to make compiler happy
    protected AllowPortalUse(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean canUsePortals() {
        if (!this.portalPatchApplies())
            return super.canUsePortals();

        return true;
    }

    @Override
    public void setInNetherPortal(BlockPos pos) {
        // make player inherit horse portal position when mounted
        if (this.portalPatchApplies())
            this.getControllingPassenger().setInNetherPortal(pos);

        super.setInNetherPortal(pos);
    }

    @Override
    public void resetPortalCooldown() {
        if (this.portalPatchApplies()) {
            // inherit portal cooldown of controlling passenger
            this.setPortalCooldown(this.getControllingPassenger().getDefaultPortalCooldown());
            return;
        }
        super.resetPortalCooldown();
    }

    private boolean portalPatchApplies() {
        return (ModConfig.getInstance().portalPatch
            && this.hasControllingPassenger()
            && this.getControllingPassenger() instanceof PlayerEntity);
    }
}
