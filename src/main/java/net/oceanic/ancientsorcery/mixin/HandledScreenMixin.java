package net.oceanic.ancientsorcery.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.oceanic.ancientsorcery.ItemImbuementInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.util.Random;

@Mixin(ClientPlayerEntity.class)
public class HandledScreenMixin {
	@Inject(method = "tick", at = @At("RETURN"))
	private void drawParticles(CallbackInfo ci) {
		ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;
		for (int particles=0;particles<100;particles++) {
//			if (ItemImbumentInfo.canImbue(player.getMainHandStack())&& new Random().nextDouble() < ((double)ItemImbumentInfo.getImbumentPercentage(player.getMainHandStack()))/100.0) {
//				player.clientWorld.addParticle(ParticleTypes.FLAME, player.getX() + player.getHandPosOffset(player.getMainHandStack().getItem()).getX()+new Random().nextDouble(0.1), player.getY() + player.getHandPosOffset(player.getMainHandStack().getItem()).getY()+new Random().nextDouble(0.1), player.getZ() + player.getHandPosOffset(player.getMainHandStack().getItem()).getZ()+new Random().nextDouble(0.1), 0.0, 0.0, 0.0);
//			}
//			if (ItemImbumentInfo.canImbue(player.getOffHandStack()) && new Random().nextDouble() < ((double)ItemImbumentInfo.getImbumentPercentage(player.getOffHandStack()))/100.0) {
//				player.clientWorld.addParticle(ParticleTypes.FLAME, player.getX() + player.getHandPosOffset(player.getOffHandStack().getItem()).getX()+new Random().nextDouble(0.1), player.getY() + player.getHandPosOffset(player.getOffHandStack().getItem()).getY()+new Random().nextDouble(0.1), player.getZ() + player.getHandPosOffset(player.getOffHandStack().getItem()).getZ()+new Random().nextDouble(0.1), 0.0, 0.0, 0.0);
//			}
			}
		}
}