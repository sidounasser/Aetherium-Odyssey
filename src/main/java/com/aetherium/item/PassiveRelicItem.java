package com.aetherium.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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
import net.minecraft.world.World;

import java.util.List;

public class PassiveRelicItem extends Item {
    private final String tooltipKey;
    private final Formatting color;
    private final boolean hasGlintEffect;
    private final RelicType relicType;

    public enum RelicType {
        NONE,
        RING_OF_HASTE,
        RING_OF_IMMORTALITY
    }

    public PassiveRelicItem(Settings settings, String tooltipKey, Formatting color, boolean hasGlintEffect, RelicType relicType) {
        super(settings);
        this.tooltipKey = tooltipKey;
        this.color = color;
        this.hasGlintEffect = hasGlintEffect;
        this.relicType = relicType;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity player && world.getTime() % 20 == 0) {
            boolean activeSlot = slot < 9 || player.getOffHandStack() == stack;
            if (!activeSlot) return;

            if (this.relicType == RelicType.RING_OF_HASTE) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60, 1, true, false, true));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 60, 1, true, false, true));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 60, 0, true, false, true));
            } else if (this.relicType == RelicType.RING_OF_IMMORTALITY) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 60, 0, true, false, true));
                // Phoenix Emergency Trigger when below 4 hearts (8.0 HP)
                if (player.getHealth() <= 8.0f && !player.getItemCooldownManager().isCoolingDown(this)) {
                    player.getItemCooldownManager().set(this, 20 * 25); // 25s emergency cooldown
                    player.heal(10.0f);
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 160, 2));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 160, 1));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 240, 2));
                    if (world instanceof ServerWorld serverWorld) {
                        serverWorld.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, player.getX(), player.getBodyY(0.5), player.getZ(), 40, 0.5, 0.8, 0.5, 0.2);
                        serverWorld.spawnParticles(ParticleTypes.FLAME, player.getX(), player.getBodyY(0.5), player.getZ(), 25, 0.4, 0.6, 0.4, 0.08);
                    }
                    world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 0.8f, 1.2f);
                    player.sendMessage(Text.translatable("message.aetherium.phoenix_triggered").formatted(Formatting.GOLD, Formatting.BOLD), true);
                }
            }
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return this.hasGlintEffect || super.hasGlint(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable(this.tooltipKey).formatted(this.color));
    }
}
