package net.oceanic.ancientsorcery.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.oceanic.ancientsorcery.ItemImbuementInfo;
import net.oceanic.ancientsorcery.OceanicSorceryMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin({Item.class, CrossbowItem.class, ShieldItem.class})
public class TooltipMixin {
	@Inject(method = "appendTooltip", at = @At("HEAD"))
	public void onTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
		if (!stack.isEmpty() && ItemImbuementInfo.isImbued(stack) && ItemImbuementInfo.canImbue(stack)) {
			tooltip.add(Text.literal(Text.translatable("imbuement."+ItemImbuementInfo.getImbuementType(stack)).getString()+"§r: §6" + (ItemImbuementInfo.getImbuementPercentage(stack))+"%"));
		}
	}
}