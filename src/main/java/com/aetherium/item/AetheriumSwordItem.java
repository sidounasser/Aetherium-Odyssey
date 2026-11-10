package com.aetherium.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
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

public class AetheriumSwordItem extends SwordItem {
    public AetheriumSwordItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 30, 1), attacker);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 100, 0), attacker);
        if (attacker.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.END_ROD, target.getX(), target.getBodyY(0.5), target.getZ(), 12, 0.3, 0.4, 0.3, 0.08);
            serverWorld.spawnParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getBodyY(0.5), target.getZ(), 10, 0.3, 0.4, 0.3, 0.1);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            user.getItemCooldownManager().set(this, 80); // 4s cooldown
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 1));

            Box box = user.getBoundingBox().expand(6.0, 3.0, 6.0);
            List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, box,
                    e -> e != user && !e.isSpectator() && !e.isTeammate(user));

            for (LivingEntity target : targets) {
                if (target.isTeammate(user)) continue;
                target.damage(world.getDamageSources().playerAttack(user), 7.5f);
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 35, 1));
                Vec3d knockback = target.getPos().subtract(user.getPos()).normalize().multiply(1.3);
                target.addVelocity(knockback.x, 0.45, knockback.z);
                target.velocityModified = true;
            }

            for (int i = 0; i < 36; i++) {
                double angle = Math.toRadians(i * 10);
                double px = user.getX() + Math.cos(angle) * 3.0;
                double pz = user.getZ() + Math.sin(angle) * 3.0;
                serverWorld.spawnParticles(ParticleTypes.END_ROD, px, user.getY() + 0.5, pz, 2, 0.1, 0.1, 0.1, 0.05);
            }
            serverWorld.spawnParticles(ParticleTypes.SWEEP_ATTACK, user.getX(), user.getBodyY(0.5), user.getZ(), 6, 1.5, 0.5, 1.5, 0.0);
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.2f, 1.2f);
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.PLAYERS, 1.5f, 1.0f);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.aetherium_sword.passive").formatted(Formatting.AQUA));
        tooltip.add(Text.translatable("tooltip.aetherium.aetherium_sword.active").formatted(Formatting.GOLD));
    }
}
