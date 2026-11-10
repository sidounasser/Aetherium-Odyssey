package com.aetherium.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class CelestialSteedWhistleItem extends Item {
    public CelestialSteedWhistleItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            if (user.hasVehicle() && user.getVehicle() instanceof SkeletonHorseEntity steed) {
                // Cosmic leap while riding!
                user.getItemCooldownManager().set(this, 40);
                Vec3d look = user.getRotationVec(1.0f);
                steed.setVelocity(look.x * 1.5, 0.85, look.z * 1.5);
                steed.velocityModified = true;
                steed.fallDistance = 0.0f;
                steed.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 100, 0));
                serverWorld.spawnParticles(ParticleTypes.END_ROD, steed.getX(), steed.getY() + 0.5, steed.getZ(), 30, 0.5, 0.4, 0.5, 0.1);
                world.playSound(null, steed.getBlockPos(), SoundEvents.ENTITY_HORSE_JUMP, SoundCategory.PLAYERS, 1.2f, 1.3f);
                return TypedActionResult.success(stack, world.isClient());
            }

            user.getItemCooldownManager().set(this, 160); // 8s summon cooldown
            SkeletonHorseEntity steed = EntityType.SKELETON_HORSE.create(serverWorld);
            if (steed != null) {
                steed.refreshPositionAndAngles(user.getX(), user.getY(), user.getZ(), user.getYaw(), 0.0f);
                steed.setTame(true);
                steed.setOwnerUuid(user.getUuid());
                steed.saddle(stack, SoundCategory.PLAYERS);
                steed.setCustomName(Text.translatable("entity.aetherium.celestial_steed").formatted(Formatting.AQUA, Formatting.BOLD));
                steed.setCustomNameVisible(true);

                EntityAttributeInstance maxHp = steed.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                if (maxHp != null) maxHp.setBaseValue(80.0);
                steed.setHealth(80.0f);

                EntityAttributeInstance speed = steed.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                if (speed != null) speed.setBaseValue(0.40);

                EntityAttributeInstance jump = steed.getAttributeInstance(EntityAttributes.GENERIC_JUMP_STRENGTH);
                if (jump != null) jump.setBaseValue(1.15);

                steed.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20 * 600, 0, true, false));
                steed.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 600, 0, true, false));
                steed.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20 * 20, 0, true, false));

                serverWorld.spawnEntity(steed);
                user.startRiding(steed);

                serverWorld.spawnParticles(ParticleTypes.END_ROD, steed.getX(), steed.getY() + 1.0, steed.getZ(), 45, 0.6, 0.6, 0.6, 0.12);
                world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.PLAYERS, 1.5f, 1.2f);
                user.sendMessage(Text.translatable("message.aetherium.steed_summoned").formatted(Formatting.AQUA), true);
            }
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.aether_steed_whistle").formatted(Formatting.AQUA));
        tooltip.add(Text.translatable("tooltip.aetherium.aether_steed_whistle.leap").formatted(Formatting.GOLD));
    }
}
