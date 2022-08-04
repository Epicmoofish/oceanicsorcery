package net.oceanic.ancientsorcery.packet;

import net.minecraft.util.Identifier;
import net.oceanic.ancientsorcery.OceanicSorceryMod;

public class PacketInfo {
    public static Identifier SERVERBOUND_CONTROLLER_ID = new Identifier(OceanicSorceryMod.MODID+":server_controller_id");
    public static Identifier CLIENTBOUND_CONTROLLER_ID = new Identifier(OceanicSorceryMod.MODID+":client_controller_id");
}
