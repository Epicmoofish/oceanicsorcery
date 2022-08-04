package net.oceanic.ancientsorcery.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.oceanic.ancientsorcery.ItemImbuementInfo;
import net.oceanic.ancientsorcery.callbacks.BlockUpdateCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin({World.class, ServerWorld.class})
public class WorldCallbackMixin {
	@Inject(method = "onBlockChanged", at = @At("RETURN"))
	public void onBlockUpdate(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo ci) {
		ActionResult result = BlockUpdateCallback.EVENT.invoker().update((World)(Object)(this), pos, oldBlock, newBlock);
	}

}