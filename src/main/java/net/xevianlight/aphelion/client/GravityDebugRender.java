package net.xevianlight.aphelion.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.xevianlight.aphelion.Aphelion;
import net.xevianlight.aphelion.core.saveddata.GravitySavedData;
import net.xevianlight.aphelion.core.saveddata.types.GravityData;

@EventBusSubscriber(modid = Aphelion.MOD_ID, value = Dist.CLIENT)
public class GravityDebugRender {
    // Untextured translucent quads (POSITION_COLOR only)
    private static final RenderType GRAVITY_FILL = RenderType.create(
            "aphelion_gravity_fill",
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
                    .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                    .createCompositeState(true)
    );

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        // One stage only (pick one that exists and looks good)
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        if (!mc.gui.getDebugOverlay().showDebugScreen()) return;

        PoseStack poseStack = event.getPoseStack();
        var cam = mc.gameRenderer.getMainCamera();
        var camPos = cam.getPosition();

        poseStack.pushPose();
        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);

        // I'm lazy, so i'm just gonna make this work on a singleplayer server and call it a day :P
        IntegratedServer singleplayer = mc.getSingleplayerServer();
        if (singleplayer == null) return;

        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        VertexConsumer vc = bufferSource.getBuffer(GRAVITY_FILL);

        for (Long2IntMap.Entry gravityEntry : GravitySavedData.get(singleplayer.getLevel(mc.level.dimension()))._debug_getGravityData().long2IntEntrySet()) {
            GravityData d = GravityData.unpack(gravityEntry.getIntValue());
            BlockPos p = BlockPos.of(gravityEntry.getLongKey());

            DebugRenderUtils.drawSphere(poseStack, vc, p.getCenter().toVector3f(), d.getRadius());
        }

        poseStack.popPose();
        bufferSource.endBatch(GRAVITY_FILL);
    }
}
