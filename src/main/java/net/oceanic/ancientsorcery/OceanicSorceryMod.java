package net.oceanic.ancientsorcery;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.oceanic.ancientsorcery.blocks.BlockInit;
import net.oceanic.ancientsorcery.blocks.ElementalNetworkControllerBlock;
import net.oceanic.ancientsorcery.blocks.ElementalPipeBlock;
import net.oceanic.ancientsorcery.blocks.blockentity.NetworkControllerBlockEntity;
import net.oceanic.ancientsorcery.callbacks.BlockUpdateCallback;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class OceanicSorceryMod implements ModInitializer {
	public static String MODID = "ancientsorcery";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	@Override
	public void onInitialize() {
		BlockInit.init();
		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			ItemStack stack = player.getStackInHand(hand);
			if (ItemImbuementInfo.isImbued(stack)) {
				if (ItemImbuementInfo.getImbuementType(stack).equals("gravity")) {
					if (ItemImbuementInfo.setImbuementPercentage(stack, ItemImbuementInfo.getImbuementPercentage(stack) - 10)) {
						entity.setOnGround(false);
						entity.setNoGravity(true);
						float pitch = player.getPitch();
						float yaw = player.getYaw();
						entity.addVelocity(player.getRotationVector().x * 2, player.getRotationVector().y * 2, player.getRotationVector().z * 2);
						for (int dp=-20;dp<21;dp++) {
							for (int dy=-20;dy<21;dy++) {
								if ((dp*dp + dy*dy)<=100) {
									world.addParticle(ParticleTypes.REVERSE_PORTAL, player.getX(), player.getEyeY(), player.getZ(), (this.getRotationVector(pitch + (float) dp * 2.0F, yaw + (float) dy * 2.0F).x)*5, (this.getRotationVector(pitch + (float) dp * 2.0F, yaw + (float) dy * 2.0F).y)*5, (this.getRotationVector(pitch + (float) dp * 2.0F, yaw + (float) dy * 2.0F).z)*5);
								}
							}
						}
					}
				}
			}
			return ActionResult.PASS;
		});
		BlockUpdateCallback.EVENT.register((world,pos,oldstate, newstate) -> {
			if (!world.isClient() && (oldstate.getBlock() instanceof ElementalPipeBlock || newstate.getBlock() instanceof ElementalPipeBlock || oldstate.hasBlockEntity() || newstate.hasBlockEntity())) {
				world.blockEntityTickers.forEach(entityTicker -> {
					if (world.getBlockEntity(entityTicker.getPos()) != null) {
						if (world.getBlockEntity(entityTicker.getPos()) instanceof NetworkControllerBlockEntity) {
							((NetworkControllerBlockEntity)world.getBlockEntity(entityTicker.getPos())).shouldUpdate = true;
						}
					}
				});
			}
			return ActionResult.PASS;
		});
	}
		protected final Vec3d getRotationVector(float pitch, float yaw) {
			float f = pitch * ((float)Math.PI / 180);
			float g = -yaw * ((float)Math.PI / 180);
			float h = MathHelper.cos(g);
			float i = MathHelper.sin(g);
			float j = MathHelper.cos(f);
			float k = MathHelper.sin(f);
			return new Vec3d(i * j, -k, h * j).normalize();
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution
	}
	public enum TransferMode{
		EXTRACT(0),
		BUFFER(1),
		INSERT(2);
		public final int id;
		TransferMode(int id) {
			this.id=id;
		}
		public static TransferMode valueOfLabel(int id) {
			for (TransferMode e : values()) {
				if (e.id==id) {
					return e;
				}
			}
			return null;
		}
	}

	public enum Spell{
		GRAVITY(1,"gravity",0),
		FIRE(2,"fire",1),
		ICE(3,"ice",2),
		LIGHTNING(4,"lightning",3),
		EARTH(5,"earth",4);
		public final int tier;
		public final String id;
		public final int uniqueID;
		Spell(int tier, String id, int uniqueID) {
			this.tier = tier;
			this.id = id;
			this.uniqueID = uniqueID;
		}
		public static Spell valueOfLabel(String id) {
			for (Spell e : values()) {
				if (e.id.equals(id)) {
					return e;
				}
			}
			return null;
		}
		public static Spell valueOfUniqueID(int uniqueID) {
			for (Spell e : values()) {
				if (e.uniqueID==(uniqueID)) {
					return e;
				}
			}
			return null;
		}
		public static List<Spell> getAllSpells(int maxTier){
			List<Spell> allSpells= Arrays.stream(values()).toList();
			allSpells.removeIf(spell-> spell.tier>maxTier);
			return allSpells;
		}
	}

}
