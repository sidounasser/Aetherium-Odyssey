package com.aetherium.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class VoiditeArmorItem extends ArmorItem {
    public VoiditeArmorItem(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity player) {
            if (this.getSlotType() == EquipmentSlot.FEET && player.getEquippedStack(EquipmentSlot.FEET) == stack) {
                player.fallDistance = 0.0f;
            }

            if (world.getTime() % 20 == 0 && this.getSlotType() == EquipmentSlot.HEAD && player.getEquippedStack(EquipmentSlot.HEAD) == stack) {
                if (hasFullSet(player)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 60, 1, true, false, true));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 60, 1, true, false, true));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60, 1, true, false, true));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 60, 1, true, false, true));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 300, 0, true, false, true));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 60, 1, true, false, true));
                    player.removeStatusEffect(StatusEffects.WITHER);
                    player.removeStatusEffect(StatusEffects.BLINDNESS);
                    player.removeStatusEffect(StatusEffects.DARKNESS);

                    if (world.getTime() % 40 == 0 && world instanceof ServerWorld serverWorld) {
                        serverWorld.spawnParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 1.0, player.getZ(), 5, 0.4, 0.5, 0.4, 0.05);
                    }
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    public static boolean hasFullSet(PlayerEntity player) {
        return player.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof VoiditeArmorItem
                && player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof VoiditeArmorItem
                && player.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof VoiditeArmorItem
                && player.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof VoiditeArmorItem;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.voidite_armor_bonus").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("tooltip.aetherium.voidite_armor_effects").formatted(Formatting.DARK_PURPLE));
    }
}
