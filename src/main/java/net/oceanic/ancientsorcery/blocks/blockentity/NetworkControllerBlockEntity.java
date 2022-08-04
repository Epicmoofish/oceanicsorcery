package net.oceanic.ancientsorcery.blocks.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.oceanic.ancientsorcery.blocks.BlockInit;
import net.oceanic.ancientsorcery.blocks.ElementalNetworkControllerBlock;
import net.oceanic.ancientsorcery.controller.NetworkControllerFiles;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkControllerBlockEntity extends BlockEntity {
    public Set<ElementalNetworkControllerBlock.BlockEntityInfo> routableBEs = new HashSet<>();
    public Set<ElementalNetworkControllerBlock.BlockEntityTransfer> routableTransfers = new HashSet<>();
    public boolean loaded = false;
    public boolean shouldUpdate = false;
    public UUID uuid;
    public NetworkControllerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockInit.NETWORK_CONTROLLER_BE, pos, state);
        uuid = UUID.randomUUID();
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        uuid = nbt.getUuid("UUID");
        super.readNbt(nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putUuid("UUID", uuid);
        super.writeNbt(nbt);
    }
///setblock ~ ~ ~ ancientsorcery:elemental_network_controller{UUID: [I; 444662511, 1131299623, -1842901606, 1169811965]}
    public static void tick(World world, BlockPos pos, BlockState state, NetworkControllerBlockEntity be) {
        if (!be.loaded){
            be.loaded=true;
            if (!world.isClient()) {
                world.blockEntityTickers.forEach(entityTicker -> {
                    if (entityTicker.getPos() != pos) {
                        if (world.getBlockEntity(entityTicker.getPos()) != null) {
                            if (world.getBlockEntity(entityTicker.getPos()) instanceof NetworkControllerBlockEntity) {
                                if (((NetworkControllerBlockEntity) world.getBlockEntity(entityTicker.getPos())).uuid.equals(be.uuid)) {
                                    be.uuid = UUID.randomUUID();
                                }
                            }
                        }
                    }
                });
                NetworkControllerFiles.get(world).createNewRegionFile(be.uuid);
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