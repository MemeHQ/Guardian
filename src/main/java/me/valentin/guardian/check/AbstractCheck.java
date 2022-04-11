package me.valentin.guardian.check;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.valentin.guardian.Guardian;
import me.valentin.guardian.player.PlayerData;
import me.valentin.guardian.player.tracker.ActionTracker;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Getter
public abstract class AbstractCheck<T> implements ICheck<T> {

	protected final Guardian plugin = Guardian.getInstance();

	protected final PlayerData playerData;
	protected final Class<T> type;

	protected final String name;

	protected final ActionTracker actionTracker;

	protected double vl;

	@Setter
	private boolean useViolation = true;

	private final Set<Long> violations = new HashSet<>();

	public AbstractCheck(PlayerData playerData, Class<T> type, String name) {
		this.playerData = playerData;
		this.type = type;
		this.name = name;

		this.actionTracker = this.playerData.getActionTracker();
	}


	protected void flag(Player player, String alert) {

		String theAlert = ChatColor.YELLOW + "[Guardian]" + ChatColor.RED + player.getName() + ChatColor.YELLOW + " flagged " + ChatColor.RED + this.name +
				ChatColor.YELLOW + " " + alert + ChatColor.YELLOW + " [" + ChatColor.YELLOW + "Ping: " + ChatColor.YELLOW + ((CraftPlayer) player).getHandle().ping + "]" ;

		this.plugin.getAlertsManager().sendMessage(theAlert);

		this.violations.removeIf(v -> System.currentTimeMillis() > v + 1200L);
		this.violations.add(System.currentTimeMillis());
		if (this.violations.size() == 5 && this.useViolation) {
			this.ban(player);
		}
	}

	protected void flag(Player player) {
		this.flag(player, "");
	}

	protected void ban(Player player) {
		if (playerData.isBanning())
			return;
		if (!player.hasPermission("guardian.bypass")) {

			playerData.setBanning(true);
			this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
				this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), "ban -s " + player.getName() + " Cheating"); //to be decided

				this.plugin.getServer().broadcastMessage(ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + "----------------------------------------------------");
				this.plugin.getServer().broadcastMessage(ChatColor.RED + "Guardian" + " removed " + ChatColor.YELLOW + player.getName() + ChatColor.RED + " from the network.");
				this.plugin.getServer().broadcastMessage(ChatColor.RED + "Reason: " + ChatColor.YELLOW + "Unfair Advantage");
				this.plugin.getServer().broadcastMessage(ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + "----------------------------------------------------");

			});
		} else {}


		this.plugin.getPlayerManager().getPlayerDataMap().remove(this.playerData.getUuid());
	}

	protected Player getPlayer() {
		return this.plugin.getServer().getPlayer(this.playerData.getUuid());
	}

	protected void setVl(double vl) {
		this.vl = Math.max(0D, vl);
	}
}
