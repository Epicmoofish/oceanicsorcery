package net.oceanic.ancientsorcery.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.oceanic.ancientsorcery.OceanicSorceryMod;
import net.oceanic.ancientsorcery.blocks.ElementalNetworkControllerBlock;

import java.util.HashSet;
import java.util.Set;

public class SorceryPacketByteBuf extends PacketByteBuf {
    public SorceryPacketByteBuf(ByteBuf parent) {
        super(parent);
    }
    public static SorceryPacketByteBuf create() {
        return new SorceryPacketByteBuf(Unpooled.buffer());
    }
    public SorceryPacketByteBuf writeBlockEntityInfo(ElementalNetworkControllerBlock.BlockEntityInfo info){
        this.writeBlockPos(info.getPos());
        this.writeInt(info.getTier());
        this.writeFloat(info.getTransferRate());
        this.writeBlockPos(info.getPipePos());
        return this;
    }
    public SorceryPacketByteBuf writeBlockEntityTransfer(ElementalNetworkControllerBlock.BlockEntityTransfer transfer){
        this.writeBlockEntityInfo(transfer.getInfo());
        this.writeInt(transfer.getMode().id);
        this.writeInt(transfer.getSpell().uniqueID);
        return this;
    }
    public ElementalNetworkControllerBlock.BlockEntityTransfer readBlockEntityTransfer(){
        ElementalNetworkControllerBlock.BlockEntityInfo info = this.readBlockEntityInfo();
        OceanicSorceryMod.TransferMode mode = OceanicSorceryMod.TransferMode.valueOfLabel(this.readInt());
        OceanicSorceryMod.Spell spell = OceanicSorceryMod.Spell.valueOfUniqueID(this.readInt());
        return new ElementalNetworkControllerBlock.BlockEntityTransfer(info, mode, spell);
    }
    public SorceryPacketByteBuf writeSetBlockEntityTransfer(Set<ElementalNetworkControllerBlock.BlockEntityTransfer> setTransfer){
        this.writeInt(setTransfer.size());
        for (ElementalNetworkControllerBlock.BlockEntityTransfer transfer: setTransfer){
            this.writeBlockEntityTransfer(transfer);
        }
        return this;
    }
    public Set<ElementalNetworkControllerBlock.BlockEntityTransfer> readSetBlockEntityTransfer(){
        int size = this.readInt();
        Set<ElementalNetworkControllerBlock.BlockEntityTransfer> setTransfer = new HashSet<>();
        for (int index = 0; index<size;index++){
            setTransfer.add(this.readBlockEntityTransfer());
        }
        return setTransfer;
    }
    public ElementalNetworkControllerBlock.BlockEntityInfo readBlockEntityInfo(){
        BlockPos pos = this.readBlockPos();
        int tier = this.readInt();
        float transferRate = this.readFloat();
        BlockPos pipePos = this.readBlockPos();
        ElementalNetworkControllerBlock.BlockEntityInfo info = new ElementalNetworkControllerBlock.BlockEntityInfo(pos, tier, transferRate, pipePos);
        return info;
    }
}