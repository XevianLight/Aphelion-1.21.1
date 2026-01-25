package net.xevianlight.aphelion.planet;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record StarSystem(
        int temp
) {
    public static final Codec<StarSystem> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("dimension").forGetter(StarSystem::temp)
    ).apply(inst, StarSystem::new));
}
