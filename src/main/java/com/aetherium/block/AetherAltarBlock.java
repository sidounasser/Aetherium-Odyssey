package com.aetherium.block;

import com.aetherium.entity.AetheriumArchonEntity;
import com.aetherium.entity.ModEntities;
import com.aetherium.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class AetherAltarBlock extends Block {
    public AetherAltarBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.isOf(ModItems.ARCHON_SUMMONER)) {
            if (!world.isClient && world instanceof ServerWorld serverWorld) {
                if (!player.isCreative()) {
                    stack.decrement(1);
                }

                // Strike cosmetic lightning
                LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(serverWorld);
                if (lightning != null) {
                    lightning.refreshPositionAfterTeleport(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
                    lightning.setCosmetic(true);
                    serverWorld.spawnEntity(lightning);
                }

                // Spawn the Boss: Aetherium Archon
                AetheriumArchonEntity boss = ModEntities.AETHERIUM_ARCHON.create(serverWorld);
                if (boss != null) {
                    boss.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, world.random.nextFloat() * 360.0f, 0.0f);
                    boss.initialize(serverWorld, serverWorld.getLocalDifficulty(pos), SpawnReason.EVENT, null);
                    serverWorld.spawnEntity(boss);
                }

                serverWorld.spawnParticles(ParticleTypes.END_ROD, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 80, 1.2, 1.5, 1.2, 0.15);
                serverWorld.spawnParticles(ParticleTypes.FLASH, pos.getX() + 0.5, pos.getY() + 2.0, pos.getZ() + 0.5, 2, 0.1, 0.1, 0.1, 0.0);
                world.playSound(null, pos, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.HOSTILE, 1.5f, 0.85f);

                for (ServerPlayerEntity serverPlayer : serverWorld.getPlayers()) {
                    if (serverPlayer.squaredDistanceTo(pos.toCenterPos()) <= 64 * 64) {
                        serverPlayer.sendMessage(Text.translatable("message.aetherium.archon_awakened").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD), false);
                    }
                }
            }
            return ItemActionResult.success(world.isClient);
        }

        // Ritual transmutation: Diamond + 3 XP levels -> Aetherium Shard
        if (stack.isOf(Items.DIAMOND)) {
            if (player.experienceLevel >= 3 || player.isCreative()) {
                if (!world.isClient && world instanceof ServerWorld serverWorld) {
                    if (!player.isCreative()) {
                        player.addExperienceLevels(-3);
                        stack.decrement(1);
                    }
                    ItemStack result = new ItemStack(ModItems.AETHERIUM_SHARD, 2);
                    if (!player.getInventory().insertStack(result)) {
                        player.dropItem(result, false);
                    }
                    serverWorld.spawnParticles(ParticleTypes.ENCHANT, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 45, 0.5, 0.5, 0.5, 0.2);
                    world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 1.5f, 1.2f);
                    player.sendMessage(Text.translatable("message.aetherium.altar_infused_diamond").formatted(Formatting.AQUA), true);
                }
                return ItemActionResult.success(world.isClient);
            } else {
                if (!world.isClient) {
                    player.sendMessage(Text.translatable("message.aetherium.altar_need_xp").formatted(Formatting.RED), true);
                }
                return ItemActionResult.FAIL;
            }
        }

        // Ritual transmutation: Golden Apple + 1 Aetherium Shard in offhand -> Aether Apple
        if (stack.isOf(Items.GOLDEN_APPLE)) {
            if (!world.isClient && world instanceof ServerWorld serverWorld) {
                if (!player.isCreative()) {
                    stack.decrement(1);
                }
                ItemStack result = new ItemStack(ModItems.AETHER_APPLE, 1);
                if (!player.getInventory().insertStack(result)) {
                    player.dropItem(result, false);
                }
                serverWorld.spawnParticles(ParticleTypes.TOTEM_OF_UNDYING, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 35, 0.4, 0.6, 0.4, 0.15);
                world.playSound(null, pos, SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 1.2f, 1.4f);
                player.sendMessage(Text.translatable("message.aetherium.altar_infused_apple").formatted(Formatting.GOLD), true);
            }
            return ItemActionResult.success(world.isClient);
        }

        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 20 * 45, 0, true, false, true));
            player.sendMessage(Text.translatable("message.aetherium.altar_info").formatted(Formatting.AQUA), true);
            world.playSound(null, pos, SoundEvents.BLOCK_BEACON_AMBIENT, SoundCategory.BLOCKS, 1.0f, 1.2f);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.8;
        double y = pos.getY() + 1.0 + random.nextDouble() * 0.6;
        double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.8;
        world.addParticle(ParticleTypes.END_ROD, x, y, z, 0.0, 0.03, 0.0);
        if (random.nextInt(2) == 0) {
            world.addParticle(ParticleTypes.ENCHANT, pos.getX() + 0.5, pos.getY() + 1.3, pos.getZ() + 0.5,
                    (random.nextDouble() - 0.5) * 1.5, random.nextDouble() - 0.5, (random.nextDouble() - 0.5) * 1.5);
        }
    }
}
