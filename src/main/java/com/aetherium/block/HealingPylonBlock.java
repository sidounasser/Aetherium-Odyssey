package com.aetherium.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class HealingPylonBlock extends Block {
    public HealingPylonBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        if (!world.isClient) {
            world.scheduleBlockTick(pos, this, 40);
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Box area = new Box(pos).expand(10.0);
        List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, area);
        for (PlayerEntity player : players) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 1, true, false, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 0, true, false, true));
            if (player.getHungerManager().isNotFull()) {
                player.getHungerManager().add(1, 0.5f);
            }
        }

        // Purge hostile monsters within 7 blocks
        Box mobArea = new Box(pos).expand(7.0);
        List<HostileEntity> hostiles = world.getNonSpectatingEntities(HostileEntity.class, mobArea);
        for (HostileEntity mob : hostiles) {
            mob.damage(world.getDamageSources().magic(), 2.5f);
            mob.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60, 0));
            world.spawnParticles(ParticleTypes.CRIT, mob.getX(), mob.getBodyY(0.5), mob.getZ(), 5, 0.2, 0.2, 0.2, 0.05);
        }

        world.spawnParticles(ParticleTypes.HEART, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 4, 0.4, 0.3, 0.4, 0.02);
        world.scheduleBlockTick(pos, this, 60);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            player.heal(8.0f);
            player.removeStatusEffect(StatusEffects.POISON);
            player.removeStatusEffect(StatusEffects.WITHER);
            player.removeStatusEffect(StatusEffects.SLOWNESS);
            player.removeStatusEffect(StatusEffects.WEAKNESS);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 1));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 400, 1));

            serverWorld.spawnParticles(ParticleTypes.HEART, player.getX(), player.getBodyY(0.5), player.getZ(), 12, 0.5, 0.5, 0.5, 0.1);
            world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.4f);
            player.sendMessage(Text.translatable("message.aetherium.pylon_healed").formatted(Formatting.GREEN), true);
            world.scheduleBlockTick(pos, this, 40);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(3) == 0) {
            world.addParticle(ParticleTypes.HEART,
                    pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.6,
                    pos.getY() + 1.1,
                    pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.6,
                    0.0, 0.04, 0.0);
        }
        world.addParticle(ParticleTypes.END_ROD,
                pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.8,
                pos.getY() + 0.5 + random.nextDouble() * 0.7,
                pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.8,
                0.0, 0.02, 0.0);
    }
}
