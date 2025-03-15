package net.F53.HorseBuff.utils;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class KeyInput {
    private boolean wasKeyPressed = false;
    public boolean isKeyReleased(long window, KeyBinding key) {
        if(GLFW.glfwGetKey(window, KeyBindingHelper.getBoundKeyOf(key).getCode()) == 1) {
            wasKeyPressed = true;
        } else {
            if(wasKeyPressed) {
                wasKeyPressed = false;
                return true;
            }
        }
        return false;
    }
}
