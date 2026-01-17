package net.xevianlight.aphelion.client.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.xevianlight.aphelion.Aphelion;

import java.util.HashMap;
import java.util.Map;

public final class DimensionRendererCache {

    public static final Map<ResourceLocation, DimensionRenderer> RENDERERS = new HashMap<>();

    public static final DimensionRenderer DEFAULT = new DimensionRenderer(
            ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Aphelion.MOD_ID, "space")),
            false,
            false,
            false,
            false,
            true,
            14180147,
            0,
            false,
            63,
            1
    );

    public static void registerPlanetRenderers(Map<ResourceLocation, DimensionRenderer> renderers) {
        RENDERERS.clear();
        RENDERERS.putAll(renderers);

    }

    public static DimensionRenderer getOrDefault(ResourceLocation id) {
        return RENDERERS.getOrDefault(id, DEFAULT);
    }
}
