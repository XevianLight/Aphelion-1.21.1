package net.xevianlight.aphelion.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.xevianlight.aphelion.Aphelion;

public record UpdateGravityTestBlockPacket(BlockPos pos, float radius, float strength) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<UpdateGravityTestBlockPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Aphelion.MOD_ID, "update_oxygen_test_block"));

    public static final StreamCodec<ByteBuf, UpdateGravityTestBlockPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            UpdateGravityTestBlockPacket::pos,
            ByteBufCodecs.FLOAT,
            UpdateGravityTestBlockPacket::radius,
            ByteBufCodecs.FLOAT,
            UpdateGravityTestBlockPacket::strength,
            UpdateGravityTestBlockPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
