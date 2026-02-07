package net.xevianlight.aphelion.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.xevianlight.aphelion.block.entity.custom.GravityTestBlockEntity;
import net.xevianlight.aphelion.network.packet.UpdateGravityTestBlockPacket;

public class UpdateGravityTestBlockHandler {
    public static void handleDataOnMain(UpdateGravityTestBlockPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            BlockPos pos = packet.pos();
            float radius = packet.radius();
            float strength = packet.strength();

            Level level = context.player().level();
            if (level.getBlockEntity(pos) instanceof GravityTestBlockEntity blockEntity) {
                blockEntity.setRadius(radius);
                blockEntity.setStrength(strength);
                blockEntity.sendUpdate();
                level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), Block.UPDATE_ALL);
            }
        });
    }
}
