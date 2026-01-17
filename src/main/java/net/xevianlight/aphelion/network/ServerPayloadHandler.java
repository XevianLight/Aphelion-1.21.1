package net.xevianlight.aphelion.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.xevianlight.aphelion.Aphelion;
import net.xevianlight.aphelion.client.PartitionClientState;
import net.xevianlight.aphelion.network.packet.PartitionData;

// Handle packets TO the client FROM the server
public class ServerPayloadHandler {

    public static void handleDataOnMain(PartitionData data, IPayloadContext context) {
        PartitionClientState.set(data);
        Aphelion.LOGGER.info("Partition packet received! id={}", data.id());
    }
}
