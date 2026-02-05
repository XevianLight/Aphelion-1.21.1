package net.xevianlight.aphelion.network;


import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.xevianlight.aphelion.Aphelion;
import net.xevianlight.aphelion.entites.vehicles.RocketEntity;
import net.xevianlight.aphelion.network.packet.ClientPlayerStateUpdatePacket;
import net.xevianlight.aphelion.network.packet.RocketLaunchPayload;

import net.xevianlight.aphelion.client.AphelionClient;

@EventBusSubscriber(modid = Aphelion.MOD_ID)
public final class KeyNetwork {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        // consumeClick makes it fire once per press, not every tick held
        if (AphelionClient.ROCKET_LAUNCH_KEY.consumeClick() && mc.player.getVehicle() instanceof RocketEntity rocket) {

            // Send a packet to the server telling it to try launching the rocket matching this id. The packet only contains the rocketId as an integer.
            PacketDistributor.sendToServer(new RocketLaunchPayload(rocket.getId()));
        }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        int FREQ = 4;
        for (ServerPlayer p : event.getServer().getPlayerList().getPlayers()) {
            if (p.tickCount % FREQ == 0) {
                ClientPlayerState state = ClientPlayerState.getServerStateOf(p);

                PacketDistributor.sendToPlayer(p, new ClientPlayerStateUpdatePacket(state.oxygen(), state.gravity(), state.temperature()));
            }
        }

    }
}
