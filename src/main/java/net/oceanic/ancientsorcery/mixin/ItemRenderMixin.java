package net.oceanic.ancientsorcery.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.oceanic.ancientsorcery.ItemImbuementInfo;
import net.oceanic.ancientsorcery.callbacks.BlockUpdateCallback;
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

@Mixin(ItemRenderer.class)
public class ItemRenderMixin {
	@Inject(method = "renderBakedItemQuads", at = @At(value="INVOKE",target="Lnet/minecraft/client/render/VertexConsumer;quad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/model/BakedQuad;FFFII)V",shift=At.Shift.AFTER),locals= LocalCapture.CAPTURE_FAILEXCEPTION)
	public void onBlockUpdate(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, ItemStack stack, int light, int overlay, CallbackInfo ci, boolean bl, MatrixStack.Entry entry, Iterator var9, BakedQuad bakedQuad, int i, float f, float g, float h) {
		if (ItemImbuementInfo.isImbued(stack)) {
			this.customQuad(vertices,entry, bakedQuad, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, f, g, h, new int[]{light, light, light, light}, overlay, false, stack);
		}
	}
	public void customQuad(VertexConsumer vertices, MatrixStack.Entry matrixEntry, BakedQuad quad, float[] brightnesses, float red, float green, float blue, int[] lights, int overlay, boolean useQuadColorData, ItemStack stack){
		float[] fs = new float[]{brightnesses[0], brightnesses[1], brightnesses[2], brightnesses[3]};
		int[] is = new int[]{lights[0], lights[1], lights[2], lights[3]};
		int[] js = quad.getVertexData();
		Vec3i vec3i = quad.getFace().getVector();
		Vec3f vec3f = new Vec3f(vec3i.getX(), vec3i.getY(), vec3i.getZ());
		Matrix4f matrix4f = matrixEntry.getPositionMatrix();
		vec3f.transform(matrixEntry.getNormalMatrix());
		int i = 8;
		int j = js.length / 8;
		try (MemoryStack memoryStack = MemoryStack.stackPush();){
			ByteBuffer byteBuffer = memoryStack.malloc(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL.getVertexSizeByte());
			IntBuffer intBuffer = byteBuffer.asIntBuffer();
			for (int k = 0; k < j; ++k) {
				float q;
				float p;
				float o;
				float n;
				float m;
				intBuffer.clear();
				intBuffer.put(js, k * 8, 8);
				float f = byteBuffer.getFloat(0);
				float g = byteBuffer.getFloat(4);
				float h = byteBuffer.getFloat(8);
				if (useQuadColorData) {
					float l = (float)(byteBuffer.get(12) & 0xFF) / 255.0f;
					m = (float)(byteBuffer.get(13) & 0xFF) / 255.0f;
					n = (float)(byteBuffer.get(14) & 0xFF) / 255.0f;
					float percentage = ((float)ItemImbuementInfo.getImbuementPercentage(stack))/100.0f;
					red= l * (1.0f-percentage) + red * percentage;
					green= m * (1.0f-percentage) + green * percentage;
					blue= n * (1.0f-percentage) + blue * percentage;
					o = l * fs[k] * red;
					p = m * fs[k] * green;
					q = n * fs[k] * blue;
				} else {
					float l = (float)(byteBuffer.get(12) & 0xFF) / 255.0f;
					m = (float)(byteBuffer.get(13) & 0xFF) / 255.0f;
					n = (float)(byteBuffer.get(14) & 0xFF) / 255.0f;
					float percentage = ((float)ItemImbuementInfo.getImbuementPercentage(stack))/100.0f;
					red= l * (1.0f-percentage) + red * percentage;
					green= m * (1.0f-percentage) + green * percentage;
					blue= n * (1.0f-percentage) + blue * percentage;
					o = fs[k] * red;
					p = fs[k] * green;
					q = fs[k] * blue;
				}
				int r = is[k];
				m = byteBuffer.getFloat(16);
				n = byteBuffer.getFloat(20);
				Vector4f vector4f = new Vector4f(f, g, h, 1.0f);
				vector4f.transform(matrix4f);
				vertices.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), o, p, q, 1.0f, m, n, overlay, r, vec3f.getX(), vec3f.getY(), vec3f.getZ());
			}
		}
	}
}