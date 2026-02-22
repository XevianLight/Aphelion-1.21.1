package net.xevianlight.aphelion.block.custom.base;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.xevianlight.aphelion.core.saveddata.types.PartitionData;
import net.xevianlight.aphelion.util.Constants;

public class StationRocketEngineBlockEntity extends StationEngineBlockEntity {

    /// Seconds to travel 1 AU
    private final double SECONDS_PER_AU = 60;
    /// AU per tick
    private final double SPEED = 1/(SECONDS_PER_AU*20);

    @Override
    public double getTravelSpeed() {
        return SPEED;
    }

    protected StationRocketEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        // TODO change type to ModBlockEntities.STATION_ROCKET_ENGINE_BLOCK_ENTITY.get()
        super(type, pos, blockState);
    }

    @Override
    public void serverTick(ServerLevel level, long time, BlockState state, BlockPos pos) {
        super.serverTick(level, time, state, pos); // IMPORTANT!!!
    }
}
