package net.xevianlight.aphelion.block.entity.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.xevianlight.aphelion.block.custom.base.TickableBlockEntity;
import net.xevianlight.aphelion.core.init.ModBlockEntities;
import net.xevianlight.aphelion.core.init.ModBlocks;
import net.xevianlight.aphelion.core.saveddata.GravitySavedData;
import net.xevianlight.aphelion.screen.ElectricArcFurnaceMenu;
import net.xevianlight.aphelion.screen.GravityTestBlockMenu;
import net.xevianlight.aphelion.systems.GravityService;
import org.jetbrains.annotations.Nullable;

public class GravityTestBlockEntity extends BlockEntity implements TickableBlockEntity, MenuProvider {

    public GravityTestBlockEntity(BlockPos pos, BlockState blockState) {super(ModBlockEntities.GRAVITY_TEST_BLOCK_ENTITY.get(), pos, blockState);}

    public int areaSize = 5;
    public float gravityStrength = 5f;
    private boolean isInitialized = false;
    public final ItemStackHandler inventory = new ItemStackHandler(0);

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.putInt("areaSize", areaSize);
        pTag.putFloat("gravityStrength", gravityStrength);
        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        areaSize = pTag.getInt("areaSize");
        gravityStrength = pTag.getFloat("gravityStrength");
        super.loadAdditional(pTag, pRegistries);
    }

    public void setRadius(float r) {
        areaSize = (int) r;
    }

    public void setStrength(float s) {
        gravityStrength = s;
    }

    public void addGravityArea() {
        Level level = getLevel();
        if (level != null && level.isClientSide()) return;

        BlockPos pos = this.getBlockPos();

        GravityService.setGravityArea((ServerLevel) level, pos, gravityStrength, areaSize);
    }

    public void removeGravityArea() {
        Level level = getLevel();
        if (level != null && level.isClientSide()) return;

        BlockPos pos = this.getBlockPos();

        GravityService.removeGravityArea((ServerLevel) level, pos);
    }

    @Override
    public void tick(Level entityLevel, long time, BlockState blockState, BlockPos pos) {}

    @Override
    public void clientTick(ClientLevel level, long time, BlockState state, BlockPos pos) {}

    @Override
    public void serverTick(ServerLevel level, long time, BlockState state, BlockPos pos) {}

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void firstTick(Level level, BlockState state, BlockPos pos) { addGravityArea(); isInitialized = true; }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new GravityTestBlockMenu(i, inventory, this);
    }

    @Override
    public Component getDisplayName() {
        return null;
    }

    public void sendUpdate() {
        removeGravityArea();
        addGravityArea();
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }
}
