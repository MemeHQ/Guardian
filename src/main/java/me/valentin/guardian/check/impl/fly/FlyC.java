package me.valentin.guardian.check.impl.fly;

import me.valentin.guardian.check.types.MoveCheck;
import me.valentin.guardian.player.PlayerData;
import org.bukkit.event.player.PlayerMoveEvent;

public class FlyC extends MoveCheck {

	public FlyC(PlayerData playerData) {
		super(playerData, "Fly Type C");
	}

	@Override
	public void handle(PlayerMoveEvent event) {
		if (event.getTo().getY() < 4D)
			return;

		if (Math.abs(event.getTo().getY() - event.getFrom().getY()) >= 2.5 && (event.getTo().getY() - event.getFrom().getY()) != 4.334) {
			this.flag(this.getPlayer(), "O " + (event.getTo().getY() - event.getFrom().getY()));
		}
	}
}
