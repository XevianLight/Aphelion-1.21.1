package net.xevianlight.aphelion.compat;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.xevianlight.aphelion.Aphelion;
import net.xevianlight.aphelion.core.init.ModBlocks;
import net.xevianlight.aphelion.recipe.ElectricArcFurnaceRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElectricArcFurnaceRecipeCategory implements IRecipeCategory<ElectricArcFurnaceJeiRecipe> {

    private final Int2ObjectMap<IDrawableAnimated> arrows = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectMap<IDrawableAnimated> energyBars = new Int2ObjectOpenHashMap<>();
    private final IGuiHelper helper;

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Aphelion.MOD_ID, "alloying");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Aphelion.MOD_ID,
            "textures/gui/electric_arc_furnace/jei.png");

    public static final RecipeType<ElectricArcFurnaceJeiRecipe> ELECTRIC_ARC_FURNACE_RECIPE_TYPE =
            new RecipeType<>(UID, ElectricArcFurnaceJeiRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ElectricArcFurnaceRecipeCategory(IGuiHelper helper) {
        this.helper = helper;
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 82);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.ELECTRIC_ARC_FURNACE.get()));
    }

    private IDrawableAnimated getArrow(int cookTime) {
        return arrows.computeIfAbsent(cookTime, t -> {
            IDrawableStatic arrowStatic = helper.createDrawable(TEXTURE, 176, 0, 24, 15);
            return helper.createAnimatedDrawable(arrowStatic, t, IDrawableAnimated.StartDirection.LEFT, false);
        });
    }

    private IDrawableAnimated getEnergyBar() {
        return arrows.computeIfAbsent(200, t -> {
            IDrawableStatic arrowStatic = helper.createDrawable(TEXTURE, 176, 15, 14, 42);
            return helper.createAnimatedDrawable(arrowStatic, t, IDrawableAnimated.StartDirection.TOP, true);
        });
    }

    @Override
    public void draw(ElectricArcFurnaceJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        getArrow(recipe.cookTime()).draw(guiGraphics, 89, 35);
        getEnergyBar().draw(guiGraphics, 9, 9);
    }

    @Override
    public RecipeType<ElectricArcFurnaceJeiRecipe> getRecipeType() {
        return ELECTRIC_ARC_FURNACE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.aphelion.electric_arc_furnace");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ElectricArcFurnaceJeiRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 63, 35).addIngredients(recipe.base());
        if(recipe.alloy() != null) {
            builder.addSlot(RecipeIngredientRole.INPUT, 40, 35).addIngredients(recipe.alloy());
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 125, 35).addItemStack(recipe.result());
    }
}
