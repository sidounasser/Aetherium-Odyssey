package com.aetherium.entity;

import com.aetherium.registry.ModItems;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AetheriumArchonEntity extends WitherSkeletonEntity {
    private final ServerBossBar bossBar = new ServerBossBar(
            Text.translatable("entity.aetherium.aetherium_archon").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD),
            BossBar.Color.PURPLE,
            BossBar.Style.NOTCHED_10
    );

    private int currentPhase = 1;
    private int abilityTimer = 0;

    public AetheriumArchonEntity(EntityType<? extends WitherSkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 350;
    }

    public static DefaultAttributeContainer.Builder createArchonAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 350.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.32)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0)
                .add(EntityAttributes.GENERIC_ARMOR, 12.0)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 6.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.85)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0)
                .add(EntityAttributes.GENERIC_SCALE, 1.35);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData);
        this.initEquipment(world.getRandom(), difficulty);
        this.setCustomName(Text.translatable("entity.aetherium.aetherium_archon").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD));
        this.setCustomNameVisible(true);
        this.setPersistent();
        return data;
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ModItems.CELESTIAL_HALBERD));
        this.equipStack(EquipmentSlot.OFFHAND, new ItemStack(ModItems.STAFF_OF_TEMPEST));
        this.equipStack(EquipmentSlot.HEAD, new ItemStack(ModItems.AETHERIUM_HELMET));
        this.equipStack(EquipmentSlot.CHEST, new ItemStack(ModItems.AETHERIUM_CHESTPLATE));
        this.equipStack(EquipmentSlot.LEGS, new ItemStack(ModItems.AETHERIUM_LEGGINGS));
        this.equipStack(EquipmentSlot.FEET, new ItemStack(ModItems.AETHERIUM_BOOTS));

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            this.setEquipmentDropChance(slot, 0.0f);
        }
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        float healthRatio = this.getHealth() / this.getMaxHealth();
        this.bossBar.setPercent(healthRatio);

        if (!(this.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }

        // Ambient boss aura particles
        if (this.age % 5 == 0) {
            serverWorld.spawnParticles(ParticleTypes.END_ROD, this.getX(), this.getBodyY(0.6), this.getZ(), 2, 0.4, 0.6, 0.4, 0.03);
            if (this.currentPhase >= 2) {
                serverWorld.spawnParticles(ParticleTypes.FLAME, this.getX(), this.getBodyY(0.4), this.getZ(), 3, 0.4, 0.5, 0.4, 0.04);
            }
            if (this.currentPhase == 3) {
                serverWorld.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getBodyY(0.5), this.getZ(), 4, 0.5, 0.6, 0.5, 0.05);
            }
        }

        // Check phase transitions
        if (healthRatio <= 0.60f && this.currentPhase == 1) {
            this.currentPhase = 2;
            this.bossBar.setColor(BossBar.Color.YELLOW);
            this.bossBar.setName(Text.translatable("bossbar.aetherium.archon.phase2").formatted(Formatting.GOLD, Formatting.BOLD));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 120, 0));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 120, 0));
            serverWorld.spawnParticles(ParticleTypes.FLASH, this.getX(), this.getBodyY(0.5), this.getZ(), 2, 0.1, 0.1, 0.1, 0.0);
            serverWorld.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.HOSTILE, 1.5f, 1.0f);
            broadcastToArena(serverWorld, Text.translatable("message.aetherium.archon_phase2").formatted(Formatting.GOLD, Formatting.BOLD));
        } else if (healthRatio <= 0.28f && this.currentPhase == 2) {
            this.currentPhase = 3;
            this.bossBar.setColor(BossBar.Color.RED);
            this.bossBar.setName(Text.translatable("bossbar.aetherium.archon.phase3").formatted(Formatting.RED, Formatting.BOLD));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 120, 1));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 20 * 120, 0));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20 * 120, 0));
            serverWorld.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, this.getX(), this.getBodyY(0.5), this.getZ(), 60, 0.8, 1.0, 0.8, 0.25);
            serverWorld.playSound(null, this.getBlockPos(), SoundEvents.ITEM_TRIDENT_THUNDER.value(), SoundCategory.HOSTILE, 1.6f, 0.85f);
            broadcastToArena(serverWorld, Text.translatable("message.aetherium.archon_phase3").formatted(Formatting.RED, Formatting.BOLD));
        }

        LivingEntity target = this.getTarget();
        if (target != null && target.isAlive()) {
            this.abilityTimer++;
            int cooldown = this.currentPhase == 3 ? 65 : (this.currentPhase == 2 ? 85 : 110);
            if (this.abilityTimer >= cooldown) {
                this.abilityTimer = 0;
                performBossAbility(serverWorld, target);
            }
        }
    }

    private void performBossAbility(ServerWorld serverWorld, LivingEntity target) {
        int roll = this.random.nextInt(3);
        if (roll == 0) {
            // Ability 1: Celestial Lightning Strike on target
            LightningEntity bolt = EntityType.LIGHTNING_BOLT.create(serverWorld);
            if (bolt != null) {
                bolt.refreshPositionAfterTeleport(target.getX(), target.getY(), target.getZ());
                bolt.setCosmetic(true);
                serverWorld.spawnEntity(bolt);
            }
            target.damage(serverWorld.getDamageSources().mobAttack(this), 7.0f + this.currentPhase * 1.5f);
            serverWorld.spawnParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getBodyY(0.5), target.getZ(), 25, 0.5, 0.6, 0.5, 0.15);
            serverWorld.playSound(null, target.getBlockPos(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.HOSTILE, 1.0f, 1.2f);
        } else if (roll == 1) {
            // Ability 2: Solar Shockwave around Boss
            Box area = this.getBoundingBox().expand(6.5);
            List<PlayerEntity> players = serverWorld.getNonSpectatingEntities(PlayerEntity.class, area);
            for (PlayerEntity player : players) {
                player.damage(serverWorld.getDamageSources().mobAttack(this), 8.0f);
                Vec3d kb = player.getPos().subtract(this.getPos()).normalize().multiply(1.2);
                player.addVelocity(kb.x, 0.45, kb.z);
                player.velocityModified = true;
                if (this.currentPhase >= 2) {
                    player.setOnFireFor(4.0f);
                }
            }
            serverWorld.spawnParticles(ParticleTypes.END_ROD, this.getX(), this.getY() + 1.0, this.getZ(), 50, 2.5, 0.5, 2.5, 0.12);
            serverWorld.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.HOSTILE, 1.2f, 1.1f);
        } else {
            // Ability 3: Astral Blink near target
            if (this.squaredDistanceTo(target) > 25.0) {
                serverWorld.spawnParticles(ParticleTypes.PORTAL, this.getX(), this.getBodyY(0.5), this.getZ(), 35, 0.4, 0.8, 0.4, 0.15);
                Vec3d offset = this.getPos().subtract(target.getPos()).normalize().multiply(2.5);
                this.requestTeleport(target.getX() + offset.x, target.getY(), target.getZ() + offset.z);
                serverWorld.spawnParticles(ParticleTypes.REVERSE_PORTAL, this.getX(), this.getBodyY(0.5), this.getZ(), 35, 0.4, 0.8, 0.4, 0.15);
                serverWorld.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.3f, 0.85f);
            }
        }
    }

    private void broadcastToArena(ServerWorld world, Text message) {
        for (ServerPlayerEntity player : this.bossBar.getPlayers()) {
            player.sendMessage(message, false);
        }
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {
        super.dropLoot(damageSource, causedByPlayer);
        if (!this.getWorld().isClient) {
            this.dropStack(new ItemStack(ModItems.CELESTIAL_CORE, 1));
            this.dropStack(new ItemStack(ModItems.AETHERIUM_INGOT, 3 + this.random.nextInt(3)));
            this.dropStack(new ItemStack(ModItems.PYROSITE_INGOT, 2 + this.random.nextInt(3)));
            this.dropStack(new ItemStack(ModItems.VOIDITE_INGOT, 2 + this.random.nextInt(2)));
            this.dropStack(new ItemStack(ModItems.CELESTIAL_AMBROSIA, 2));
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, this.getX(), this.getBodyY(0.5), this.getZ(), 100, 1.2, 1.5, 1.2, 0.3);
            serverWorld.spawnParticles(ParticleTypes.END_ROD, this.getX(), this.getBodyY(0.5), this.getZ(), 80, 1.5, 1.5, 1.5, 0.2);
            broadcastToArena(serverWorld, Text.translatable("message.aetherium.archon_defeated").formatted(Formatting.AQUA, Formatting.BOLD));
        }
        super.onDeath(damageSource);
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }
}
