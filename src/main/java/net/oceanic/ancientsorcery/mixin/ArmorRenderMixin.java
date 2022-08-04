package net.oceanic.ancientsorcery.mixin;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vector4f;
import net.oceanic.ancientsorcery.ItemImbuementInfo;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.List;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorRenderMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {
	@Inject(method = "renderArmor", at = @At(value="TAIL"),locals= LocalCapture.CAPTURE_FAILEXCEPTION)
	private void onRenderPart(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo ci, ItemStack itemStack, ArmorItem armorItem, boolean bl, boolean bl2) {
		if (armorItem instanceof DyeableArmorItem) {
			int i = ((DyeableArmorItem)armorItem).getColor(itemStack);
			float f = (float)(i >> 16 & 0xFF) / 255.0f;
			float g = (float)(i >> 8 & 0xFF) / 255.0f;
			float h = (float)(i & 0xFF) / 255.0f;
			int i2 = ItemImbuementInfo.getColor(itemStack);
			float r2 = (float)(i2 >> 16 & 0xFF) / 255.0f;
			float g2 = (float)(i2 >> 8 & 0xFF) / 255.0f;
			float b2 = (float)(i2 & 0xFF) / 255.0f;
			float percentage = ((float)ItemImbuementInfo.getImbuementPercentage(itemStack))/100.0f;
			this.renderArmorParts(matrices, vertexConsumers, light, armorItem, bl2, model, bl, f*(1-percentage)+r2*percentage, g*(1-percentage)+g2*percentage, h*(1-percentage)+b2*percentage, null,1.0f);
			this.renderArmorParts(matrices, vertexConsumers, light, armorItem, bl2, model, bl, r2, g2, b2, "overlay",percentage);
		} else {
			int i2 = ItemImbuementInfo.getColor(itemStack);
			float r2 = (float)(i2 >> 16 & 0xFF) / 255.0f;
			float g2 = (float)(i2 >> 8 & 0xFF) / 255.0f;
			float b2 = (float)(i2 & 0xFF) / 255.0f;
			float percentage = ((float)ItemImbuementInfo.getImbuementPercentage(itemStack))/100.0f;
			this.renderArmorParts(matrices, vertexConsumers, light, armorItem, bl2, model, bl, r2, g2, b2, null,percentage);
		}
	}
	private void renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItem item, boolean usesSecondLayer, A model, boolean legs, float red, float green, float blue, @Nullable String overlay,float alpha) {
		VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(((ArmorFeatureRenderer)(Object)this).getArmorTexture(item, legs, overlay)), false, usesSecondLayer);
		((AnimalModel)model).render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, alpha);
	}
}