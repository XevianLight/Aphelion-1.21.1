package net.xevianlight.aphelion.block.entity.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.xevianlight.aphelion.Aphelion;
import net.xevianlight.aphelion.block.custom.PipeTestBlock;
import net.xevianlight.aphelion.block.custom.base.TickableBlockEntity;
import net.xevianlight.aphelion.core.init.ModBlockEntities;
import net.xevianlight.aphelion.core.init.ModBlocks;
import net.xevianlight.aphelion.util.FloodFill3D;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// TODO: Rearrange this WHOOLE fucking thing into like, 10 different files for the actual pipes

public class PipeTestBlockEntity extends BlockEntity implements TickableBlockEntity {
    public @Nullable PipeGraph graph = null;
    public final Map<Direction, @Nullable PipeAttachment> attachments = new HashMap<>();
    public final Map<Direction, @Nullable PipeOutput> outputs = new HashMap<>();

    public PipeTestBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.PIPE_TEST_BLOCK_ENTITY.get(), pos, blockState);
    }

    @Override
    public void clientTick(ClientLevel level, long time, BlockState state, BlockPos pos) {

    }

    @Override
    public void serverTick(ServerLevel level, long time, BlockState state, BlockPos pos) {
        if (this.graph == null) initGraph(level, pos);
        // TODO: Call this as little as necessary
        makeOutputs(level, state, pos);

        for (Direction dir : Direction.values()) {
            PipeAttachment attachment = attachments.get(dir);
            if (attachment != null) {
                attachment.tick(level, state, pos, dir, graph);
            }
        }
    }

    @Override
    public void firstTick(Level level, BlockState state, BlockPos pos) {

    }

    private void addOutput(Direction dir, PipeOutput output) {
        outputs.put(dir, output);
        if (graph != null) graph.outputs.add(output);
    }

    private void removeOutput(Direction dir) {
        PipeOutput old = outputs.get(dir);
        if (graph != null) graph.outputs.remove(old);
        outputs.remove(dir);
    }

    private boolean canOutputTo(Level level, BlockPos pos, Direction accessSide) {
        return level.getCapability(Capabilities.ItemHandler.BLOCK, pos, accessSide) != null;
    }

    protected void makeOutputs(ServerLevel level, BlockState state, BlockPos pos) {
        BlockPos.MutableBlockPos neighbor = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.values()) {
            neighbor.setWithOffset(pos, dir);

            if (canOutputTo(level, neighbor, dir.getOpposite()) && attachments.get(dir) == null && outputs.get(dir) == null) {
                addOutput(dir, new BasicItemOutput(level, pos, dir));
            }

            if (!(canOutputTo(level, neighbor, dir.getOpposite()) && attachments.get(dir) == null) && outputs.get(dir) != null) {
                removeOutput(dir);
            }
        }
    }

    //TODO: move all these classes somewhere else
    public interface PipeInput {
        void tick(ServerLevel level, BlockState state, BlockPos pos, Direction facingDirection, PipeGraph graph);
    }

    public interface PipeOutput {
        /// @return Rejected items
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
            pipes.add(pipe);
            pipe.graph = this;
            for (PipeOutput output : pipe.outputs.values()) {
                if (output != null) this.outputs.add(output);
            }
        }

        /// Called whenever a pipe is removed from a graph, or when a new graph comes across an old one.
        public void invalidate() {
            for (PipeTestBlockEntity pipe : pipes) {
                pipe.graph = null;
                for (PipeOutput output : pipe.outputs.values()) {
                    if (output != null) this.outputs.remove(output);
                }
            }
            this.isInvalid = true;
        }
    }

    public static abstract class PipeAttachment {
        PipeAttachment(ServerLevel level, BlockPos pos, Direction facingDirection) {}

        //TODO: put in the right interface for a render function
        void render() {};

        public void tick(ServerLevel level, BlockState state, BlockPos pos, Direction facingDirection, PipeGraph graph) {}
    }

    public boolean hasAttachment(Direction direction) {
        return attachments.get(direction) != null;
    }

    // Simplest implementation I can think of
    public static void initGraph(Level level, BlockPos pos) {
        Aphelion.LOGGER.info("Init graph from {}", pos);
        if (!level.getBlockState(pos).is(ModBlocks.PIPE_TEST_BLOCK.get())) return;
        Set<BlockPos> pipes = FloodFill3D.run(
                level, pos, 1000,
                (var v1, var v2, var state, var v4, var v5, var v6) -> state.is(ModBlocks.PIPE_TEST_BLOCK.get()),
                false);

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

    public void setAttachment(Direction side, PipeAttachment attachment) {
        attachments.put(side, attachment);
    }

    public boolean trySetAttachment(Direction side, PipeAttachment attachment) {
        if (hasAttachment(side)) return false;
        setAttachment(side, attachment);
        return true;
    }

    public static boolean isAttachmentItem(ItemStack stack) {
        //TODO: add actual attachment items instead of just stone
        return stack.is(Item.byId(1));
    }

    public static class BasicItemExtractAttachment extends PipeAttachment implements PipeInput {

        BlockCapabilityCache<IItemHandler, @Nullable Direction> capabilityCache;

        BasicItemExtractAttachment(ServerLevel level, BlockPos pos, Direction facingDirection) {
            super(level, pos, facingDirection);

            capabilityCache = BlockCapabilityCache.create(
                    Capabilities.ItemHandler.BLOCK,
                    level,
                    pos.relative(facingDirection),
                    facingDirection.getOpposite()
            );
        }

        @Override
        public void render() {

        }

        @Override
        public void tick(ServerLevel level, BlockState state, BlockPos pos, Direction facingDirection, PipeGraph graph) {
            // do an extract and distribute
            IItemHandler container = capabilityCache.getCapability();
            if (container == null) return;

            final int EXTRACT_PER_TICK = 4;
            int to_extract = EXTRACT_PER_TICK;

            int extract_slot_id = container.getSlots() - 1;
            while (extract_slot_id >= 0 && container.getStackInSlot(extract_slot_id).isEmpty()) extract_slot_id--;
            if (extract_slot_id == -1) return;


            // Do a simulated extract, then run through every output side on the graph and do real inserts.
            // By the end, remove as many items as we successfully inserted with a real extract
            ItemStack to_distribute = container.extractItem(extract_slot_id, to_extract, true);
            int extracted_amount = to_distribute.getCount();

            to_distribute = graph.insertItem(to_distribute, false);

            int distributed_amount = extracted_amount;
            if (!to_distribute.isEmpty()) {
                distributed_amount = extracted_amount - to_distribute.getCount();
            }
            container.extractItem(extract_slot_id, distributed_amount, false);
        }
    }

    public static class BasicItemOutput implements PipeOutput {

        BlockCapabilityCache<IItemHandler, @Nullable Direction> capabilityCache;

        BasicItemOutput(ServerLevel level, BlockPos pos, Direction facingDirection) {
            capabilityCache = BlockCapabilityCache.create(
                    Capabilities.ItemHandler.BLOCK,
                    level,
                    pos.relative(facingDirection),
                    facingDirection.getOpposite()
            );
        }

        @Override
        public ItemStack insertItem(ItemStack stack, boolean simulate) {
            ItemStack toInsert = stack.copy();

            IItemHandler container = capabilityCache.getCapability();
            if (container == null) return toInsert;

            for (int insertIndex = 0; insertIndex < container.getSlots(); insertIndex++) {
                if (toInsert.isEmpty()) break;

                toInsert = container.insertItem(insertIndex, toInsert, simulate);
            }

            return toInsert;
        }
    }

    // Server only
    public  PipeAttachment getAttachmentForItem(ItemStack stack, Direction side) {
        if (level.isClientSide()) throw new RuntimeException("Cannot get attachment item on client side!");
        return new BasicItemExtractAttachment((ServerLevel) level, getBlockPos(), side);
    }

    /// Called from pipe test block's useItemOn
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, PipeTestBlock block) {
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
        Vec3 relativePos = hitResult.getLocation().subtract(pos.getCenter());
        Direction pipeSide = Direction.getNearest(relativePos);

        if (isAttachmentItem(stack)) {
            boolean success = trySetAttachment(pipeSide, getAttachmentForItem(stack, pipeSide));

            return success ? ItemInteractionResult.SUCCESS : ItemInteractionResult.FAIL;
        } else {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION; // goes through with whatever other interaction (placing a block, etc)
        }
    }
}
