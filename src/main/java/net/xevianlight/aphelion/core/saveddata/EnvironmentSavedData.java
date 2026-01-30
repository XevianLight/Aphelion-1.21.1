package net.xevianlight.aphelion.core.saveddata;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.xevianlight.aphelion.Aphelion;
import net.xevianlight.aphelion.core.saveddata.types.EnvironmentData;
import net.xevianlight.aphelion.planet.Planet;
import net.xevianlight.aphelion.planet.PlanetCache;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

/**
 * Pattern:
 *  - World-level SavedData
 *  - Outer map keyed by section (chunkX, sectionY, chunkZ) packed into a long
 *  - Inner map keyed by localIndex (0..4095) -> packed int env value
 *
 * Sparse by design: blocks not present in the inner map are implicitly "default environment".
 */
public class EnvironmentSavedData extends SavedData {

    private final Long2IntOpenHashMap envData = new Long2IntOpenHashMap();

    private static final String NAME = "aphelion_environment";

    public static EnvironmentSavedData create() {
        return new EnvironmentSavedData();
    }

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        int size = envData.size();
        long[] positions = new long[size];
        int[] data = new int[size];

        int i = 0;
        for (var e : envData.long2IntEntrySet()) {
            positions[i] = e.getLongKey();
            data[i] = e.getIntValue();
            i++;
        }

        tag.putLongArray("Position", positions);
        tag.putIntArray("Value", data);

        return tag;
    }

    public static EnvironmentSavedData load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        EnvironmentSavedData data = create();

        if (!tag.contains("Position", Tag.TAG_LONG_ARRAY) || !tag.contains("Value", Tag.TAG_INT_ARRAY)) { return data; }

        long[] positions = tag.getLongArray("Positions");
        int[] values = tag.getIntArray("Value");

        int length = Math.min(positions.length, values.length);

        data.envData.ensureCapacity(length);

        for (int i = 0; i < length; i++) {
            data.envData.put(positions[i], values[i]);
        }

        return data;
    }

    public EnvironmentData getDataForPosition(Level level, BlockPos pos) {
        Planet planet = PlanetCache.getByDimensionOrNull(level.dimension());
        int packedDefault;

        if (planet == null) {
            packedDefault = EnvironmentData.DEFAULT_PACKED;
        } else {
            EnvironmentData planetData = new EnvironmentData(planet.oxygen(), EnvironmentData.DEFAULT_TEMPERATURE, (short) planet.gravity());
            packedDefault = planetData.pack();
        }
        int packed = envData.getOrDefault(pos.asLong(),packedDefault);
        return EnvironmentData.unpack(packed);
    }

    public void setDataForPosition(Level level, BlockPos pos, EnvironmentData data) {
        putOrRemove(pos.asLong(), data.pack());
    }

    public boolean hasOxygen(Level level, BlockPos pos) {
        var data = getDataForPosition(level, pos);
        return data.hasOxygen();
    }

    public void setOxygen(Level level, BlockPos pos, boolean value) {
        var data = getDataForPosition(level, pos);
        data.setOxygen(value);
        Aphelion.LOGGER.info("Set oxygen for {} to {}", pos, value);
        putOrRemove(pos.asLong(), data.pack());
    }

    public void setOxygen(Level level, Collection<BlockPos> positions, boolean value) {
        for (BlockPos pos : positions) {
            setOxygen(level, pos, value);
        }
    }

    public void resetOxygen(Level level, BlockPos pos) {
        var data = getDataForPosition(level, pos);
        data.setOxygen(EnvironmentData.DEFAULT_OXYGEN);
        putOrRemove(pos.asLong(), data.pack());
    }

    public float getGravity(Level level, BlockPos pos) {
        var data = getDataForPosition(level, pos);
        return data.getGravity();
    }

    public void setGravity(Level level, BlockPos pos, float value) {
        var data = getDataForPosition(level, pos);
        data.setGravity(value);
        putOrRemove(pos.asLong(), data.pack());
    }

    public void setGravity(Level level, Collection<BlockPos> positions, float value) {
        for (BlockPos pos : positions) {
            setGravity(level, pos, value);
        }
    }

    public short getTemperature(Level level, BlockPos pos) {
        var data = getDataForPosition(level, pos);
        return data.getTemperature();
    }

    public void setTemperature(Level level, BlockPos pos, short value) {
        var data = getDataForPosition(level, pos);
        data.setTemperature(value);
        putOrRemove(pos.asLong(), data.pack());
    }

    public void setTemperature(Level level, Collection<BlockPos> positions, short value) {
        for (BlockPos pos : positions) {
            setTemperature(level, pos, value);
        }
    }

    private void putOrRemove(long key, int packed) {
        if (packed == EnvironmentData.DEFAULT_PACKED) {
            envData.remove(key);
        } else {
            envData.put(key, packed);
        }
        setDirty();
    }

    public static EnvironmentSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new Factory<>(EnvironmentSavedData::create, EnvironmentSavedData::load),
                NAME
        );
    }
}