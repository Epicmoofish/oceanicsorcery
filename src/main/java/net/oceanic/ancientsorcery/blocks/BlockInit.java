package net.oceanic.ancientsorcery.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.oceanic.ancientsorcery.OceanicSorceryMod;
import net.oceanic.ancientsorcery.blocks.blockentity.NetworkControllerBlockEntity;

public class BlockInit {
    public static final Block TIER1_PIPE_BLOCK = new ElementalPipeBlock(FabricBlockSettings.of(Material.METAL).strength(1.0f),1, 0.2f);
    public static final Block TIER2_PIPE_BLOCK = new ElementalPipeBlock(FabricBlockSettings.of(Material.METAL).strength(1.0f),2, 1);
    public static final Block CONTROLLER_BLOCK = new ElementalNetworkControllerBlock(FabricBlockSettings.of(Material.METAL).strength(1.0f));

    public static BlockEntityType<NetworkControllerBlockEntity> NETWORK_CONTROLLER_BE;
    public static void init(){
        Registry.register(Registry.BLOCK, new Identifier(OceanicSorceryMod.MODID, "basic_pipe_block"), TIER1_PIPE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(OceanicSorceryMod.MODID, "basic_pipe_block"), new BlockItem(TIER1_PIPE_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
        Registry.register(Registry.BLOCK, new Identifier(OceanicSorceryMod.MODID, "gravity_pipe_block"), TIER2_PIPE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(OceanicSorceryMod.MODID, "gravity_pipe_block"), new BlockItem(TIER2_PIPE_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
        Registry.register(Registry.BLOCK, new Identifier(OceanicSorceryMod.MODID, "elemental_network_controller"), CONTROLLER_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(OceanicSorceryMod.MODID, "elemental_network_controller"), new BlockItem(CONTROLLER_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
        NETWORK_CONTROLLER_BE = Registry.register(Registry.BLOCK_ENTITY_TYPE, OceanicSorceryMod.MODID+":network_controller_block_entity", FabricBlockEntityTypeBuilder.create(NetworkControllerBlockEntity::new, CONTROLLER_BLOCK).build(null));
    }
}
