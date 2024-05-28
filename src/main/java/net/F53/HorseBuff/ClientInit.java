package net.F53.HorseBuff;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ClientInit implements ClientModInitializer {
    public static KeyBinding horsePlayerInventory = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "text.HorseBuff.keybinding.horsePlayerInventory",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            "text.HorseBuff.keybinding.category"
    ));

    @Override
    public void onInitializeClient() {}
}
