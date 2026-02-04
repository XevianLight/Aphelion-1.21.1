package net.xevianlight.aphelion.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.xevianlight.aphelion.block.custom.base.BasicEntityBlock;
import net.xevianlight.aphelion.block.entity.custom.OxygenTestBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OxygenTestBlock extends BasicEntityBlock {

    public OxygenTestBlock(Properties properties) {
        super(properties, true);
    }

    public static final MapCodec<OxygenTestBlock> CODEC = simpleCodec(OxygenTestBlock::new);

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new OxygenTestBlockEntity(blockPos, blockState);
    }

    public static Properties getProperties() {
        return Properties.of();
    }

    @Override
    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity= level.getBlockEntity(pos);
            if (blockEntity instanceof OxygenTestBlockEntity oxygenTestBlockEntity) {
                oxygenTestBlockEntity.removeEnclosed();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
