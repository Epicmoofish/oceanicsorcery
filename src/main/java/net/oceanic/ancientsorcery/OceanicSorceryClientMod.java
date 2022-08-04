package net.oceanic.ancientsorcery;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.oceanic.ancientsorcery.blocks.ElementalNetworkControllerBlock;
import net.oceanic.ancientsorcery.blocks.blockentity.NetworkControllerBlockEntity;
import net.oceanic.ancientsorcery.packet.PacketInfo;
import net.oceanic.ancientsorcery.packet.SorceryPacketByteBuf;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
            ClientPlayNetworking.registerGlobalReceiver(PacketInfo.CLIENTBOUND_CONTROLLER_ID, (client, handler, buf, responseSender) -> {
                Set<ElementalNetworkControllerBlock.BlockEntityTransfer> transferSet = SorceryPacketByteBuf.readSetBlockEntityTransfer(buf);
                BlockPos pos = buf.readBlockPos();
                client.execute(() -> {
                    // Everything in this lambda is run on the render thread
                    World world = client.world;
                    if (world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof NetworkControllerBlockEntity){
                        NetworkControllerBlockEntity networkBE = (NetworkControllerBlockEntity)world.getBlockEntity(pos);
                        Set<ElementalNetworkControllerBlock.BlockEntityInfo> routable = new HashSet<>();
                        for (ElementalNetworkControllerBlock.BlockEntityTransfer transfer: transferSet){
                            routable.add(transfer.getInfo());
                        }
                        networkBE.routableBEs = routable;
                        networkBE.routableTransfers = transferSet;
                    }
                });
            });
        }
    }
