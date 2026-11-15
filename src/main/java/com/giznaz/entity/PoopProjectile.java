package com.giznaz.entity;

import com.giznaz.registry.GiznazItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class PoopProjectile extends ThrowableItemProjectile {
    public PoopProjectile(EntityType<? extends PoopProjectile> type, Level level) {
        super(type, level);
    }

    public PoopProjectile(EntityType<? extends PoopProjectile> type, LivingEntity owner, Level level) {
        super(type, owner, level);
    }

    @Override
    protected Item getDefaultItem() {
        return GiznazItems.POOP.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            ItemEntity dropped = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(),
                    GiznazItems.POOP.get().getDefaultInstance());
            this.level().addFreshEntity(dropped);
        }
    }

    @Override
    protected void onHitEntity(net.minecraft.world.phys.EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof LivingEntity living) {
            living.hurt(this.damageSources().thrown(this, this.getOwner()), 2.0F);
        }
    }
}
