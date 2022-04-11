package me.valentin.guardian.alerts;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import me.valentin.guardian.Guardian;

@Getter
public class AlertsManager {

	private final Guardian plugin = Guardian.getInstance();

	private final Set<UUID> players = new HashSet<>();

	public void sendMessage(String message) {
		this.players.stream()
				.map(this.plugin.getServer()::getPlayer)
				.filter(Objects::nonNull)
				.forEach(p -> p.sendMessage(message));
	}
}
