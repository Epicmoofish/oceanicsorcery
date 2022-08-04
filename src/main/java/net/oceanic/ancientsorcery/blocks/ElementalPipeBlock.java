package net.oceanic.ancientsorcery.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ElementalPipeBlock extends Block {
    private final int tier;
    private final int transferRate;
    public ElementalPipeBlock(Settings settings, int tier, int transferRate) {
        super(settings);
        this.tier = tier;
        this.transferRate=transferRate;
    }
    public int getTransferRate(){
        return transferRate;
    }
    public int getTier(){
        return tier;
    }
    public boolean validPipeToConnect(BlockPos pos, World world){
        if (world.getBlockState(pos).getBlock() instanceof ElementalNetworkControllerBlock){
            return true;
        }
        return world.getBlockState(pos).getBlock().equals(this);
    }
}
