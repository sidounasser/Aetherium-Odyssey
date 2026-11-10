package com.aetherium.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
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
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class HealingStaffItem extends Item {
    public HealingStaffItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            user.getItemCooldownManager().set(this, 100); // 5s cooldown

            Box area = user.getBoundingBox().expand(8.0);
            List<LivingEntity> allies = world.getEntitiesByClass(LivingEntity.class, area, e -> !e.isSpectator() && !(e instanceof HostileEntity));
            for (LivingEntity ally : allies) {
                ally.heal(10.0f);
                ally.removeStatusEffect(StatusEffects.POISON);
                ally.removeStatusEffect(StatusEffects.WITHER);
                ally.removeStatusEffect(StatusEffects.SLOWNESS);
                ally.removeStatusEffect(StatusEffects.WEAKNESS);
                ally.removeStatusEffect(StatusEffects.DARKNESS);
                ally.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 160, 1));
                ally.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 400, 1));
                serverWorld.spawnParticles(ParticleTypes.HEART, ally.getX(), ally.getBodyY(0.7), ally.getZ(), 8, 0.4, 0.4, 0.4, 0.05);
            }

            serverWorld.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, user.getX(), user.getBodyY(0.5), user.getZ(), 35, 1.0, 0.8, 1.0, 0.15);
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.PLAYERS, 1.5f, 1.3f);
            world.playSound(null, user.getBlockPos(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 0.5f, 1.5f);

            if (!user.isCreative()) {
                stack.damage(1, user, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.staff_of_healing").formatted(Formatting.GREEN));
    }
}
