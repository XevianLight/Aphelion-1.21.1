package net.xevianlight.aphelion.mixins.common;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.xevianlight.aphelion.systems.GravityService;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.util.Locals;

import java.io.ObjectOutputStream;

@Mixin(Entity.class)
public abstract class EntityMixin extends AttachmentHolder {


    @Shadow
    public float fallDistance;

    // @At("RETURN") injects at all points IMMEDIATELY before a "return" opcode.
    // Immediately before means IMMEDIATELY BEFORE,
    // so even in a method like:
    //
    // return gravity;
    //
    // What's ACTUALLY happening is:
    //
    // $value = gravity;
    // return $value;
    //
    // So, when we inject, we get:
    //
    // $value = gravity;
    // [RETURN INJECT POINT]
    // return $value;
    //
     // so we get to edit $value.
    @Inject(method = "getGravity", at = @At("RETURN"), cancellable = true)
    public void aphelion$getGravity(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(cir.getReturnValue() * GravityService.getGravityFactor((Entity) (Object) this));
    }

    // Should only break if the code that increases fall distance is moved outside Entity$checkFallDamage.
    @WrapMethod(method = "checkFallDamage")
    public void aphelion$checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos, Operation<Void> original) {
        float prevFallDistance = this.fallDistance;

        original.call(y, onGround, state, pos);

        float newFallDistance = this.fallDistance;
        if (newFallDistance > prevFallDistance && newFallDistance > 0) {
            float fallDistanceGained = newFallDistance - prevFallDistance;
            this.fallDistance = prevFallDistance + (fallDistanceGained * Math.min(GravityService.getGravityFactor((Entity) (Object) this), 1));
        }
    }
}
