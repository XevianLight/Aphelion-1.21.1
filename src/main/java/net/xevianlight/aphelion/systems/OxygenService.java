package net.xevianlight.aphelion.systems;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.xevianlight.aphelion.Aphelion;
import net.xevianlight.aphelion.core.init.ModDamageSources;
import net.xevianlight.aphelion.core.saveddata.EnvironmentSavedData;

public class OxygenService {
    public static boolean hasOxygen(Level level, BlockPos pos) {
        if (level.isClientSide) {
            // We can't pull oxygen data from the client side, so just, uhh, don't!
            Aphelion.LOGGER.warn("Tried to get server oxygen data from client side!");
            return false;
        }
        boolean positionHasOxygen = EnvironmentSavedData.get((ServerLevel) level).hasOxygen(level, pos);

        return positionHasOxygen;
    }

    public static boolean hasOxygen(Entity entity) {
        // Not sure if this is at the entity's feet, head, or the middle... research later
        BlockPos entityBlockPos = BlockPos.containing(entity.getX(), entity.getY(), entity.getZ());
        return hasOxygen(entity.level(), entityBlockPos);
    }

    public static int OXYGEN_DAMAGE_TICK_AMT = 2;
    public static int OXYGEN_DAMAGE_TICK_FREQ = 20;
    /// Called by LivingEntity.entityTick mixin
    public static void entityTick(ServerLevel level, LivingEntity entity) {
        if (entity.tickCount % OXYGEN_DAMAGE_TICK_FREQ != 0) return;
        if (hasOxygen(entity)) return;
        entity.hurt(ModDamageSources.create(level, ModDamageSources.OXYGEN), OXYGEN_DAMAGE_TICK_AMT);
    }
}
