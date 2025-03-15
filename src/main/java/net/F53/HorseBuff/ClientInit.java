package net.F53.HorseBuff;

import net.F53.HorseBuff.utils.KeyInput;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.passive.AbstractHorseEntity;
import org.lwjgl.glfw.GLFW;

public class ClientInit implements ClientModInitializer {

    private boolean isInventoryOpened = false;
    private KeyInput keyInput = new KeyInput();

    public static KeyBinding horsePlayerInventory = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "text.HorseBuff.keybinding.horsePlayerInventory",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            "text.HorseBuff.keybinding.category"
    ));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            openPlayerInventory(client);
        });
    }

    private void openPlayerInventory(MinecraftClient client) {
        if(client.player == null) {
            return;
        }
        if(!(client.player.getVehicle() instanceof AbstractHorseEntity)) {
            return;
        }
        if(client.currentScreen == null) {
            isInventoryOpened = false;
        }
        if(keyInput.isKeyReleased(client.getWindow().getHandle(), horsePlayerInventory)) {
            if(!isInventoryOpened) {
                if(client.currentScreen == null) {
                    client.setScreen(new InventoryScreen(client.player));
                    isInventoryOpened = true;
                }
            } else {
                client.setScreen(null);
                isInventoryOpened = false;
            }
        }
    }
}