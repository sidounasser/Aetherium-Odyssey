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
import net.minecraft.world.World;

import java.util.List;

public class CelestialHalberdItem extends SwordItem {
    public CelestialHalberdItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        attacker.heal(2.0f);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 1), attacker);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 120, 0), attacker);

        if (attacker.getWorld() instanceof ServerWorld serverWorld) {
            Box cleaveBox = target.getBoundingBox().expand(3.0, 1.5, 3.0);
            List<LivingEntity> nearby = serverWorld.getEntitiesByClass(LivingEntity.class, cleaveBox,
                    e -> e != attacker && e != target && !e.isSpectator() && !e.isTeammate(attacker));
            for (LivingEntity extra : nearby) {
                extra.damage(serverWorld.getDamageSources().magic(), 5.0f);
            }
            serverWorld.spawnParticles(ParticleTypes.END_ROD, target.getX(), target.getBodyY(0.5), target.getZ(), 16, 0.5, 0.5, 0.5, 0.1);
            serverWorld.spawnParticles(ParticleTypes.SONIC_BOOM, target.getX(), target.getBodyY(0.5), target.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            user.getItemCooldownManager().set(this, 140); // 7s cooldown

            user.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 240, 2));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 1));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 0));

            Box area = user.getBoundingBox().expand(8.5, 4.0, 8.5);
            List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, area, e -> e != user && !e.isSpectator() && !e.isTeammate(user));
            int lightningCount = 0;
            for (LivingEntity target : targets) {
                target.damage(world.getDamageSources().magic(), 14.0f);
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 25, 2));
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2));

                if (lightningCount < 5) {
                    LightningEntity bolt = EntityType.LIGHTNING_BOLT.create(serverWorld);
                    if (bolt != null) {
                        bolt.refreshPositionAfterTeleport(target.getX(), target.getY(), target.getZ());
                        bolt.setCosmetic(true);
                        serverWorld.spawnEntity(bolt);
                    }
                    lightningCount++;
                }
            }

            serverWorld.spawnParticles(ParticleTypes.END_ROD, user.getX(), user.getY() + 1.0, user.getZ(), 80, 3.0, 1.0, 3.0, 0.15);
            serverWorld.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, user.getX(), user.getY() + 1.0, user.getZ(), 45, 2.0, 1.0, 2.0, 0.2);
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 1.2f, 1.1f);
            world.playSound(null, user.getBlockPos(), SoundEvents.ITEM_TRIDENT_THUNDER.value(), SoundCategory.PLAYERS, 1.5f, 1.0f);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.celestial_halberd.passive").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("tooltip.aetherium.celestial_halberd.active").formatted(Formatting.GOLD));
    }
}
