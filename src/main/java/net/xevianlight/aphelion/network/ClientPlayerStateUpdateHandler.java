package net.xevianlight.aphelion.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.xevianlight.aphelion.network.packet.ClientPlayerStateUpdatePacket;

public class ClientPlayerStateUpdateHandler {

    public static void handleDataOnMain(ClientPlayerStateUpdatePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientPlayerState.updateState(new ClientPlayerState(packet.oxygen(), packet.gravity(), packet.temp())));
    }
}
