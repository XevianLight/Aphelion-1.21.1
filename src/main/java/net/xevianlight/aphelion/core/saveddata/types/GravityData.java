package net.xevianlight.aphelion.core.saveddata.types;

public class GravityData {
    private float accel;
    private float radius;

    public static final float DEFAULT_GRAVITY = 9.80665f; // 1G
    public static final float GRAVITY_PRECISION = 100.0f;
    public static final float RADIUS_PRECISION = 100.0f;
    public static final float MAX_RADIUS = Short.MAX_VALUE / RADIUS_PRECISION;
    public static final float MAX_GRAVITY = Short.MAX_VALUE / GRAVITY_PRECISION;

    public GravityData(float accel, float radius) {
        this.accel = accel;
        this.radius = radius;
    }

    public int pack() {
        int packed = 0;

        packed |= (int) (this.accel * GRAVITY_PRECISION);
        packed |= (int) (this.radius * RADIUS_PRECISION) << 16;

        return packed;
    }

    public float getAccel() {
        return accel;
    }

    public void setAccel(float accel) {
        this.accel = accel;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(short radius) {
        this.radius = radius;
    }

    public static GravityData unpack(int packed) {
        float accel = (packed & 0xFFFF) / GRAVITY_PRECISION;
        float radius = (packed >> 16) / RADIUS_PRECISION;

        return new GravityData(accel, radius);
    }
}
