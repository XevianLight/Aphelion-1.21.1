package net.xevianlight.aphelion.client;

import net.xevianlight.aphelion.network.packet.PartitionData;

import java.util.Optional;

public final class PartitionClientState {
    private static volatile PartitionData last = null;

    public static void set(PartitionData d) { last = d; }

    public static Optional<PartitionData> get() {
        return Optional.ofNullable(last);
    }

    public static String idOrUnknown() {
        return last != null ? last.id() : "unknown";
    }
//
//    public static int pxOr(int fallback) {
//        return last != null ? last.px() : fallback;
//    }
//
//    public static int pyOr(int fallback) {
//        return last != null ? last.py() : fallback;
//    }
}