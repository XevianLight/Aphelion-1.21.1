package net.xevianlight.aphelion.core.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.xevianlight.aphelion.Aphelion;
import net.xevianlight.aphelion.entites.vehicles.RocketEntity;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, Aphelion.MOD_ID);

    public static final Supplier<EntityType<RocketEntity>> ROCKET =
            ENTITIES.register("rocket", () ->
                    EntityType.Builder.<RocketEntity>of(RocketEntity::new, MobCategory.MISC)
                            .sized(0.75f, 2.0f) // tall-ish rocket
                            .clientTrackingRange(8)
                            .updateInterval(1)
                            .build("rocket")
            );

    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
    }
}
