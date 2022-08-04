package net.oceanic.ancientsorcery.blocks;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.oceanic.ancientsorcery.OceanicSorceryMod;
import net.oceanic.ancientsorcery.blocks.blockentity.NetworkControllerBlockEntity;
import net.oceanic.ancientsorcery.controller.NetworkControllerFiles;
import net.oceanic.ancientsorcery.packet.PacketInfo;
import net.oceanic.ancientsorcery.packet.SorceryPacketByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class ElementalNetworkControllerBlock extends BlockWithEntity {
//    @Nullable
//    @Override
//    public Packet<ClientPlayPacketListener> toUpdatePacket() {
//        return BlockEntityUpdateS2CPacket.create(this);
//    }
//
//    @Override
//    public NbtCompound toInitialChunkDataNbt() {
//        return createNbt();
//    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            if (world.getBlockEntity(pos) instanceof NetworkControllerBlockEntity) {
                Set<BlockEntityTransfer> sendToClient = new HashSet<>();
                NetworkControllerBlockEntity blockEntity = (NetworkControllerBlockEntity) world.getBlockEntity(pos);
                PacketByteBuf buf = PacketByteBufs.create();
                for (BlockEntityInfo beinfo: blockEntity.routableBEs) {
                    sendToClient.add(new BlockEntityTransfer(beinfo, OceanicSorceryMod.TransferMode.NONE, OceanicSorceryMod.Spell.GRAVITY));
                }
                System.out.println(NetworkControllerFiles.get(world).readJson(blockEntity.uuid));
                buf = SorceryPacketByteBuf.writeSetBlockEntityTransfer(sendToClient, buf);
                buf.writeBlockPos(pos);
                ServerPlayNetworking.send((ServerPlayerEntity) player, PacketInfo.CLIENTBOUND_CONTROLLER_ID,buf);
            }
        }
        if (world.isClient()){
            if (world.getBlockEntity(pos) instanceof NetworkControllerBlockEntity) {
                NetworkControllerBlockEntity blockEntity = (NetworkControllerBlockEntity) world.getBlockEntity(pos);
//                System.out.println(blockEntity.routableBEs);
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
    public Set<BlockEntityInfo> findRoutableBEs(World world, BlockPos pos){
        if (world !=null && pos !=null) {
            Set<BlockPos> foundPipes = new HashSet<>();
            Set<BlockPos> currentPipes = findValidPipes(pos, foundPipes, world);
            while (currentPipes.size() > 0) {
                BlockPos position = currentPipes.iterator().next();
                foundPipes.add(position);
                currentPipes.addAll(findValidPipes(position, foundPipes, world));
                currentPipes.remove(position);
            }
            Set<BlockEntityInfo> validBEs = new HashSet<>();
            while (foundPipes.size() > 0) {
                BlockPos position = foundPipes.iterator().next();
                validBEs.addAll(findValidBEs(position, world,validBEs));
                foundPipes.remove(position);
            }
            return validBEs;
        }
        return new HashSet<>();
    }
    public Set<BlockEntityInfo> findValidBEs(BlockPos start, World world, Set<BlockEntityInfo> alreadyIn) {
        Set<BlockEntityInfo> currentSet = new HashSet<>();
        Block block = world.getBlockState(start).getBlock();
        if (block instanceof ElementalPipeBlock) {
            ElementalPipeBlock pipeBlock = (ElementalPipeBlock)block;
            int tier = pipeBlock.getTier();
            int transferRate = pipeBlock.getTransferRate();
            currentSet.add(new BlockEntityInfo(start.up(), tier, transferRate,start));
            currentSet.add(new BlockEntityInfo(start.down(), tier, transferRate,start));
            currentSet.add(new BlockEntityInfo(start.west(), tier, transferRate,start));
            currentSet.add(new BlockEntityInfo(start.east(), tier, transferRate,start));
            currentSet.add(new BlockEntityInfo(start.north(), tier, transferRate,start));
            currentSet.add(new BlockEntityInfo(start.south(), tier, transferRate,start));
            currentSet.removeIf(BEInfo -> world == null|| world.getBlockEntity(BEInfo.getPos()) == null || alreadyIn.contains(BEInfo));
        }
        return currentSet;
    }
    public Set<BlockPos> findValidPipes(BlockPos start, Set<BlockPos> alreadyFound, World world){
        Set<BlockPos> currentSet = new HashSet<>();
        currentSet.add(start.up());
        currentSet.add(start.down());
        currentSet.add(start.west());
        currentSet.add(start.east());
        currentSet.add(start.north());
        currentSet.add(start.south());
        currentSet.removeIf(pos -> {
            if (world!=null) {
                Block block = world.getBlockState(pos).getBlock();
                if (alreadyFound.contains(pos)) {
                    return true;
                }
                if (!(block instanceof ElementalPipeBlock)) {
                    return true;
                }
                ElementalPipeBlock pipeBlock = (ElementalPipeBlock) block;
                return !pipeBlock.validPipeToConnect(start, world);
            }
            return true;
        });
        return currentSet;
    }
    public ElementalNetworkControllerBlock(Settings settings) {
        super(settings);

    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new NetworkControllerBlockEntity(pos, state);
    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, BlockInit.NETWORK_CONTROLLER_BE, (world1, pos, state1, be) -> NetworkControllerBlockEntity.tick(world1, pos, state1, be));
    }

    public static class BlockEntityInfo {

        private final float transferRate;
        private final int tier;
        private final BlockPos pos;
        private final BlockPos pipePos;

        public BlockEntityInfo(BlockPos pos, int tier, float transferRate, BlockPos pipePos){
            this.pos=pos;
            this.tier=tier;
            this.transferRate=transferRate;
            this.pipePos=pipePos;
        }
        public BlockPos getPos() {
            return pos;
        }
        public BlockPos getPipePos() {
            return pipePos;
        }
        public int getTier() {
            return tier;
        }
        public float getTransferRate() {
            return transferRate;
        }
        @Override
        public String toString() {
            return "BlockEntityInfo{pos="+getPos().toString()+", tier="+getTier()+", transferRate="+getTransferRate()+", pipePos="+getPipePos()+"}";
        }
    }
    public static class BlockEntityTransfer {
        private final BlockEntityInfo info;
        private final OceanicSorceryMod.TransferMode mode;
        private final OceanicSorceryMod.Spell spell;

        public BlockEntityTransfer(BlockEntityInfo info, OceanicSorceryMod.TransferMode mode, OceanicSorceryMod.Spell spell){
            this.info=info;
            this.mode=mode;
            this.spell = spell;
        }
        public BlockEntityInfo getInfo() {
            return info;
        }
        public OceanicSorceryMod.TransferMode getMode() {
            return mode;
        }
        public OceanicSorceryMod.Spell getSpell() {
            return spell;
        }
        @Override
        public String toString() {
            return "BlockEntityTransfer{info="+getInfo().toString()+", mode="+getMode()+", spell="+getSpell()+"}";
        }
    }
}
