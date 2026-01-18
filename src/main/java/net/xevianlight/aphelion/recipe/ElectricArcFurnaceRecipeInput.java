package net.xevianlight.aphelion.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record ElectricArcFurnaceRecipeInput (ItemStack base, ItemStack alloy) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        return switch (i) {
            case 0 -> base;
            case 1 -> alloy;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
