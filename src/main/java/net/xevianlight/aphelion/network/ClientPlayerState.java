package net.xevianlight.aphelion.network;

import net.minecraft.world.entity.player.Player;
import net.xevianlight.aphelion.core.saveddata.types.GravityData;
import net.xevianlight.aphelion.systems.GravityService;
import net.xevianlight.aphelion.systems.OxygenService;

/// Read-only player state object; updated by a server packet every so often
public record ClientPlayerState(boolean oxygen, float gravity, float temperature) {
    // Default player state
    private static ClientPlayerState localData = new ClientPlayerState(true, GravityData.DEFAULT_GRAVITY * 0.5f, 50f);

    public static void updateState(ClientPlayerState newData) {
        onStateUpdate(localData, newData);
        localData = newData;
    }

    public static ClientPlayerState getServerStateOf(Player player) {
        return new ClientPlayerState(OxygenService.hasOxygen(player), GravityService.getGravityAccel(player), 50f);
    }

    /// For things like playing SFX, VFX, etc. etc.
    public static void onStateUpdate(ClientPlayerState oldData, ClientPlayerState newData) {
        // TODO: add sfx
        if (!oldData.oxygen() && newData.oxygen()) {
            // On oxygen gained
        }
        if (oldData.oxygen() && !newData.oxygen()) {
            // On oxygen removed
        }
        if (newData.gravity() - 0.25f > oldData.gravity()) {
            // On gravity increased by > 0.25
        }
        if (oldData.gravity() - 0.25f > newData.gravity()) {
            // On gravity decreased by > 0.25
        }
    }

    public static ClientPlayerState getLocalData() {
        return localData;
    }
}
