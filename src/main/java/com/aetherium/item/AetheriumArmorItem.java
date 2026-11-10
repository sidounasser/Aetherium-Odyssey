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

public class AetheriumArmorItem extends ArmorItem {
    public AetheriumArmorItem(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity player) {
            // Boots negate fall damage at all times when worn
            if (this.getSlotType() == EquipmentSlot.FEET && player.getEquippedStack(EquipmentSlot.FEET) == stack) {
                if (player.fallDistance > 2.5f) {
                    player.fallDistance = 0.0f;
                }
            }

            // Evaluate periodic buffs once per second from the helmet slot (or individual slots)
            if (world.getTime() % 20 == 0) {
                if (this.getSlotType() == EquipmentSlot.HEAD && player.getEquippedStack(EquipmentSlot.HEAD) == stack) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 300, 0, true, false, true));

                    if (hasFullSuitOfArmor(player)) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 60, 0, true, false, true));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 60, 0, true, false, true));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 60, 0, true, false, true));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60, 0, true, false, true));
                        player.removeStatusEffect(StatusEffects.WITHER);
                        player.removeStatusEffect(StatusEffects.POISON);

                        if (world.getTime() % 40 == 0 && world instanceof ServerWorld serverWorld) {
                            serverWorld.spawnParticles(ParticleTypes.END_ROD, player.getX(), player.getY() + 1.0, player.getZ(), 2, 0.35, 0.5, 0.35, 0.01);
                        }
                    }
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    public static boolean hasFullSuitOfArmor(PlayerEntity player) {
        ItemStack head = player.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack legs = player.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack feet = player.getEquippedStack(EquipmentSlot.FEET);
        return head.getItem() instanceof AetheriumArmorItem
                && chest.getItem() instanceof AetheriumArmorItem
                && legs.getItem() instanceof AetheriumArmorItem
                && feet.getItem() instanceof AetheriumArmorItem;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.armor_set_bonus").formatted(Formatting.AQUA));
        tooltip.add(Text.translatable("tooltip.aetherium.armor_set_effects").formatted(Formatting.GOLD));
    }
}
