package net.xevianlight.aphelion.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.xevianlight.aphelion.block.custom.base.BasicEntityBlock;
import net.xevianlight.aphelion.block.entity.custom.GravityTestBlockEntity;
import net.xevianlight.aphelion.block.entity.custom.OxygenTestBlockEntity;
import net.xevianlight.aphelion.block.entity.custom.TestBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GravityTestBlock extends BasicEntityBlock {

    public GravityTestBlock(Properties properties) {super(properties, true);}

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {return null;}

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GravityTestBlockEntity(blockPos, blockState);
    }

    public static Properties getProperties() {return Properties.of();}

    @Override
    protected void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof GravityTestBlockEntity BE) {
                BE.removeGravityArea();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof GravityTestBlockEntity testBlockEntity) {
            serverPlayer.openMenu(new SimpleMenuProvider(testBlockEntity, Component.literal("Gravity Test Block")), pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
