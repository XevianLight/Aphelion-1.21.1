package net.xevianlight.aphelion.block.entity.custom;

import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.common.Mod;
import net.xevianlight.aphelion.Aphelion;
import net.xevianlight.aphelion.block.custom.PipeTestBlock;
import net.xevianlight.aphelion.block.custom.base.TickableBlockEntity;
import net.xevianlight.aphelion.core.init.ModBlockEntities;
import net.xevianlight.aphelion.core.init.ModBlocks;
import net.xevianlight.aphelion.util.FloodFill3D;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PipeTestBlockEntity extends BlockEntity implements TickableBlockEntity {
    public PipeTestBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.PIPE_TEST_BLOCK_ENTITY.get(), pos, blockState);
    }

    @Override
    public void clientTick(ClientLevel level, long time, BlockState state, BlockPos pos) {

    }

    @Override
    public void serverTick(ServerLevel level, long time, BlockState state, BlockPos pos) {
        if (this.graph == null) initGraph(level, pos);
    }

    @Override
    public void firstTick(Level level, BlockState state, BlockPos pos) {

    }

    //TODO: move all this somewhere else
    public interface PipeInput {
        void tick(Level level, BlockState state, BlockPos pos, Direction facingDirection);
    }

    public interface PipeOutput {
        ItemStack insertItem(ItemStack stack, boolean simulate);
    }

    public static class PipeGraph {
        boolean isInvalid = false;
        public List<PipeTestBlockEntity> pipes = new ArrayList<>();
        public Set<PipeOutput> outputs = new HashSet<>();

        public ItemStack insertItem(ItemStack stack, boolean simulate) {
            if (isInvalid) return stack;
            for (PipeOutput output : outputs) {
                stack = output.insertItem(stack, simulate);
                if (stack.isEmpty()) break;
            }
            return stack;
        }

        public void addPipe(PipeTestBlockEntity pipe) {
            //TODO: add outputs the pipe has already if applicable
            pipes.add(pipe);
            pipe.graph = this;
        }

        /// Called whenever a pipe is removed from a graph, or when a new graph comes across an old one.
        public void invalidate() {
            for (PipeTestBlockEntity pipe : pipes) {
                pipe.graph = null;
            }
            this.isInvalid = true;
        }
    }

    public @Nullable PipeGraph graph = null;

    // Simplest implementation I can think of
    public static void initGraph(Level level, BlockPos pos) {
        Aphelion.LOGGER.info("Init graph from {}", pos);
        if (!level.getBlockState(pos).is(ModBlocks.PIPE_TEST_BLOCK.get())) return;
        Set<BlockPos> pipes = FloodFill3D.run(level, pos, 1000, (var v1, var v2, var state, var v4, var v5, var v6) -> state.is(ModBlocks.PIPE_TEST_BLOCK.get()), false);

        Aphelion.LOGGER.info("Got {} pipes", pipes.size());
        PipeGraph graph = new PipeGraph();
        for (BlockPos pipePos : pipes) {
            BlockEntity BE = level.getBlockEntity(pipePos);
            if (BE instanceof PipeTestBlockEntity pipe) {
                // Invalidate any old graphs
                if (pipe.graph != null) {
                    pipe.graph.invalidate();
                }

                graph.addPipe(pipe);
            }
        }
    }
}
