package com.aetherium.forge;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public class ForgeArchonBoss extends AbstractSkeleton {
    private final ServerBossEvent bossBar = new ServerBossEvent(
            Component.translatable("entity.aetherium.aetherium_archon").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD),
            BossEvent.BossBarColor.PURPLE,
            BossEvent.BossBarOverlay.NOTCHED_10
    );

    private int phase = 1;
    private int timer = 0;

    public ForgeArchonBoss(EntityType<? extends AbstractSkeleton> type, Level level) {
        super(type, level);
        this.xpReward = 350;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.WITHER_SKELETON_STEP;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.WITHER_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_SKELETON_DEATH;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.WITHER, 160, 1), this);
        }
        return hit;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return effect.getEffect() != MobEffects.WITHER && super.canBeAffected(effect);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 350.0)
                .add(Attributes.MOVEMENT_SPEED, 0.32)
                .add(Attributes.ATTACK_DAMAGE, 12.0)
                .add(Attributes.ARMOR, 12.0)
                .add(Attributes.ARMOR_TOUGHNESS, 6.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.85)
                .add(Attributes.FOLLOW_RANGE, 48.0)
                .add(Attributes.SCALE, 1.35);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance diff, MobSpawnType reason, SpawnGroupData data) {
        SpawnGroupData res = super.finalizeSpawn(level, diff, reason, data);
        this.populateDefaultEquipmentSlots(level.getRandom(), diff);
        this.setCustomName(Component.translatable("entity.aetherium.aetherium_archon").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));
        this.setCustomNameVisible(true);
        this.setPersistenceRequired();
        return res;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance diff) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ForgeItems.CELESTIAL_HALBERD.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ForgeItems.STAFF_OF_TEMPEST.get()));
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ForgeItems.AETHERIUM_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ForgeItems.AETHERIUM_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ForgeItems.AETHERIUM_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ForgeItems.AETHERIUM_BOOTS.get()));
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            this.setDropChance(slot, 0.0f);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        float ratio = this.getHealth() / this.getMaxHealth();
        this.bossBar.setProgress(ratio);

        if (!(this.level() instanceof ServerLevel sl)) return;

        if (ratio <= 0.60f && this.phase == 1) {
            this.phase = 2;
            this.bossBar.setColor(BossEvent.BossBarColor.YELLOW);
            this.bossBar.setName(Component.translatable("bossbar.aetherium.archon.phase2").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 0));
            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2400, 0));
            for (ServerPlayer sp : this.bossBar.getPlayers()) {
                sp.sendSystemMessage(Component.translatable("message.aetherium.archon_phase2").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
            }
        } else if (ratio <= 0.28f && this.phase == 2) {
            this.phase = 3;
            this.bossBar.setColor(BossEvent.BossBarColor.RED);
            this.bossBar.setName(Component.translatable("bossbar.aetherium.archon.phase3").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 1));
            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2400, 0));
            for (ServerPlayer sp : this.bossBar.getPlayers()) {
                sp.sendSystemMessage(Component.translatable("message.aetherium.archon_phase3").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
            }
        }

        LivingEntity target = this.getTarget();
        if (target != null && target.isAlive()) {
            this.timer++;
            int cd = this.phase == 3 ? 65 : (this.phase == 2 ? 85 : 110);
            if (this.timer >= cd) {
                this.timer = 0;
                int roll = this.random.nextInt(3);
                if (roll == 0) {
                    LightningBolt b = EntityType.LIGHTNING_BOLT.create(sl);
                    if (b != null) { b.moveTo(target.getX(), target.getY(), target.getZ()); b.setVisualOnly(true); sl.addFreshEntity(b); }
                    target.hurt(sl.damageSources().mobAttack(this), 8.0f + this.phase * 1.5f);
                } else if (roll == 1) {
                    for (Player p : sl.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(6.5))) {
                        p.hurt(sl.damageSources().mobAttack(this), 8.5f);
                        Vec3 kb = p.position().subtract(this.position()).normalize().scale(1.2);
                        p.push(kb.x, 0.45, kb.z);
                        p.hurtMarked = true;
                    }
                    sl.sendParticles(ParticleTypes.END_ROD, this.getX(), this.getY() + 1.0, this.getZ(), 50, 2.5, 0.5, 2.5, 0.12);
                    sl.playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 1.2f, 1.1f);
                } else if (this.distanceToSqr(target) > 25.0) {
                    this.teleportTo(target.getX() + 1.5, target.getY(), target.getZ() + 1.5);
                    sl.playSound(null, this.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.3f, 0.85f);
                }
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        this.spawnAtLocation(new ItemStack(ForgeItems.CELESTIAL_CORE.get(), 1));
        this.spawnAtLocation(new ItemStack(ForgeItems.PRIMORDIAL_VOID_ESSENCE.get(), 1));
        this.spawnAtLocation(new ItemStack(ForgeItems.AETHERIUM_INGOT.get(), 4));
        this.spawnAtLocation(new ItemStack(ForgeItems.PYROSITE_INGOT.get(), 3));
        this.spawnAtLocation(new ItemStack(ForgeItems.VOIDITE_INGOT.get(), 3));
        this.spawnAtLocation(new ItemStack(ForgeItems.CELESTIAL_AMBROSIA.get(), 2));
        for (ServerPlayer sp : this.bossBar.getPlayers()) {
            sp.sendSystemMessage(Component.translatable("message.aetherium.archon_defeated").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossBar.removePlayer(player);
    }
}
