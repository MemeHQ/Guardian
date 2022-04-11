package me.valentin.guardian.player.tracker;

import me.valentin.guardian.update.RotationUpdate;
import me.valentin.guardian.check.types.RotationCheck;
import me.valentin.guardian.player.PlayerData;

public class RotationTracker {
    private Float lastYaw, lastPitch;

    public void handleLook(PlayerData playerData, float yaw, float pitch) {
        if (lastYaw != null && lastPitch != null) {
            playerData.getChecks().stream()
                    .filter(RotationCheck.class::isInstance)
                    .map(RotationCheck.class::cast)
                    .forEach(c -> c.handle(new RotationUpdate(lastYaw, yaw, lastPitch, pitch)));
        }

        lastYaw = yaw;
        lastPitch = pitch;
    }
}
