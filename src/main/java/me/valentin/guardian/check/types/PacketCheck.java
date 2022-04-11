package me.valentin.guardian.check.types;

import me.valentin.guardian.check.AbstractCheck;
import me.valentin.guardian.player.PlayerData;
import net.minecraft.server.v1_7_R4.Packet;

public abstract class PacketCheck extends AbstractCheck<Packet> {

	public PacketCheck(PlayerData playerData, String name) {
		super(playerData, Packet.class, name);
	}

	public int getMaxVl() {
		return 20;
	}
}
