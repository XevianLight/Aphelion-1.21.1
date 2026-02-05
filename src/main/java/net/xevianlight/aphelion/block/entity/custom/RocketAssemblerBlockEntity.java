package net.xevianlight.aphelion.block.entity.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.xevianlight.aphelion.block.custom.base.TickableBlockEntity;
import net.xevianlight.aphelion.core.init.ModBlockEntities;
import net.xevianlight.aphelion.util.ModTags;
import net.xevianlight.aphelion.util.RocketStructure;
import org.apache.commons.lang3.NotImplementedException;

public class RocketAssemblerBlockEntity extends BlockEntity implements TickableBlockEntity {

    Direction facing;
    BlockPos padScanStart = BlockPos.ZERO;

    public boolean isInitialized;
    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    public RocketAssemblerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ROCKET_ASSEMBLER_BLOCK_ENTITY.get(), pos, blockState);
    }

    public record PadInfo(BlockPos min, BlockPos max, int size) {};

    public PadInfo getPlatform() {
        // TODO
        int y = this.padScanStart.getY();
        BlockPos start = this.padScanStart;

        if (level == null) return null;
        if (!isPad(level.getBlockState(start))) return null;

        int minX = start.getX();
        while (isPad(level.getBlockState(new BlockPos(minX - 1, y, start.getZ())))) minX--;

        // Find maxX by walking east
        int maxX = start.getX();
        while (isPad(level.getBlockState(new BlockPos(maxX + 1, y, start.getZ())))) maxX++;

        // Find minZ by walking north
        int minZ = start.getZ();
        while (isPad(level.getBlockState(new BlockPos(start.getX(), y, minZ - 1)))) minZ--;

        // Find maxZ by walking south
        int maxZ = start.getZ();
        while (isPad(level.getBlockState(new BlockPos(start.getX(), y, maxZ + 1)))) maxZ++;

        int width  = (maxX - minX) + 1;
        int length = (maxZ - minZ) + 1;

        // Must be square
        if (width != length) return null;

        // Verify the entire rectangle is filled with pad blocks
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (!isPad(level.getBlockState(new BlockPos(x, y, z)))) return null;
            }
        }

        return new PadInfo(new BlockPos(minX, y, minZ), new BlockPos(maxX, y, maxZ), width);
    }

    public RocketStructure scan() {
        // TODO
        throw new NotImplementedException();
    }

    @Override
    public void clientTick(ClientLevel level, long time, BlockState state, BlockPos pos) {

    }

    @Override
    public void serverTick(ServerLevel level, long time, BlockState state, BlockPos pos) {

    }

    @Override
    public void firstTick(Level level, BlockState state, BlockPos pos) {
        facing = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        padScanStart = getBlockPos().mutable().below().relative(facing.getOpposite());
        this.isInitialized = true;
    }


    private static boolean isPad(BlockState s) {
        return s.is(ModTags.Blocks.LAUNCH_PAD); // or s.getBlock() == ModBlocks.PAD.get()
    }
}
