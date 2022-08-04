package net.oceanic.ancientsorcery;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;


public class OceanicSorceryClientMod implements ClientModInitializer {

        // The KeyBinding declaration and registration are commonly executed here statically

        private static KeyBinding keyBindingGUI;
        @Override
        public void onInitializeClient() {
            keyBindingGUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "Open GUI", // The translation key of the keybinding's name
                    InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                    GLFW.GLFW_KEY_O, // The keycode of the key
                    "Oceanic Client Utils" // The translation key of the keybinding's category.
            ));
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                while (keyBindingGUI.wasPressed()) {
                    ItemImbuementInfo.setImbuementPercentage(client.player.getMainHandStack(),ItemImbuementInfo.getImbuementPercentage(client.player.getMainHandStack())+1.0f);
                    ItemImbuementInfo.setImbuementType(client.player.getMainHandStack(), "gravity");
                }
            });
            List<Item> modifiedItems = new ArrayList<Item>();
            List<Item> registeredItems = Registry.ITEM.stream().toList();
            modifiedItems.addAll(registeredItems);
            modifiedItems.removeIf(item -> !ItemImbuementInfo.canImbue(item));
            for (Item item: modifiedItems){
                ColorProviderRegistry.ITEM.register((stack, tintIndex) -> ItemImbuementInfo.getColor(stack), item);
            }
        }
    }
