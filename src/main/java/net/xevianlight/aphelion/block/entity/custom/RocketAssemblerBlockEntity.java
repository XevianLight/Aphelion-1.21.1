package net.xevianlight.aphelion.block.entity.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.xevianlight.aphelion.block.custom.base.TickableBlockEntity;
import net.xevianlight.aphelion.core.init.ModBlockEntities;
import net.xevianlight.aphelion.util.RocketStructure;
import org.apache.commons.lang3.NotImplementedException;

public class RocketAssemblerBlockEntity extends BlockEntity implements TickableBlockEntity {

    public boolean isInitialized;
    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    public RocketAssemblerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ROCKET_ASSEMBLER_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void tick(Level level1, BlockPos blockPos, BlockState blockState) {

    }

    public boolean getPlatform() {
        // TODO
        throw new NotImplementedException();
    }

    public RocketStructure scan() {
        // TODO
        throw new NotImplementedException();
    }

    @Override
    public void tick(Level entityLevel, long time, BlockState blockState, BlockPos pos) {

    }

    @Override
    public void clientTick(ClientLevel level, long time, BlockState state, BlockPos pos) {

    }

    @Override
    public void serverTick(ServerLevel level, long time, BlockState state, BlockPos pos) {

    }

    @Override
    public void firstTick(Level level, BlockState state, BlockPos pos) {
        this.isInitialized = true;
    }
}
