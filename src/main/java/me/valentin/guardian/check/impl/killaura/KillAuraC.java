package me.valentin.guardian.check.impl.killaura;

import me.valentin.guardian.check.types.PacketCheck;
import me.valentin.guardian.player.PlayerData;
import net.minecraft.server.v1_7_R4.*;

public class KillAuraC extends PacketCheck {
    private boolean sent;

    public KillAuraC(PlayerData playerData) {
        super(playerData, "Kill Aura Type C");
    }

    @Override
    public void handle(Packet packet) {
        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).c() == EnumEntityUseAction.ATTACK) {
            if (!this.sent)
                return;

            this.flag(this.getPlayer());
        } else if (packet instanceof PacketPlayInBlockPlace) {
            this.sent = true;
        } else if (packet instanceof PacketPlayInFlying) {
            this.sent = false;
        }
    }
}
