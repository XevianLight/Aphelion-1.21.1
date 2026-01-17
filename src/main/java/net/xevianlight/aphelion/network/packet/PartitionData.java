package net.xevianlight.aphelion.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.xevianlight.aphelion.Aphelion;

public record PartitionData (String id) implements CustomPacketPayload {
    public static final Type<PartitionData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Aphelion.MOD_ID, "partition_data"));

    public static final StreamCodec<ByteBuf, PartitionData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            PartitionData::id,

            PartitionData::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
