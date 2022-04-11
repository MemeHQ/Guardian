package me.valentin.guardian.player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.Getter;
import lombok.Setter;
import me.valentin.guardian.Guardian;
import me.valentin.guardian.check.AbstractCheck;
import me.valentin.guardian.check.impl.aimassist.AimAssistA;
import me.valentin.guardian.check.impl.aimassist.AimAssistC;
import me.valentin.guardian.check.impl.autoclicker.*;
import me.valentin.guardian.check.impl.fly.FlyA;
import me.valentin.guardian.check.impl.fly.FlyB;
import me.valentin.guardian.check.impl.fly.FlyC;
import me.valentin.guardian.check.impl.killaura.KillAuraA;
import me.valentin.guardian.check.impl.killaura.KillAuraB;
import me.valentin.guardian.check.impl.killaura.KillAuraC;
import me.valentin.guardian.check.impl.killaura.KillAuraD;
import me.valentin.guardian.check.impl.range.RangeA;
import me.valentin.guardian.check.impl.range.RangeB;
import me.valentin.guardian.check.impl.speed.SpeedA;
import me.valentin.guardian.check.impl.speed.SpeedB;
import me.valentin.guardian.check.impl.timer.TimerA;
import me.valentin.guardian.check.impl.velocity.VelocityA;
import me.valentin.guardian.hitbox.PlayerHitbox;
import me.valentin.guardian.packet.PacketManager;
import me.valentin.guardian.player.tracker.ActionTracker;
import me.valentin.guardian.player.tracker.MoveTracker;
import me.valentin.guardian.player.tracker.RotationTracker;
import me.valentin.guardian.wrapper.impl.WrapperNetworkManager;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInCustomPayload;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

@Getter
public class PlayerData {

	private static final List<Class<? extends AbstractCheck>> CHECKS = Arrays.asList(
			AimAssistA.class, AimAssistC.class,

			AutoClickerA.class, AutoClickerB.class, AutoClickerC.class, AutoClickerD.class,
			AutoClickerE.class, AutoClickerF.class, AutoClickerG.class, AutoClickerH.class,

			FlyA.class, FlyB.class, FlyC.class,

			TimerA.class,

			KillAuraA.class, KillAuraB.class, KillAuraC.class, KillAuraD.class,

			RangeA.class, RangeB.class,

			SpeedA.class, SpeedB.class,

			VelocityA.class);

	private final Guardian plugin = Guardian.getInstance();

	private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

	private final Set<AbstractCheck> checks = new HashSet<>();

	private final UUID uuid;

	private final ActionTracker actionTracker = new ActionTracker();
	private final MoveTracker moveTracker = new MoveTracker(this);
	private final RotationTracker rotationTracker = new RotationTracker();

	private int loginDelay, flyDelay, teleportDelay;

	@Setter
	private PlayerHitbox hitbox;
	private long lastFlying, lastLastFlying;

	@Setter
	private boolean banning;

	private final Deque<PlayerHitbox> hitboxes = new LinkedList<>();

	public PlayerData(Player player) {
		this.uuid = player.getUniqueId();

		PlayerData.CHECKS.forEach(check -> {
			try {
				checks.add(check.getConstructor(PlayerData.class).newInstance(this));
			} catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
				e.printStackTrace();
			}
		});

		EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
		WrapperNetworkManager networkManager = new WrapperNetworkManager(nmsPlayer.playerConnection.networkManager);
		PlayerData.EXECUTOR.execute(() -> networkManager.getChannel().pipeline().addBefore("packet_handler", "gbo_gaston_packet_handler", new PacketManager(this)));
	}

	public void handleInboundPacket(Packet packet) {
		if (this.loginDelay > 0) {
			return;
		}

		this.actionTracker.handleInboundPacket(packet);

		long now = System.currentTimeMillis();

		if (packet instanceof PacketPlayInFlying) {
			if (((PacketPlayInFlying) packet).k()) {
				this.rotationTracker.handleLook(this, ((PacketPlayInFlying) packet).g(), ((PacketPlayInFlying) packet).h());
			}
			this.lastLastFlying = this.lastFlying;
			this.lastFlying = now;
		}

		this.checks.stream()
				.filter(c -> c.getType() == Packet.class)
				.forEach(c -> c.handle(packet));

		if (packet instanceof PacketPlayInCustomPayload && ((PacketPlayInCustomPayload) packet).c().equalsIgnoreCase("MC|Brand")) {
			this.loginDelay = 20;
		} else if (packet instanceof PacketPlayInFlying) {
			this.loginDelay--;
		}
	}

	public void handleOutboundPacket(Packet packet) {
		if (this.loginDelay > 0) {
			return;
		}

		this.moveTracker.handleOutboundPacket(packet);
	}

	public void handleMove(PlayerMoveEvent event) {
		if (this.loginDelay > 0) {
			return;
		}

		if (this.getPlayer().getAllowFlight()) {
			flyDelay = 20;
			return;
		} else if (flyDelay > 0) {
			--flyDelay;
			return;
		}

		this.moveTracker.handleMove(this, event);

		if (event.getTo().getX() != event.getFrom().getX() || event.getTo().getY() != event.getFrom().getY()
				|| event.getTo().getZ() != event.getFrom().getZ()) {
			this.checks.stream()
					.filter(c -> c.getType() == PlayerMoveEvent.class)
					.forEach(c -> c.handle(event));
		}
	}

	public Player getPlayer() {
		return this.plugin.getServer().getPlayer(this.uuid);
	}
}
