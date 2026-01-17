package net.xevianlight.aphelion.util;

public class SpacePartitionHelper {

    public static final int SIZE = 16;

    public static int get(double pos) {
        return ceilDiv(pos, SIZE);
    }

    private static int ceilDiv(double a, int b) {
        return (int) Math.floor(a / b);
    }

}
