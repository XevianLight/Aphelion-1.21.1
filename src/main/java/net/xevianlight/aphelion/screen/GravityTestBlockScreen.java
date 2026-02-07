package net.xevianlight.aphelion.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import net.xevianlight.aphelion.Aphelion;
import net.xevianlight.aphelion.network.packet.UpdateGravityTestBlockPacket;

public class GravityTestBlockScreen extends AbstractContainerScreen<GravityTestBlockMenu> {

    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Aphelion.MOD_ID, "textures/gui/gravity_test_block/gui.png");

    public GravityTestBlockScreen(GravityTestBlockMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    private StringWidget gravityWidget;
    private StringWidget rangeWidget;

    @Override
    protected void init() {
        super.init();

        // Gets rid of labels
        this.inventoryLabelY = 73;
        this.titleLabelY = 5;

        // Increase Gravity
        this.addRenderableWidget(Button.builder(Component.literal("+"), (button) -> {
            PacketDistributor.sendToServer(new UpdateGravityTestBlockPacket(menu.blockEntity.getBlockPos(), menu.blockEntity.areaSize, menu.blockEntity.gravityStrength + 0.1f));
        }).bounds(this.leftPos + 7, this.topPos + 30, 9, 9).build());
        // Decrease Gravity
        this.addRenderableWidget(Button.builder(Component.literal("-"), (button) -> {
            PacketDistributor.sendToServer(new UpdateGravityTestBlockPacket(menu.blockEntity.getBlockPos(), menu.blockEntity.areaSize, menu.blockEntity.gravityStrength - 0.1f));
        }).bounds(this.leftPos + 19, this.topPos + 30, 9, 9).build());
        // Increase Radius
        this.addRenderableWidget(Button.builder(Component.literal("+"), (button) -> {
            PacketDistributor.sendToServer(new UpdateGravityTestBlockPacket(menu.blockEntity.getBlockPos(), menu.blockEntity.areaSize + 1, menu.blockEntity.gravityStrength));
        }).bounds(this.leftPos + 135, this.topPos + 32, 9, 9).build());
        // Decrease Radius
        this.addRenderableWidget(Button.builder(Component.literal("-"), (button) -> {
            PacketDistributor.sendToServer(new UpdateGravityTestBlockPacket(menu.blockEntity.getBlockPos(), menu.blockEntity.areaSize - 1, menu.blockEntity.gravityStrength));
        }).bounds(this.leftPos + 147, this.topPos + 32, 9, 9).build());

        // Current Gravity
        gravityWidget = new StringWidget(
                this.leftPos + 11,
                this.topPos+46,
                26,
                9,
                Component.literal("" + menu.blockEntity.gravityStrength),
                this.font);
        this.addRenderableWidget(gravityWidget);

        // Current Radius
        rangeWidget = new StringWidget(
                this.leftPos + 139,
                this.topPos+46,
                26,
                9,
                Component.literal("" + menu.blockEntity.areaSize),
                this.font);
        this.addRenderableWidget(rangeWidget);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        gravityWidget.setMessage(Component.literal(String.format("%.1f", menu.blockEntity.gravityStrength)));
        rangeWidget.setMessage(Component.literal("" + menu.blockEntity.areaSize));
    }
}
