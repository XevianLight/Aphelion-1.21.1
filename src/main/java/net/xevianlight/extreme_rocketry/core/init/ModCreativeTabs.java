package net.xevianlight.extreme_rocketry.core.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.xevianlight.extreme_rocketry.ExtremeRocketry;

import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExtremeRocketry.MOD_ID);

    public static final Supplier<CreativeModeTab> EXTREME_ROCKETRY_ITEMS_TAB = CREATIVE_MODE_TAB.register("extreme_rocketry_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.TEST_ITEM.get()))
                    .title(Component.translatable("creativetab.extreme_rocketry.extreme_rocketry_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.TEST_ITEM);
                    }).build());

    public static final Supplier<CreativeModeTab> EXTREME_ROCKETRY_BLOCKS_TAB = CREATIVE_MODE_TAB.register("extreme_rocketry_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.TEST_BLOCK.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(ExtremeRocketry.MOD_ID, "extreme_rocketry_items_tab"))
                    .title(Component.translatable("creativetab.extreme_rocketry.extreme_rocketry_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.TEST_BLOCK);
                    }).build());
}
