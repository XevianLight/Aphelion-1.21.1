package net.xevianlight.extreme_rocketry.core.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.xevianlight.extreme_rocketry.ExtremeRocketry;
import net.xevianlight.extreme_rocketry.block.TestBlock;
import net.xevianlight.extreme_rocketry.item.TestItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtremeRocketry.MOD_ID);

    public static final DeferredItem<Item> TEST_ITEM = ITEMS.register("test_item", TestItem::new);


    // Block Items

    public static final DeferredItem<BlockItem> TEST_BLOCK = ITEMS.register("test_block", () -> new BlockItem(ModBlocks.TEST_BLOCK.get(), TestBlock.getItemProperties()));
}
