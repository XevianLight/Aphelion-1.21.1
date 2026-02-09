package net.xevianlight.aphelion.core.saveddata.types;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.xevianlight.aphelion.util.BigCodec;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PartitionData {
    public static final int MAX_PADS = 64;
    private static final StreamCodec<ByteBuf, List<BlockPos>> BLOCKPOS_LIST_CODEC = BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list(MAX_PADS));

    @Nullable private ResourceLocation orbit;
    @Nullable private ResourceLocation destination;
    private boolean traveling;
    private double distanceTraveled;
    private double distanceToDest;
    private boolean generated;
    private UUID owner;
    private List<BlockPos> landingPadControllers;

    public PartitionData(@Nullable ResourceLocation orbit) {
        this.orbit = orbit;
        this.destination = null;
        this.traveling = false;
        this.distanceTraveled = 0;
        this.distanceToDest = 0;
        this.generated = false;
        this.owner = null;
        this.landingPadControllers = List.of();
    }

    public PartitionData(PartitionData other) {
        this.orbit = other.orbit;
        this.destination = other.destination;
        this.traveling = other.traveling;
        this.distanceTraveled = other.distanceTraveled;
        this.distanceToDest = other.distanceToDest;
        this.generated = other.generated;
        this.owner = other.owner;
        this.landingPadControllers = other.landingPadControllers;
    }

    public static final StreamCodec<ByteBuf, PartitionData> STREAM_CODEC =
            BigCodec.composite(
                    // orbit is nullable -> optional codec
                    ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC),
                    d -> Optional.ofNullable(d.getOrbit()),

                    ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC),
                    d -> Optional.ofNullable(d.getDestination()),

                    ByteBufCodecs.BOOL,
                    PartitionData::isTraveling,

                    // doubles -> DOUBLE codec
                    ByteBufCodecs.DOUBLE,
                    PartitionData::getDistanceTraveled,

                    ByteBufCodecs.DOUBLE,
                    PartitionData::getDistanceToDest,

                    ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC),
                    d -> Optional.ofNullable(d.getOwner()),

                    ByteBufCodecs.BOOL,
                    PartitionData::isGenerated,

                    BLOCKPOS_LIST_CODEC,
                    PartitionData::getLandingPadControllers,

                    (orbitOpt, destOpt, traveling, distTraveled, distToDest, ownerOpt, generated, controllers) -> {
                        PartitionData data = new PartitionData(orbitOpt.orElse(null));
                        data.destination = destOpt.orElse(null);
                        data.traveling = traveling;
                        data.distanceTraveled = distTraveled;
                        data.distanceToDest = distToDest;
                        data.owner = ownerOpt.orElse(null);
                        data.generated = generated;
                        data.landingPadControllers = controllers;
                        return data;
                    }
            );

    public @Nullable ResourceLocation getOrbit() {
        return this.orbit;
    }

    public void setOrbit(@Nullable ResourceLocation orbit) {
        this.orbit = orbit;
    }

    public @Nullable ResourceLocation getDestination() {
        return destination;
    }

    public void setDestination(@Nullable ResourceLocation destination) {
        this.destination = destination;
    }

    public boolean isTraveling() {
        return traveling;
    }

    public void setTraveling(boolean traveling) {
        this.traveling = traveling;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(double distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public double getDistanceToDest() {
        return distanceToDest;
    }

    public void setDistanceToDest(double distanceToDest) {
        this.distanceToDest = distanceToDest;
    }

    public void travel(double distance) {
        distanceTraveled = Math.min( distanceTraveled + distance, distanceToDest);
    }

    public boolean isGenerated() {
        return generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }

    public @Nullable UUID getOwner() {
        return owner;
    }

    public void setOwner(@Nullable UUID owner) {
        this.owner = owner;
    }

    public List<BlockPos> getLandingPadControllers() {
        return landingPadControllers;
    }

    public void setLandingPadControllers(List<BlockPos> landingPadControllers) {
        this.landingPadControllers = landingPadControllers;
    }

    public boolean addLandingPadController(BlockPos pos) {
        if (!landingPadControllers.contains(pos)) {
            landingPadControllers.add(pos);
            return true;
        }
        return false;
    }

    public boolean removeLandingPadController(BlockPos pos) {
        return landingPadControllers.remove(pos);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;

        PartitionData that = (PartitionData) obj;

        return Objects.equals(this.orbit, that.orbit)
                && Objects.equals(this.destination, that.destination)
                && this.traveling == that.traveling
                && Double.compare(this.distanceTraveled, that.distanceTraveled) == 0
                && Double.compare(this.distanceToDest, that.distanceToDest) == 0
                && this.generated == that.generated
                && Objects.equals(this.owner, that.owner);
    }

    public long[] getLandingPadContollersAsArray() {
        long[] out = new long[landingPadControllers.size()];
        int i = 0;
        for (BlockPos pos : landingPadControllers) {
            out[i] = pos.asLong();
            i++;
        }
        return out;
    }

    public void setLandingPadContollersFromArray(long[] in) {
        List<BlockPos> newList = new java.util.ArrayList<>(List.of());
        int i = 0;
        for (Long packedPos : in) {
            newList.add(BlockPos.of(packedPos));
            i++;
        }
        setLandingPadControllers(newList);
    }
}
