package net.xevianlight.aphelion.block.custom.base;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface TickableBlockEntity {
    void tick(Level entityLevel, long time, BlockState blockState, BlockPos pos);

    void clientTick(ClientLevel level, long time, BlockState state, BlockPos pos);

    void serverTick(ServerLevel level, long time, BlockState state, BlockPos pos);

    default boolean isInitialized() {
        return true;
    };

    void firstTick(Level level, BlockState state, BlockPos pos);

    default void onRemoved() {}
}
