package net.xevianlight.aphelion.entites.vehicles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.fluids.FluidType;
import net.xevianlight.aphelion.core.init.ModEntities;
import net.xevianlight.aphelion.util.RocketStructure;
import org.jetbrains.annotations.NotNull;

public class RocketEntity extends Entity implements IEntityWithComplexSpawn {

    RocketStructure structure = new RocketStructure(s -> {
        s.add(0,0,0, Blocks.NETHERITE_BLOCK.defaultBlockState());
        s.add(0,1,0, Blocks.NETHERITE_BLOCK.defaultBlockState());
        s.add(0,2,0, Blocks.NETHERITE_BLOCK.defaultBlockState());
    });

    private static final EntityDataAccessor<CompoundTag> STRUCTURE_TAG =
            SynchedEntityData.defineId(RocketEntity.class, EntityDataSerializers.COMPOUND_TAG);

    public static RocketEntity spawnRocket(Level level, BlockPos pos, RocketStructure structure) {
        if (level.isClientSide) return null;

        RocketEntity rocket = new RocketEntity(ModEntities.ROCKET.get(), level);

        rocket.moveTo(
                pos.getX() + 0.5f,
                pos.getY(),
                pos.getZ() + 0.5f,
                0.0f,
                0.0f
        );

        rocket.setStructure(structure);
        level.addFreshEntity(rocket);

        return rocket;
    }

    public RocketStructure getStructure() {
        return structure;
    }

    public RocketEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(STRUCTURE_TAG, new CompoundTag());
    }

    public void setStructure(RocketStructure structure) {

        this.structure.clear();
        CompoundTag tag = structure.save();
        this.structure.load(tag);
        this.refreshDimensions();
        this.setBoundingBox(this.makeBoundingBox());
        // sync to clients
        if (!level().isClientSide) {
            this.entityData.set(STRUCTURE_TAG, this.structure.save());
        }
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (STRUCTURE_TAG.equals(key)) {
            CompoundTag tag = this.entityData.get(STRUCTURE_TAG);
            this.applyStructureTag(tag);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("RocketStructure")) {
            CompoundTag rocketTag = tag.getCompound("RocketStructure");
            structure.load(rocketTag);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.put("RocketStructure", structure.save());
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushedByFluid(@NotNull FluidType type) {
        return false;
    }

    @Override @NotNull
    protected AABB makeBoundingBox() {
        if (structure == null) {
            double half = 0.5;
            return new AABB(
                    getX() - half, getY() - half + 0.5, getZ() - half,
                    getX() + half, getY() + half + 0.5, getZ() + half
            );
        }
        return computeWorldAABBFromStructure();
    }

    @Override @NotNull
    public AABB getBoundingBoxForCulling() {
        if (structure == null)
            return super.getBoundingBoxForCulling();
        return computeWorldAABBFromStructure();
    }

    @Override @NotNull
    public EntityDimensions getDimensions(@NotNull Pose pose) {
        // Example: dynamic size you already compute for your rocket
        EntityDimensions base = EntityDimensions.scalable(1, 1);

        // Put “eyes” near the top (1 block below top here)
        return base.withEyeHeight(0);
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            // Simple upward movement
            setDeltaMovement(0, 0, 0);
        }

        move(MoverType.SELF, getDeltaMovement());
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buf) {
        buf.writeNbt(structure.save());
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf buf) {
        CompoundTag tag = buf.readNbt();
        if (tag != null) {
            structure.load(tag);
            refreshDimensions();
        }
    }

    public void applyStructureTag(CompoundTag structureTag) {
        this.structure.load(structureTag);
        this.refreshDimensions(); // if your hitbox/eye depends on structure
    }

    private AABB computeWorldAABBFromStructure() {
        RocketStructure.Extents e = structure.computeExtents();
        AABB local = e.toLocalAABB(); // [min, max+1] in local structure coords

        return local.move(getX(), getY(), getZ());
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, amount);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

}