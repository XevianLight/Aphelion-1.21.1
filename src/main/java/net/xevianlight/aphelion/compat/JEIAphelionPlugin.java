package net.xevianlight.aphelion.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.xevianlight.aphelion.Aphelion;
import net.xevianlight.aphelion.core.init.ModBlocks;
import net.xevianlight.aphelion.recipe.ElectricArcFurnaceRecipe;
import net.xevianlight.aphelion.recipe.ModRecipes;
import net.xevianlight.aphelion.screen.ElectricArcFurnaceScreen;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIAphelionPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Aphelion.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ElectricArcFurnaceRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()
        ));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;

        RecipeManager recipeManager = level.getRecipeManager();

        List<ElectricArcFurnaceJeiRecipe> recipes = new ArrayList<>();

        for (var holder : recipeManager.getAllRecipesFor(ModRecipes.ELECTRIC_ARC_FURNACE_RECIPE_TYPE.get())) {
            recipes.add(ElectricArcFurnaceJeiRecipe.fromAlloying(holder.value()));
        }

        for (var holder : recipeManager.getAllRecipesFor(RecipeType.BLASTING)) {
            recipes.add(ElectricArcFurnaceJeiRecipe.fromBlasting(holder.value()));
        }

//        List<ElectricArcFurnaceRecipe> electricArcFurnaceRecipes = recipeManager
//                .getAllRecipesFor(ModRecipes.ELECTRIC_ARC_FURNACE_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(ElectricArcFurnaceRecipeCategory.ELECTRIC_ARC_FURNACE_RECIPE_TYPE, recipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ElectricArcFurnaceScreen.class, 88, 35, 24, 16,
                ElectricArcFurnaceRecipeCategory.ELECTRIC_ARC_FURNACE_RECIPE_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.ELECTRIC_ARC_FURNACE.get()), ElectricArcFurnaceRecipeCategory.ELECTRIC_ARC_FURNACE_RECIPE_TYPE
        );
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.ELECTRIC_ARC_FURNACE.get()),
                RecipeTypes.BLASTING
        );
    }
}
