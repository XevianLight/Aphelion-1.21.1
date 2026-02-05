package net.xevianlight.aphelion.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.xevianlight.aphelion.Aphelion;

public record ClientPlayerStateUpdatePacket(boolean oxygen, float gravity, float temp) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientPlayerStateUpdatePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Aphelion.MOD_ID, "player_state_update"));

    public static final StreamCodec<ByteBuf, ClientPlayerStateUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            ClientPlayerStateUpdatePacket::oxygen,
            ByteBufCodecs.FLOAT,
            ClientPlayerStateUpdatePacket::gravity,
            ByteBufCodecs.FLOAT,
            ClientPlayerStateUpdatePacket::temp,
            ClientPlayerStateUpdatePacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


}
