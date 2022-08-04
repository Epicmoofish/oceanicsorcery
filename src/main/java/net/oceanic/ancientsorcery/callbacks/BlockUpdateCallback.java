package net.oceanic.ancientsorcery.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface BlockUpdateCallback {

        Event<BlockUpdateCallback> EVENT = EventFactory.createArrayBacked(BlockUpdateCallback.class,
                (listeners) -> (world,pos,oldstate,newstate) -> {
                    for (BlockUpdateCallback listener : listeners) {
                        ActionResult result = listener.update(world,pos,oldstate, newstate);
                        if(result != ActionResult.PASS) {
                            return result;
                        }
                    }
                    return ActionResult.PASS;
                });

        ActionResult update(World world, BlockPos pos, BlockState oldstate, BlockState newstate);
}
