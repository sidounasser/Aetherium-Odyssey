package com.aetherium.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
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
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class PyrositeArmorItem extends ArmorItem {
    public PyrositeArmorItem(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity player && world.getTime() % 20 == 0) {
            if (this.getSlotType() == EquipmentSlot.HEAD && player.getEquippedStack(EquipmentSlot.HEAD) == stack) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 60, 0, true, false, true));

                if (hasFullSet(player)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 60, 1, true, false, true));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 60, 0, true, false, true));
                    player.extinguish();

                    // Solar Thermal Aura: burns nearby hostile mobs within 4.5 blocks
                    Box box = player.getBoundingBox().expand(4.5);
                    List<HostileEntity> mobs = world.getNonSpectatingEntities(HostileEntity.class, box);
                    for (HostileEntity mob : mobs) {
                        mob.setOnFireFor(4.0f);
                        mob.damage(world.getDamageSources().onFire(), 2.0f);
                    }

                    if (world.getTime() % 40 == 0 && world instanceof ServerWorld serverWorld) {
                        serverWorld.spawnParticles(ParticleTypes.FLAME, player.getX(), player.getY() + 0.8, player.getZ(), 4, 0.4, 0.4, 0.4, 0.02);
                    }
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    public static boolean hasFullSet(PlayerEntity player) {
        return player.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof PyrositeArmorItem
                && player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof PyrositeArmorItem
                && player.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof PyrositeArmorItem
                && player.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof PyrositeArmorItem;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.pyrosite_armor_bonus").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.aetherium.pyrosite_armor_effects").formatted(Formatting.RED));
    }
}
