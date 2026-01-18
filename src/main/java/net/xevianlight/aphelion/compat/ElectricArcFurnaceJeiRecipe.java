package net.xevianlight.aphelion.compat;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.xevianlight.aphelion.recipe.ElectricArcFurnaceRecipe;
import org.jetbrains.annotations.Nullable;

public record ElectricArcFurnaceJeiRecipe (
        Ingredient base,
        int baseCount,
        @Nullable Ingredient alloy,
        int secondaryCount,
        ItemStack result,
        int cookTime
){
    public boolean isAlloying() {
        return alloy != null;
    }

    public static ElectricArcFurnaceJeiRecipe fromAlloying(ElectricArcFurnaceRecipe r) {
        return new ElectricArcFurnaceJeiRecipe(
                r.base(),
                r.baseCount(),
                r.alloy(),
                r.alloyCount(),
                r.getResultItem(null),
                r.cookTime()
        );
    }

    public static ElectricArcFurnaceJeiRecipe fromBlasting(BlastingRecipe r) {
        Ingredient in = r.getIngredients().get(0);
        return new ElectricArcFurnaceJeiRecipe(
                in,
                1,
                null,
                1,
                r.getResultItem(null),
                r.getCookingTime()
        );
    }
}
