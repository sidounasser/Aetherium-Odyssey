package com.aetherium.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
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

public class PyrositeGreatswordItem extends SwordItem {
    public PyrositeGreatswordItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(8.0f);
        attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 140, 0, true, false, true));
        if (attacker.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.FLAME, target.getX(), target.getBodyY(0.5), target.getZ(), 18, 0.35, 0.4, 0.35, 0.06);
            serverWorld.spawnParticles(ParticleTypes.LAVA, target.getX(), target.getBodyY(0.5), target.getZ(), 5, 0.2, 0.3, 0.2, 0.05);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            user.getItemCooldownManager().set(this, 120); // 6s cooldown
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 200, 0));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 160, 0));

            Box box = user.getBoundingBox().expand(6.5, 3.0, 6.5);
            List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, box, e -> e != user && !e.isSpectator() && !e.isTeammate(user));
            for (LivingEntity target : targets) {
                target.setOnFireFor(10.0f);
                target.damage(world.getDamageSources().playerAttack(user), 9.0f);
                Vec3d kb = target.getPos().subtract(user.getPos()).normalize().multiply(1.1);
                target.addVelocity(kb.x, 0.35, kb.z);
                target.velocityModified = true;
            }

            serverWorld.spawnParticles(ParticleTypes.FLAME, user.getX(), user.getY() + 0.8, user.getZ(), 75, 2.5, 0.5, 2.5, 0.08);
            serverWorld.spawnParticles(ParticleTypes.LAVA, user.getX(), user.getY() + 1.0, user.getZ(), 20, 1.8, 0.5, 1.8, 0.05);
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1.0f, 1.15f);
            world.playSound(null, user.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.3f, 0.9f);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.pyrosite_greatsword.passive").formatted(Formatting.RED));
        tooltip.add(Text.translatable("tooltip.aetherium.pyrosite_greatsword.active").formatted(Formatting.GOLD));
    }
}
