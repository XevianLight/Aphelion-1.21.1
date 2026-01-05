package net.xevianlight.extreme_rocketry.core.init;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.xevianlight.extreme_rocketry.ExtremeRocketry;
import net.xevianlight.extreme_rocketry.block.TestBlock;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ExtremeRocketry.MOD_ID);

    public static final  DeferredBlock<Block> TEST_BLOCK = BLOCKS.register("test_block", () -> new TestBlock(TestBlock.getProperties()));
}
