package net.xevianlight.aphelion.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.xevianlight.aphelion.Aphelion;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Aphelion.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Aphelion.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ElectricArcFurnaceRecipe>> EAF_SERIALIZER =
            SERIALIZERS.register("alloying", ElectricArcFurnaceRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<ElectricArcFurnaceRecipe>> ELECTRIC_ARC_FURNACE_RECIPE_TYPE =
            TYPES.register("alloying", () -> new RecipeType<ElectricArcFurnaceRecipe>() {
                @Override
                public String toString() {
                    return "alloying";
                }
            });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
