package net.xevianlight.aphelion.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record ElectricArcFurnaceRecipe(Ingredient base, int baseCount, Ingredient alloy, int alloyCount, int cookTime, ItemStack output) implements Recipe<ElectricArcFurnaceRecipeInput> {

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(base);
        list.add(alloy);
        return list;
    }

    @Override
    public boolean matches(ElectricArcFurnaceRecipeInput electricArcFurnaceRecipeInput, Level level) {
        if (level.isClientSide()) { return false; }

        return base.test(electricArcFurnaceRecipeInput.getItem(0)) && alloy.test(electricArcFurnaceRecipeInput.getItem(1));
    }

    @Override
    public ItemStack assemble(ElectricArcFurnaceRecipeInput electricArcFurnaceRecipeInput, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.EAF_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.ELECTRIC_ARC_FURNACE_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ElectricArcFurnaceRecipe> {

        public static final MapCodec<ElectricArcFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("base").forGetter(ElectricArcFurnaceRecipe::base),
                Codec.INT.optionalFieldOf("base_count", 1).forGetter(ElectricArcFurnaceRecipe::baseCount),
                Ingredient.CODEC_NONEMPTY.fieldOf("alloy").forGetter(ElectricArcFurnaceRecipe::alloy),
                Codec.INT.optionalFieldOf("alloy_count", 1).forGetter(ElectricArcFurnaceRecipe::alloyCount),
                Codec.INT.optionalFieldOf("cook_time", 100).forGetter(ElectricArcFurnaceRecipe::cookTime),
                ItemStack.CODEC.fieldOf("result").forGetter(ElectricArcFurnaceRecipe::output)
        ).apply(inst, ElectricArcFurnaceRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ElectricArcFurnaceRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, ElectricArcFurnaceRecipe::base,
                        ByteBufCodecs.VAR_INT, ElectricArcFurnaceRecipe::baseCount,
                        Ingredient.CONTENTS_STREAM_CODEC, ElectricArcFurnaceRecipe::alloy,
                        ByteBufCodecs.VAR_INT, ElectricArcFurnaceRecipe::alloyCount,
                        ByteBufCodecs.VAR_INT, ElectricArcFurnaceRecipe::cookTime,
                        ItemStack.STREAM_CODEC, ElectricArcFurnaceRecipe::output,
                        ElectricArcFurnaceRecipe::new
                );

        @Override
        public MapCodec<ElectricArcFurnaceRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ElectricArcFurnaceRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
