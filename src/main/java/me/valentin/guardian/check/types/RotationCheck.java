package me.valentin.guardian.check.types;

import me.valentin.guardian.check.AbstractCheck;
import me.valentin.guardian.player.PlayerData;
import me.valentin.guardian.update.RotationUpdate;

public abstract class RotationCheck extends AbstractCheck<RotationUpdate> {
    public RotationCheck(PlayerData playerData, String name) {
        super(playerData, RotationUpdate.class, name);
    }


}
