package net.F53.HorseBuff.mixin.Client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.tutorial.Tutorial;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class InventoryAccessor {
    @Shadow @Final private Tutorial tutorialManager;

    @Shadow public abstract void setScreen(@Nullable Screen screen);

    @Shadow @Nullable public LocalPlayer player;

    @Redirect(method= "handleInputEvents()V", at = @At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerEntity.openRidingInventory ()V"))
    void playerInventoryAccess(LocalPlayer instance){
        assert this.player != null;
        if (Minecraft.getInstance().options.keySprint.isDown()) {
            tutorialManager.onOpenInventory();
            setScreen(new InventoryScreen(this.player));
        }
        else {
            instance.sendOpenInventory();
        }
    }
}
