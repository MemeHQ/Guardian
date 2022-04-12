package me.valentin.guardian;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.GuardianEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

@Getter
public class Guardian extends JavaPlugin implements Listener {


	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onGuardianEvent(GuardianEvent event) {

		String data = getData(event.getData());
		String displayName = getNonScuffedAlertName(event.getCheat()) + " " + event.getModule();

		for (Player players : Bukkit.getOnlinePlayers()) {
			if (players.hasPermission("guardian.alert")) {
				players.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&c!&7] &e" + event.getPlayer().getName()) + " &fhas been detected using " + displayName);
			}
		}

	}

	private String getNonScuffedAlertName(GuardianEvent.Cheat cheat) {
		if (cheat == GuardianEvent.Cheat.SPEED_HACKS) {
			return "Speed";
		} else if (cheat == GuardianEvent.Cheat.FLY_HACKS) {
			return "Flight";
		} else if (cheat == GuardianEvent.Cheat.AUTO_CLICKER) {
			return "Auto Clicker";
		} else if (cheat == GuardianEvent.Cheat.KILL_AURA) {
			return "Kill Aura";
		} else if (cheat == GuardianEvent.Cheat.HOVER) {
			return "Hover";
		} else if (cheat == GuardianEvent.Cheat.CRITICALS) {
			return "Criticals";
		} else if (cheat == GuardianEvent.Cheat.NO_FALL) {
			return "No Fall";
		} else if (cheat == GuardianEvent.Cheat.TIMER) {
			return "Timer";
		} else if (cheat == GuardianEvent.Cheat.PHASE) {
			return "Phase";
		} else if (cheat == GuardianEvent.Cheat.FAST_USE) {
			return "Fast Use";
		} else if (cheat == GuardianEvent.Cheat.REGENERATION) {
			return "Regen";
		} else if (cheat == GuardianEvent.Cheat.CLIENT_MODIFICATIONS) {
			return "Client Modifications";
		} else if (cheat == GuardianEvent.Cheat.REACH) {
			return "Reach";
		} else if (cheat == GuardianEvent.Cheat.GENERAL) {
			return "General";
		} else if (cheat == GuardianEvent.Cheat.DEBUG) {
			return "Debug";
		} else {
			return "Unknown";
		}
	}




	private String getData(Map<String, Object> data) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			if(sb.length() != 0) {
				sb.append(", ");
			}
			sb.append(entry.getKey()).append(" ").append(entry.getValue());
		}
		return sb.toString();
	}
}

