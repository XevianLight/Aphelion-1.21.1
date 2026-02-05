package net.xevianlight.aphelion.systems;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.xevianlight.aphelion.network.ClientPlayerState;
import net.xevianlight.aphelion.core.saveddata.GravitySavedData;
import net.xevianlight.aphelion.core.saveddata.types.GravityData;

public class GravityService {

    ///  If i did this right, there SHOULDN'T be a way to break client/server separation with this...
    ///  you shouldn't be able to get the "ServerLevel" object from the Client side unless you're already
    ///  breaking that rule, in which case, go for it lmfao

    public static float getGravityAccel(Level level, BlockPos pos) {
        if (level.isClientSide) {
            // Pull from the client data b/c we can't access the server's saved data
            return ClientPlayerState.getLocalData().gravity();
        }
        // TODO: maybe change this based on how stuff pans out
        var gravity = GravitySavedData.get((ServerLevel) level).getGravityMax(pos);

        return gravity;
    }

    public static float getGravityAccel(Entity entity) {
        // Not sure if this is at the entity's feet, head, or the middle... research later
        // Blockpos is from entity's feet ~Xev
        BlockPos entityBlockPos = BlockPos.containing(entity.getX(), entity.getY(), entity.getZ());
        return getGravityAccel(entity.level(), entityBlockPos);
    }

    /// Called by LivingEntity$travel mixin
    public static void onEntityTravel(Level level, LivingEntity entity) {
        if (
                entity.isFallFlying() || entity.isInLiquid() ||
                entity.isUnderWater() ||
                entity.hasEffect(MobEffects.SLOW_FALLING)
        ) return;

        float gravityAccelReal = getGravityAccel(entity);

        // How many times normal gravity you're experiencing.
        // "normal gravity" varies across different entities. Thankfully, minecraft slaps a "protected" status
        // on LivingEntity.getGravity(), so i graciously get to go fuck myself and not care.
        // Players are 0.08 units/second/travel() of gravity (from what i've gathered)
        float gravityFactor = gravityAccelReal / GravityData.ONE_G;

        // NOTE: this might cause certain entities to fly into the stratosphere at ultra low gravity,
        // seeing as this isn't the same for all entities.
        // Thankfully, though, this should have no effect on anything at default gravity.
        float baseGameGravityAccel = 0.08f;
        float translatedAccel = baseGameGravityAccel * gravityFactor;

        Vec3 currentVelocity = entity.getDeltaMovement();
        // add baseGameGravity to cancel normal gravity, then subtract the new gravity
        if (translatedAccel > 0) entity.setDeltaMovement(currentVelocity.x(), currentVelocity.y() + (baseGameGravityAccel - translatedAccel), currentVelocity.z());
        else entity.setDeltaMovement(currentVelocity.x(), currentVelocity.y() + baseGameGravityAccel, currentVelocity.z());
    }
}
