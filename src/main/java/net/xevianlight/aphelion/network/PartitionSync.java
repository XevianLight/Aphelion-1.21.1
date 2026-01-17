package net.xevianlight.aphelion.network;


import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.xevianlight.aphelion.Aphelion;
import net.xevianlight.aphelion.core.space.SpacePartitionSavedData;
import net.xevianlight.aphelion.network.packet.PartitionData;
import net.xevianlight.aphelion.util.SpacePartitionHelper;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@EventBusSubscriber(modid = Aphelion.MOD_ID)
public final class PartitionSync {

    // send once right after join (safe: delayed to next server tick)
    private static final Set<UUID> PENDING_JOIN_SEND = new HashSet<>();

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.getEntity() instanceof ServerPlayer sp) {
            PENDING_JOIN_SEND.add(sp.getUUID());
        }
    }

    private static final java.util.Map<UUID, PartitionData> LAST_SENT = new java.util.HashMap<>();

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post e) {
        var server = e.getServer();

//        Aphelion.LOGGER.info("WORKS!!!");

        for (ServerPlayer sp : server.getPlayerList().getPlayers()) {
            PartitionData now = computePartitionFor(sp); // your logic
            PartitionData prev = LAST_SENT.get(sp.getUUID());

            if (prev == null || !prev.equals(now)) {
                PacketDistributor.sendToPlayer(sp, now);
                LAST_SENT.put(sp.getUUID(), now);
            }
        }
    }

    private static PartitionData computePartitionFor(ServerPlayer sp) {
        // Example: convert player position to partition coords
        int px = (int)Math.floor(sp.getX() / SpacePartitionHelper.SIZE);
        int pz = (int)Math.floor(sp.getZ() / SpacePartitionHelper.SIZE);

        var orbit = SpacePartitionSavedData.get(sp.serverLevel()).getOrbitForPartition(px, pz);
        String orbitId = (orbit != null) ? orbit.toString() : "aphelion:orbit/default";

        return new PartitionData(orbitId);
    }
}
