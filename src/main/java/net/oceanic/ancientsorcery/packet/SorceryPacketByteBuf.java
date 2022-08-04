package net.oceanic.ancientsorcery.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.oceanic.ancientsorcery.OceanicSorceryMod;
import net.oceanic.ancientsorcery.blocks.ElementalNetworkControllerBlock;

import java.util.HashSet;
import java.util.Set;

public class SorceryPacketByteBuf {
    public static PacketByteBuf writeBlockEntityInfo(ElementalNetworkControllerBlock.BlockEntityInfo info, PacketByteBuf buf){
        buf.writeBlockPos(info.getPos());
        buf.writeInt(info.getTier());
        buf.writeFloat(info.getTransferRate());
        buf.writeBlockPos(info.getPipePos());
        return buf;
    }
    public static PacketByteBuf writeBlockEntityTransfer(ElementalNetworkControllerBlock.BlockEntityTransfer transfer, PacketByteBuf buf){
        SorceryPacketByteBuf.writeBlockEntityInfo(transfer.getInfo(), buf);
        buf.writeInt(transfer.getMode().id);
        buf.writeInt(transfer.getSpell().uniqueID);
        return buf;
    }
    public static PacketByteBuf writeSetBlockEntityTransfer(Set<ElementalNetworkControllerBlock.BlockEntityTransfer> setTransfer, PacketByteBuf buf){
        buf.writeInt(setTransfer.size());
        for (ElementalNetworkControllerBlock.BlockEntityTransfer transfer: setTransfer){
            SorceryPacketByteBuf.writeBlockEntityTransfer(transfer, buf);
        }
        return buf;
    }
    public static ElementalNetworkControllerBlock.BlockEntityTransfer readBlockEntityTransfer(PacketByteBuf buf){
        ElementalNetworkControllerBlock.BlockEntityInfo info = SorceryPacketByteBuf.readBlockEntityInfo(buf);
        OceanicSorceryMod.TransferMode mode = OceanicSorceryMod.TransferMode.valueOfLabel(buf.readInt());
        OceanicSorceryMod.Spell spell = OceanicSorceryMod.Spell.valueOfUniqueID(buf.readInt());
        return new ElementalNetworkControllerBlock.BlockEntityTransfer(info, mode, spell);
    }
    public static Set<ElementalNetworkControllerBlock.BlockEntityTransfer> readSetBlockEntityTransfer(PacketByteBuf buf){
        int size = buf.readInt();
        Set<ElementalNetworkControllerBlock.BlockEntityTransfer> setTransfer = new HashSet<>();
        for (int index = 0; index<size;index++){
            setTransfer.add(SorceryPacketByteBuf.readBlockEntityTransfer(buf));
        }
        return setTransfer;
    }
    public static ElementalNetworkControllerBlock.BlockEntityInfo readBlockEntityInfo(PacketByteBuf buf){
        BlockPos pos = buf.readBlockPos();
        int tier = buf.readInt();
        float transferRate = buf.readFloat();
        BlockPos pipePos = buf.readBlockPos();
        ElementalNetworkControllerBlock.BlockEntityInfo info = new ElementalNetworkControllerBlock.BlockEntityInfo(pos, tier, transferRate, pipePos);
        return info;
    }
}