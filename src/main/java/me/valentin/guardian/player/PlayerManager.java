package me.valentin.guardian.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import me.valentin.guardian.Guardian;

@Getter
public class PlayerManager {

	private final Guardian plugin = Guardian.getInstance();

	private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

}
