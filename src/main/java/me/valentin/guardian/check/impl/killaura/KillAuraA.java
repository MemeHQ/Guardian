package me.valentin.guardian.check.impl.killaura;

import me.valentin.guardian.check.types.PacketCheck;
import me.valentin.guardian.player.PlayerData;
import net.minecraft.server.v1_7_R4.EnumEntityUseAction;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInArmAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;

public class KillAuraA extends PacketCheck {

	private boolean sent;

	public KillAuraA(PlayerData playerData) {
		super(playerData, "Kill Aura Type A");
	}

	@Override
	public void handle(Packet packet) {
		if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).c() == EnumEntityUseAction.ATTACK) {
			if (!this.sent) {
				this.flag(this.getPlayer());
			}
		} else if (packet instanceof PacketPlayInArmAnimation) {
			this.sent = true;
		} else if (packet instanceof PacketPlayInFlying) {
			this.sent = false;
		}
	}
}
