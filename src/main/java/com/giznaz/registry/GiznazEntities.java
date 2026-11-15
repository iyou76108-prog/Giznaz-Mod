package com.giznaz.registry;

import com.giznaz.GiznazMod;
import com.giznaz.entity.GiznazEntity;
import com.giznaz.entity.PoopProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class GiznazEntities {
    private GiznazEntities() {}

    private static final DeferredRegister<EntityType<?>> TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, GiznazMod.MOD_ID);

    public static final RegistryObject<EntityType<GiznazEntity>> GIZNAZ = TYPES.register("giznaz",
            () -> EntityType.Builder.of(GiznazEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.95F).clientTrackingRange(8).build("giznaz:giznaz"));

    public static final RegistryObject<EntityType<PoopProjectile>> POOP_PROJECTILE = TYPES.register("poop_projectile",
            () -> EntityType.Builder.<PoopProjectile>of(PoopProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build("giznaz:poop_projectile"));

    public static void register(IEventBus bus) {
        TYPES.register(bus);
        bus.addListener(GiznazEntities::registerAttributes);
    }

    private static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(GIZNAZ.get(), GiznazEntity.createAttributes().build());
    }
}
