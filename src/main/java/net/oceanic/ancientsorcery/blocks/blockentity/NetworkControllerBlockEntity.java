package net.oceanic.ancientsorcery.blocks.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.oceanic.ancientsorcery.OceanicSorceryMod;
import net.oceanic.ancientsorcery.blocks.BlockInit;
import net.oceanic.ancientsorcery.blocks.ElementalNetworkControllerBlock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NetworkControllerBlockEntity extends BlockEntity {
    public Set<ElementalNetworkControllerBlock.BlockEntityInfo> routableBEs = new HashSet<>();
    public boolean loaded = false;
    public boolean shouldUpdate = false;
    public int id = 0;
    public NetworkControllerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockInit.NETWORK_CONTROLLER_BE, pos, state);
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        id = nbt.getInt("uniqueid");
        super.readNbt(nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("uniqueid", id);
        super.writeNbt(nbt);
    }

    public static void tick(World world, BlockPos pos, BlockState state, NetworkControllerBlockEntity be) {
        if (!be.loaded){
            be.loaded=true;
            if (!world.isClient()) {
                if (world != null && pos != null) {
                    if (state.getBlock() instanceof ElementalNetworkControllerBlock) {
                        ElementalNetworkControllerBlock block = (ElementalNetworkControllerBlock) state.getBlock();
                        Set<ElementalNetworkControllerBlock.BlockEntityInfo> routables = block.findRoutableBEs(world, pos);
                        be.routableBEs = routables;
                        be.loaded=true;
                    } else {
                        be.routableBEs = new HashSet<>();
                    }
                }
            }
        }
        if (be.shouldUpdate){
            be.shouldUpdate=false;
            if (!world.isClient()) {
                if (world != null && pos != null) {
                    if (state.getBlock() instanceof ElementalNetworkControllerBlock) {
                        ElementalNetworkControllerBlock block = (ElementalNetworkControllerBlock) state.getBlock();
                        Set<ElementalNetworkControllerBlock.BlockEntityInfo> routables = block.findRoutableBEs(world, pos);
                        be.routableBEs = routables;
                        be.loaded=true;
                    }
                }
            }
        }
    }
}