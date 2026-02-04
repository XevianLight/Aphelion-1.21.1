package net.xevianlight.aphelion.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.xevianlight.aphelion.block.custom.base.BasicHorizontalEntityBlock;
import net.xevianlight.aphelion.block.entity.custom.RocketAssemblerBlockEntity;
import org.jetbrains.annotations.Nullable;

public class RocketAssemblerBlock extends BasicHorizontalEntityBlock {

    public RocketAssemblerBlock(Properties properties) {
        super(properties, true);
    }

    public static final MapCodec<RocketAssemblerBlock> CODEC = simpleCodec(RocketAssemblerBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public static Properties getProperties() {
        return Properties
                .of()
                .sound(SoundType.METAL)
                .destroyTime(2f)
                .explosionResistance(10f)
                .requiresCorrectToolForDrops();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RocketAssemblerBlockEntity(blockPos, blockState);
    }
}
