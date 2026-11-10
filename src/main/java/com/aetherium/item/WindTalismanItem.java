package com.aetherium.item;

import net.minecraft.entity.EquipmentSlot;
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
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class WindTalismanItem extends Item {
    public WindTalismanItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        user.getItemCooldownManager().set(this, 45); // 2.25s cooldown

        Vec3d look = user.getRotationVec(1.0f);
        user.setVelocity(look.x * 1.65, Math.max(0.65, look.y * 1.35 + 0.35), look.z * 1.65);
        user.velocityModified = true;
        user.fallDistance = 0.0f;

        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 120, 0, true, false, true));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 1, true, false, true));
            serverWorld.spawnParticles(ParticleTypes.CLOUD, user.getX(), user.getY() + 0.3, user.getZ(), 25, 0.4, 0.2, 0.4, 0.08);
            serverWorld.spawnParticles(ParticleTypes.END_ROD, user.getX(), user.getY() + 0.5, user.getZ(), 12, 0.3, 0.3, 0.3, 0.05);
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_BREEZE_JUMP, SoundCategory.PLAYERS, 1.3f, 1.15f);

            if (!user.isCreative()) {
                stack.damage(1, user, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.wind_talisman").formatted(Formatting.AQUA));
    }
}
