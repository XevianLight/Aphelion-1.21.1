package net.xevianlight.aphelion.block.custom.base;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.xevianlight.aphelion.core.init.ModDimensions;
import net.xevianlight.aphelion.core.saveddata.SpacePartitionSavedData;
import net.xevianlight.aphelion.core.saveddata.types.PartitionData;

import javax.annotation.Nullable;

public abstract class StationEngineBlockEntity extends BlockEntity implements TickableBlockEntity {

    private boolean isInitialized = false;
    private @Nullable PartitionData data;

    /**
     * The travel speed in AU/tick.
     */
    public abstract double getTravelSpeed();

    protected StationEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void clientTick(ClientLevel level, long time, BlockState state, BlockPos pos) {

    }

    /**
     * Handles station travel logic for this engine.
     *
     * <p>If the associated station is currently traveling, this method advances
     * its movement using the value returned by {@link #getTravelSpeed()}.</p>
     *
     * <p>Subclasses may override this method to add additional server-side behavior.
     * When doing so, {@code super.serverTick(...)} should be called to preserve
     * the default travel logic.</p>
     *
     * <p>This method is invoked once per server tick.</p>
     *
     * @param level the server level the block entity exists in
     * @param time the current tick time offset used for scheduling
     * @param state the current block state
     * @param pos the world position of the block entity
     */
    @Override
    public void serverTick(ServerLevel level, long time, BlockState state, BlockPos pos) {
        double speed = getTravelSpeed();
        if (data != null) {
            if (data.isTraveling()) {
                data.travel(speed);
            }
        }
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void firstTick(Level level, BlockState state, BlockPos pos) {
        if (level.isClientSide()) return;
        if (level instanceof ServerLevel serverLevel) {
            if (serverLevel.dimension() == ModDimensions.SPACE) {
                data = SpacePartitionSavedData.get(serverLevel).getDataForBlockPos(pos);
            }
        }
        isInitialized = true;
    }
}
