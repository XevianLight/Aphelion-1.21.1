package net.xevianlight.aphelion.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.Set;

public class DebugRenderUtils {

    /// Utilities for dealing with longs instead of BlockPos
    /// I am not actually sure if this is faster or not. it probably is
    /// 99% sure that this breaks in edge cases
    public static class LongPos {
        private static final int PACKED_X_LENGTH = 26;
        private static final int PACKED_Y_LENGTH = 12;
        private static final int PACKED_Z_LENGTH = 26;
        private static final long PACKED_X_MASK = 67108863L;
        private static final long PACKED_Y_MASK = 4095L;
        private static final long PACKED_Z_MASK = 67108863L;
        private static final int Y_OFFSET = 0;
        private static final int Z_OFFSET = 12;
        private static final int X_OFFSET = 38;
        private static final long Y_MAGIC = PACKED_Y_MASK << Y_OFFSET;
        private static final long Z_MAGIC = PACKED_Z_MASK << Z_OFFSET;
        private static final long X_MAGIC = PACKED_X_MASK << X_OFFSET;

        // no safeguard for the maximum values, but those are
        public static long above(long pos) {
            long y = (((pos & Y_MAGIC) >> Y_OFFSET) + 1) << Y_OFFSET;
            return (pos & (~Y_MAGIC)) | (y & Y_MAGIC);
        }

        public static long below(long pos) {
            long y = (((pos & Y_MAGIC) >> Y_OFFSET) - 1) << Y_OFFSET;
            return (pos & (~Y_MAGIC)) | (y & Y_MAGIC);
        }

        public static long north(long pos) {
            long z = (((pos & Z_MAGIC) >> Z_OFFSET) - 1) << Z_OFFSET;
            return (pos & (~Z_MAGIC)) | (z & Z_MAGIC);
        }

        public static long south(long pos) {
            long z = (((pos & Z_MAGIC) >> Z_OFFSET) + 1) << Z_OFFSET;
            return (pos & (~Z_MAGIC)) | (z & Z_MAGIC);
        }

        public static long east(long pos) {
            long x = (((pos & X_MAGIC) >> X_OFFSET) + 1) << X_OFFSET;
            return (pos & (~X_MAGIC)) | (x & X_MAGIC);
        }

        public static long west(long pos) {
            long x = (((pos & X_MAGIC) >> X_OFFSET) - 1) << X_OFFSET;
            return (pos & (~X_MAGIC)) | (x & X_MAGIC);
        }

        public static int getX(long pos) {
            return BlockPos.getX(pos);
        }

        public static int getY(long pos) {
            return BlockPos.getY(pos);
        }

        public static int getZ(long pos) {
            return BlockPos.getZ(pos);
        }
    }

    public static void drawSphere(PoseStack poseStack, VertexConsumer vc, Vector3f center, float radius) {

        Matrix4f mat = poseStack.last().pose();

        final int Y_SEGMENT_COUNT = 20;
        for (int segmentY=0; segmentY < Y_SEGMENT_COUNT; segmentY++) {
            double bottomAngle = (((double) segmentY / Y_SEGMENT_COUNT) - 0.5) * Math.PI;
            double topAngle = (((double) (segmentY+1) / Y_SEGMENT_COUNT) - 0.5) * Math.PI;
            float y0 =  (float) Math.sin(bottomAngle) * radius + center.y();
            float y1 = (float) Math.sin(topAngle) * radius + center.y();
            float bottomRadius = (float) Math.cos(bottomAngle) * radius;
            float topRadius = (float) Math.cos(topAngle) * radius;

            final int POLAR_SEGMENT_COUNT = 20;
            for (int segmentP = 0; segmentP < POLAR_SEGMENT_COUNT; segmentP++) {
                // "left" and "right" As viewed from outside the sphere
                double leftAngle = (((double) segmentP / POLAR_SEGMENT_COUNT) - 1) * 2 * Math.PI;
                double rightAngle = (((double) (segmentP+1) / POLAR_SEGMENT_COUNT) - 1) * 2 * Math.PI;
                // Points have to wind CCW, so 0->1->2->3 is CCW.
                // 0 and 1 use y0, 2 and 3 use y1.
                float x0, x1, x2, x3, z0, z1, z2, z3;

                x0 = (float) Math.cos(leftAngle) * bottomRadius + center.x();
                x1 = (float) Math.cos(rightAngle) * bottomRadius + center.x();
                x2 = (float) Math.cos(rightAngle) * topRadius + center.x();
                x3 = (float) Math.cos(leftAngle) * topRadius + center.x();
                z0 = (float) Math.sin(leftAngle) * bottomRadius + center.z();
                z1 = (float) Math.sin(rightAngle) * bottomRadius + center.z();
                z2 = (float) Math.sin(rightAngle) * topRadius + center.z();
                z3 = (float) Math.sin(leftAngle) * topRadius + center.z();

                // Draw the sphere quad
                quad(mat, vc,
                        x0, y0, z0,
                        x1, y0, z1,
                        x2, y1, z2,
                        x3, y1, z3,
                        0.7f, 0.2f, 0.2f, 0.5f);
            }
        }
    }

     // Sorry LOSERS, we only let FAST data structures in HERE.
    public static void drawBlockArea(PoseStack poseStack, VertexConsumer vc, LongOpenHashSet blocks) {

        for (long p : blocks) {


            // Neighbor checks: only render faces exposed to non-oxygen
            boolean up    = blocks.contains(LongPos.above(p));
            boolean down  = blocks.contains(LongPos.below(p));
            boolean north = blocks.contains(LongPos.north(p));
            boolean south = blocks.contains(LongPos.south(p));
            boolean east  = blocks.contains(LongPos.east(p));
            boolean west  = blocks.contains(LongPos.west(p));

            if (up && down && north && south && east && west) continue;

            drawBlockFaces(poseStack, vc, p, up, down, north, south, east, west);
        }
    }

    public static void drawBlockFaces(
            PoseStack poseStack, VertexConsumer vc, long posAsLong,
            boolean up, boolean down, boolean north, boolean south, boolean east, boolean west) {
        final float eps = 0.0025f;
        float x0 = LongPos.getX(posAsLong) + eps;
        float y0 = LongPos.getY(posAsLong) + eps;
        float z0 = LongPos.getZ(posAsLong) + eps;
        float x1 = LongPos.getX(posAsLong) + 1 - eps;
        float y1 = LongPos.getY(posAsLong) + 1 - eps;
        float z1 = LongPos.getZ(posAsLong) + 1 - eps;

        // Color (ARGB-ish but as floats)
        float r = 0.2f, g = 0.8f, b = 1.0f, a = 0.18f;

        Matrix4f mat = poseStack.last().pose();

        // IMPORTANT: vertex winding should be consistent (counter-clockwise)
        if (!up)    quad(mat, vc, x0,y1,z0,  x1,y1,z0,  x1,y1,z1,  x0,y1,z1,  r,g,b,a);
        if (!down)  quad(mat, vc, x0,y0,z1,  x1,y0,z1,  x1,y0,z0,  x0,y0,z0,  r,g,b,a);

        if (!north) quad(mat, vc, x1,y0,z0,  x0,y0,z0,  x0,y1,z0,  x1,y1,z0,  r,g,b,a);
        if (!south) quad(mat, vc, x0,y0,z1,  x1,y0,z1,  x1,y1,z1,  x0,y1,z1,  r,g,b,a);

        if (!east)  quad(mat, vc, x1,y0,z1,  x1,y0,z0,  x1,y1,z0,  x1,y1,z1,  r,g,b,a);
        if (!west)  quad(mat, vc, x0,y0,z0,  x0,y0,z1,  x0,y1,z1,  x0,y1,z0,  r,g,b,a);
    }

    public static void quad(
            Matrix4f mat, VertexConsumer vc,
            float x0, float y0, float z0,
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            float x3, float y3, float z3,
            float r, float g, float b, float a
    ) {
        // POSITION_COLOR format: ONLY position + color.
        vc.addVertex(mat, x0, y0, z0).setColor(r, g, b, a);
        vc.addVertex(mat, x1, y1, z1).setColor(r, g, b, a);
        vc.addVertex(mat, x2, y2, z2).setColor(r, g, b, a);
        vc.addVertex(mat, x3, y3, z3).setColor(r, g, b, a);
    }
}
