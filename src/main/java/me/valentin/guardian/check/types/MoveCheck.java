package me.valentin.guardian.check.types;

import me.valentin.guardian.check.AbstractCheck;
import me.valentin.guardian.player.PlayerData;
import me.valentin.guardian.player.tracker.MoveTracker;
import org.bukkit.event.player.PlayerMoveEvent;

public abstract class MoveCheck extends AbstractCheck<PlayerMoveEvent> {

	protected final MoveTracker moveTracker = this.playerData.getMoveTracker();

	public MoveCheck(PlayerData playerData, String name) {
		super(playerData, PlayerMoveEvent.class, name);
	}
}
