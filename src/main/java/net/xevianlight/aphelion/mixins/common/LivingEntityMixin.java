package net.xevianlight.aphelion.mixins.common;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.xevianlight.aphelion.systems.OxygenService;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, Level level) { super(type, level); }

    @Inject(method= "tick", at = @At("TAIL"))
    public void aphelion$tick(CallbackInfo ci) {
        if ((level() instanceof ServerLevel level)) {
            LivingEntity entity = (LivingEntity) (Object) this;

            // Oxygen system
            OxygenService.entityTick(level, entity);
        }
    }
}
