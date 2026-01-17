package net.xevianlight.aphelion.core.space;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class SpacePartitionSavedData extends SavedData {

    private static final String NAME = "aphelion_station_partitions";

    private final Long2ObjectMap<ResourceLocation> map = new Long2ObjectOpenHashMap<>();

    public static SpacePartitionSavedData create() {
        return new SpacePartitionSavedData();
    }

    public static SpacePartitionSavedData load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        SpacePartitionSavedData data = create();

        ListTag entires = tag.getList("Entries", CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < entires.size(); i++) {
            CompoundTag e = entires.getCompound(i);
            long key = e.getLong("Key");
            String orbit = e.getString("Orbit"); // "aphelion/mars"
            ResourceLocation orbitRL = ResourceLocation.tryParse(orbit);
            if (orbitRL != null)
                data.map.put(key, orbitRL);
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag entries = new ListTag();

        map.long2ObjectEntrySet().forEach(entry -> {
            CompoundTag e = new CompoundTag();
            e.putLong("Key", entry.getLongKey());
            e.putString("Orbit", entry.getValue().toString());
            entries.add(e);
        });

        tag.put("Entries", entries);
        return tag;
    }

    public @Nullable ResourceLocation getOrbitForPartition(int px, int pz) {
        return map.get(pack(px, pz));
    }

    public void setOrbitForPartition(int px, int pz, ResourceLocation orbit) {
        long key = pack(px, pz);
        ResourceLocation prev = map.get(key);
        if (!orbit.equals(prev)) {
            map.put(key, orbit);
            setDirty();
        }
    }

    public boolean clearOrbitForPartition(int px, int pz) {
        long key = pack(px, pz);
        ResourceLocation removed = map.remove(key);
        if (removed != null) {
            setDirty();;
            return true;
        }
        return false;
    }

    public void clearAllOrbits() {
        if (!map.isEmpty()) {
            map.clear();
            setDirty();
        }
    }

    public void overwriteAllExistingOrbits(ResourceLocation orbit) {
        if (map.isEmpty()) return;

        boolean changed = false;
        for (var entry : map.long2ObjectEntrySet()) {
            if(!orbit.equals(entry.getValue())) {
                entry.setValue(orbit);
                changed = true;
            }
        }

        if (changed) setDirty();
    }

    public static SpacePartitionSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new Factory<>(SpacePartitionSavedData::create, SpacePartitionSavedData::load),
                NAME
        );
    }

    public static long pack(int px, int pz) {
        return (((long) px) << 32) | (pz & 0xffffffffL);
    }

    public static int unpackX(long key) {
        return (int)(key >> 32);
    }

    public static int unpackZ(long key) {
        return (int)key;
    }

}
