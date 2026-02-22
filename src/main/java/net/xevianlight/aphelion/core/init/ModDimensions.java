package net.xevianlight.aphelion.core.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.xevianlight.aphelion.Aphelion;

public final class ModDimensions {
    public static final ResourceKey<Level> SPACE = ResourceKey.create(Registries.DIMENSION, Aphelion.id("space"));
}
