package com.giznaz.events;

import com.giznaz.registry.GiznazEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "giznaz")
public final class GiznazSpawnEvents {
    private GiznazSpawnEvents() {}

    @SubscribeEvent
    public static void onVillagerJoin(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Villager villager) || !(event.getLevel() instanceof ServerLevel level)) return;
        if (event.loadedFromDisk() || level.random.nextInt(12) != 0) return;

        AABB area = villager.getBoundingBox().inflate(48.0D);
        if (!level.getEntitiesOfClass(com.giznaz.entity.GiznazEntity.class, area).isEmpty()) return;

        var type = GiznazEntities.GIZNAZ.get();
        var giznaz = type.create(level);
        if (giznaz == null) return;
        double angle = level.random.nextDouble() * Math.PI * 2.0D;
        double distance = 5.0D + level.random.nextDouble() * 7.0D;
        giznaz.moveTo(villager.getX() + Math.cos(angle) * distance, villager.getY(),
                villager.getZ() + Math.sin(angle) * distance, level.random.nextFloat() * 360.0F, 0.0F);
        if (level.noCollision(giznaz)) level.addFreshEntity(giznaz);
    }
}
