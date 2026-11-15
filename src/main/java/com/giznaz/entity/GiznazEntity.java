package com.giznaz.entity;

import com.giznaz.registry.GiznazEntities;
import com.giznaz.registry.GiznazItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class GiznazEntity extends PathfinderMob {
    private int throwCooldown = 50;
    private int crouchTicks = 0;
    private int poopCooldown = 260;

    public GiznazEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.xpReward = 3;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        if (crouchTicks > 0) {
            crouchTicks--;
            this.setShiftKeyDown(true);
            if (crouchTicks == 0) {
                this.setShiftKeyDown(false);
                dropPoop();
            }
            return;
        }
        this.setShiftKeyDown(false);

        if (poopCooldown > 0) poopCooldown--;
        if (throwCooldown > 0) throwCooldown--;

        LivingEntity target = findTarget();
        if (target != null) {
            double distance = this.distanceToSqr(target);
            if (distance > 7.0D * 7.0D) {
                this.getNavigation().moveTo(target, 1.0D);
            } else {
                this.getNavigation().stop();
                this.getLookControl().setLookAt(target, 30.0F, 30.0F);
                if (throwCooldown <= 0) {
                    throwPoop(target);
                    throwCooldown = 90 + this.random.nextInt(50);
                }
            }
        }

        if (poopCooldown <= 0 && this.onGround() && this.random.nextInt(5) == 0) {
            crouchTicks = 24;
            poopCooldown = 260 + this.random.nextInt(240);
            this.getNavigation().stop();
        }
    }

    private LivingEntity findTarget() {
        List<LivingEntity> nearby = this.level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(12.0D),
                e -> e.isAlive() && e != this && !(e instanceof Villager)
                        && !(e instanceof GiznazEntity) && !(e instanceof Player p && p.isCreative()));
        return nearby.stream().min(Comparator.comparingDouble(this::distanceToSqr)).orElse(null);
    }

    private void throwPoop(LivingEntity target) {
        PoopProjectile projectile = new PoopProjectile(GiznazEntities.POOP_PROJECTILE.get(), this, this.level());
        projectile.setItem(GiznazItems.POOP.get().getDefaultInstance());
        Vec3 aim = target.getEyePosition().subtract(this.getEyePosition());
        projectile.shoot(aim.x, aim.y + aim.length() * 0.08D, aim.z, 1.25F, 7.0F);
        this.level().addFreshEntity(projectile);
        this.playSound(net.minecraft.sounds.SoundEvents.SNOWBALL_THROW, 0.8F, 0.7F);
    }

    private void dropPoop() {
        ItemStack stack = new ItemStack(GiznazItems.POOP.get());
        this.spawnAtLocation(stack);
    }

    @Override
    protected net.minecraft.sounds.SoundEvent getAmbientSound() {
        return net.minecraft.sounds.SoundEvents.VILLAGER_AMBIENT;
    }

    @Override
    protected net.minecraft.sounds.SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource source) {
        return net.minecraft.sounds.SoundEvents.VILLAGER_HURT;
    }

    @Override
    protected net.minecraft.sounds.SoundEvent getDeathSound() {
        return net.minecraft.sounds.SoundEvents.VILLAGER_DEATH;
    }
}
