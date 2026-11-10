package com.aetherium.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class ElevatorPadBlock extends Block {
    private static final int MAX_RANGE = 64;

    public ElevatorPadBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            boolean goingDown = player.isSneaking();
            BlockPos target = findTargetElevator(world, pos, goingDown ? -1 : 1);
            if (target != null) {
                teleportPlayer(serverWorld, player, target, goingDown);
            } else {
                String key = goingDown ? "message.aetherium.elevator_none_below" : "message.aetherium.elevator_none_above";
                player.sendMessage(Text.translatable(key).formatted(Formatting.YELLOW), true);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClient && world instanceof ServerWorld serverWorld && entity instanceof PlayerEntity player) {
            if (player.isSneaking() && !player.getItemCooldownManager().isCoolingDown(this.asItem())) {
                BlockPos target = findTargetElevator(world, pos, -1);
                if (target != null) {
                    player.getItemCooldownManager().set(this.asItem(), 20);
                    teleportPlayer(serverWorld, player, target, true);
                }
            }
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    private BlockPos findTargetElevator(World world, BlockPos start, int direction) {
        for (int i = 2; i <= MAX_RANGE; i++) {
            BlockPos check = start.add(0, i * direction, 0);
            if (world.getBlockState(check).isOf(this)) {
                // Ensure the 2 blocks above the target pad are passable
                if (world.getBlockState(check.up()).getCollisionShape(world, check.up()).isEmpty()
                        && world.getBlockState(check.up(2)).getCollisionShape(world, check.up(2)).isEmpty()) {
                    return check;
                }
            }
        }
        return null;
    }

    public static void teleportPlayer(ServerWorld world, PlayerEntity player, BlockPos targetPad, boolean goingDown) {
        world.spawnParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 1.0, player.getZ(), 25, 0.3, 0.5, 0.3, 0.1);
        player.requestTeleport(targetPad.getX() + 0.5, targetPad.getY() + 1.05, targetPad.getZ() + 0.5);
        player.fallDistance = 0.0f;
        world.spawnParticles(ParticleTypes.REVERSE_PORTAL, targetPad.getX() + 0.5, targetPad.getY() + 1.5, targetPad.getZ() + 0.5, 35, 0.3, 0.5, 0.3, 0.08);
        world.playSound(null, targetPad, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, goingDown ? 0.85f : 1.25f);
        player.sendMessage(Text.translatable(goingDown ? "message.aetherium.elevator_down" : "message.aetherium.elevator_up").formatted(Formatting.AQUA), true);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(2) == 0) {
            world.addParticle(ParticleTypes.PORTAL,
                    pos.getX() + 0.2 + random.nextDouble() * 0.6,
                    pos.getY() + 1.05,
                    pos.getZ() + 0.2 + random.nextDouble() * 0.6,
                    0.0, 0.05, 0.0);
        }
    }
}
