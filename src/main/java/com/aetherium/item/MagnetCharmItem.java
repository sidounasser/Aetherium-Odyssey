package com.aetherium.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class MagnetCharmItem extends Item {
    public MagnetCharmItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity player && !player.isSneaking()) {
            // Active when in hotbar (slots 0-8) or offhand (selected)
            if (slot < 9 || player.getOffHandStack() == stack) {
                pullItemsAndXp(world, player, 10.0);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            user.getItemCooldownManager().set(this, 30);
            int count = pullItemsAndXp(world, user, 18.0);
            serverWorld.spawnParticles(ParticleTypes.END_ROD, user.getX(), user.getY() + 1.0, user.getZ(), 20, 0.5, 0.5, 0.5, 0.08);
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.PLAYERS, 1.0f, 1.5f);
            user.sendMessage(Text.translatable("message.aetherium.magnet_pulled", count).formatted(Formatting.AQUA), true);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    public static int pullItemsAndXp(World world, PlayerEntity player, double radius) {
        Box box = player.getBoundingBox().expand(radius);
        List<ItemEntity> items = world.getNonSpectatingEntities(ItemEntity.class, box);
        int count = 0;
        for (ItemEntity item : items) {
            if (!item.cannotPickup()) {
                Vec3d pull = player.getPos().add(0, 0.5, 0).subtract(item.getPos()).normalize().multiply(0.45);
                item.setVelocity(pull);
                item.velocityModified = true;
                count++;
            }
        }
        List<ExperienceOrbEntity> orbs = world.getNonSpectatingEntities(ExperienceOrbEntity.class, box);
        for (ExperienceOrbEntity orb : orbs) {
            Vec3d pull = player.getPos().add(0, 0.5, 0).subtract(orb.getPos()).normalize().multiply(0.45);
            orb.setVelocity(pull);
            orb.velocityModified = true;
            count++;
        }
        return count;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.magnet_charm").formatted(Formatting.AQUA));
        tooltip.add(Text.translatable("tooltip.aetherium.magnet_charm.sneak").formatted(Formatting.GRAY));
    }
}
