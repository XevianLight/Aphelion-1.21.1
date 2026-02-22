package net.xevianlight.aphelion.block.entity.custom.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.xevianlight.aphelion.block.entity.custom.RocketAssemblerBlockEntity;
import org.jetbrains.annotations.NotNull;

public class RocketAssemblerBlockEntityRenderer implements BlockEntityRenderer<RocketAssemblerBlockEntity> {

    public RocketAssemblerBlockEntityRenderer (BlockEntityRendererProvider.Context context) {

    }

    @Override
    public AABB getRenderBoundingBox(RocketAssemblerBlockEntity blockEntity) {
        // If we don't know bounds yet, fall back to default BE culling.
        RocketAssemblerBlockEntity.PadInfo pad = blockEntity.getPadBounds();
        if (pad == null) {
            return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
        }

        BlockPos min = pad.min();
        BlockPos max = pad.max();

        // Expand slightly to avoid edge precision culling
        return new AABB(
                min.getX(), min.getY(), min.getZ(),
                max.getX() + 1, max.getY() + 1, max.getZ() + 1
        ).inflate(0.5);
    }

    private static final RenderType CENTER_FACE = RenderType.create(
            "aphelion_center_face",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            256,
            false,
            true,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .createCompositeState(false)
    );

    @Override
    public void render(@NotNull RocketAssemblerBlockEntity be, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, int i1) {
//        if (!Minecraft.getInstance().gui.getDebugOverlay().showDebugScreen()) return;

        if (be.getPadBounds() == null) return;
        BlockPos min = be.getPadBounds().min();
        BlockPos max = be.getPadBounds().max();

        AABB box = new AABB(
                min.getX(), min.getY(), min.getZ(),
                max.getX() + 1, max.getY() + 1, max.getZ() + 1
        );

        poseStack.pushPose();
        poseStack.translate(-be.getBlockPos().getX(), -be.getBlockPos().getY(), -be.getBlockPos().getZ());

        VertexConsumer lineVc = multiBufferSource.getBuffer(RenderType.lines());
        LevelRenderer.renderLineBox(poseStack, lineVc, box, 0f, 1f, 0f, 1f);

        VertexConsumer faceVc = multiBufferSource.getBuffer(CENTER_FACE);

        BlockPos center = be.getPadBounds().getCenter();
        float y = center.getY() + 0.01f; // avoid z-fighting

        LevelRenderer.renderFace(
                poseStack, faceVc, Direction.UP,
                center.getX(), y, center.getZ(),
                center.getX() + 1, y, center.getZ() + 1,
                1f, 0f, 0f, 0.5f
        );
        poseStack.popPose();
    }
}
