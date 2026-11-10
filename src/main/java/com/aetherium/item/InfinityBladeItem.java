package com.aetherium.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
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

public class InfinityBladeItem extends SwordItem {
    public InfinityBladeItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        attacker.heal(4.0f);
        target.setOnFireFor(10.0f);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 120, 2), attacker);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 120, 1), attacker);

        if (attacker.getWorld() instanceof ServerWorld serverWorld) {
            Box cleave = target.getBoundingBox().expand(4.0, 2.0, 4.0);
            List<LivingEntity> others = serverWorld.getEntitiesByClass(LivingEntity.class, cleave,
                    e -> e != attacker && e != target && !e.isSpectator() && !e.isTeammate(attacker));
            for (LivingEntity extra : others) {
                extra.damage(serverWorld.getDamageSources().magic(), 9.0f);
                extra.setOnFireFor(5.0f);
            }
            serverWorld.spawnParticles(ParticleTypes.SONIC_BOOM, target.getX(), target.getBodyY(0.5), target.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            serverWorld.spawnParticles(ParticleTypes.END_ROD, target.getX(), target.getBodyY(0.5), target.getZ(), 20, 0.5, 0.5, 0.5, 0.12);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            user.getItemCooldownManager().set(this, 120); // 6s cooldown

            user.heal(user.getMaxHealth());
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 300, 2));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 300, 1));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 300, 2));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 2));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 400, 3));

            Box area = user.getBoundingBox().expand(11.0, 5.0, 11.0);
            List<LivingEntity> victims = world.getEntitiesByClass(LivingEntity.class, area,
                    e -> e != user && !e.isSpectator() && !e.isTeammate(user));

            int bolts = 0;
            for (LivingEntity victim : victims) {
                victim.damage(world.getDamageSources().magic(), 22.0f);
                victim.setOnFireFor(12.0f);
                victim.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 140, 2));
                Vec3d kb = victim.getPos().subtract(user.getPos()).normalize().multiply(1.5);
                victim.addVelocity(kb.x, 0.65, kb.z);
                victim.velocityModified = true;

                if (bolts < 8) {
                    LightningEntity bolt = EntityType.LIGHTNING_BOLT.create(serverWorld);
                    if (bolt != null) {
                        bolt.refreshPositionAfterTeleport(victim.getX(), victim.getY(), victim.getZ());
                        bolt.setCosmetic(true);
                        serverWorld.spawnEntity(bolt);
                    }
                    bolts++;
                }
            }

            serverWorld.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, user.getX(), user.getY() + 1.0, user.getZ(), 100, 3.0, 1.5, 3.0, 0.25);
            serverWorld.spawnParticles(ParticleTypes.END_ROD, user.getX(), user.getY() + 1.0, user.getZ(), 100, 3.5, 1.5, 3.5, 0.2);
            world.playSound(null, user.getBlockPos(), SoundEvents.ITEM_TRIDENT_THUNDER.value(), SoundCategory.PLAYERS, 1.6f, 0.9f);
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1.2f, 1.1f);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.infinity_blade.passive").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD));
        tooltip.add(Text.translatable("tooltip.aetherium.infinity_blade.active").formatted(Formatting.GOLD, Formatting.BOLD));
    }
}
