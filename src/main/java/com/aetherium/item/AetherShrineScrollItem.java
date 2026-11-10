package com.aetherium.item;

import com.aetherium.registry.ModBlocks;
import com.aetherium.registry.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class AetherShrineScrollItem extends Item {
    public AetherShrineScrollItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            user.getItemCooldownManager().set(this, 100);
            BlockPos center = user.getBlockPos().offset(user.getHorizontalFacing(), 7);
            generateCelestialTemple(serverWorld, center);

            if (!user.isCreative()) {
                stack.decrement(1);
            }
            user.sendMessage(Text.translatable("message.aetherium.shrine_built").formatted(Formatting.GOLD, Formatting.BOLD), true);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    public static void generateCelestialTemple(ServerWorld world, BlockPos center) {
        // 9x9 floor of Quartz & Aetherium
        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                BlockPos floorPos = center.add(dx, -1, dz);
                boolean border = Math.abs(dx) == 4 || Math.abs(dz) == 4;
                boolean innerRing = Math.abs(dx) == 2 && Math.abs(dz) == 2;
                if (border) {
                    world.setBlockState(floorPos, Blocks.CHISELED_QUARTZ_BLOCK.getDefaultState());
                } else if (innerRing) {
                    world.setBlockState(floorPos, ModBlocks.AETHER_LAMP.getDefaultState());
                } else if (dx == 0 && dz == 0) {
                    world.setBlockState(floorPos, ModBlocks.AETHERIUM_BLOCK.getDefaultState());
                } else {
                    world.setBlockState(floorPos, Blocks.QUARTZ_BRICKS.getDefaultState());
                }

                // Clear interior air up to 6 blocks high
                for (int dy = 0; dy <= 5; dy++) {
                    world.setBlockState(center.add(dx, dy, dz), Blocks.AIR.getDefaultState());
                }
            }
        }

        // 4 Corner Pillars with Starlight Lamps on top
        int[][] corners = new int[][]{{-4, -4}, {-4, 4}, {4, -4}, {4, 4}};
        for (int[] c : corners) {
            for (int dy = 0; dy <= 3; dy++) {
                world.setBlockState(center.add(c[0], dy, c[1]), Blocks.QUARTZ_PILLAR.getDefaultState());
            }
            world.setBlockState(center.add(c[0], 4, c[1]), ModBlocks.AETHER_LAMP.getDefaultState());
        }

        // Center Ascension Altar & 2 Healing Pylons
        world.setBlockState(center, ModBlocks.AETHER_ALTAR.getDefaultState());
        world.setBlockState(center.add(-3, 0, 0), ModBlocks.HEALING_PYLON.getDefaultState());
        world.setBlockState(center.add(3, 0, 0), ModBlocks.HEALING_PYLON.getDefaultState());

        // Treasure Chest behind the altar
        BlockPos chestPos = center.add(0, 0, -2);
        world.setBlockState(chestPos, Blocks.CHEST.getDefaultState());
        BlockEntity be = world.getBlockEntity(chestPos);
        if (be instanceof ChestBlockEntity chest) {
            chest.setStack(4, new ItemStack(ModItems.ARCHON_SUMMONER, 1));
            chest.setStack(11, new ItemStack(ModItems.AETHERIUM_INGOT, 4));
            chest.setStack(12, new ItemStack(ModItems.AETHER_APPLE, 2));
            chest.setStack(13, new ItemStack(ModItems.AETHER_PORTAL_KEY, 1));
            chest.setStack(14, new ItemStack(ModItems.ELIXIR_OF_FLIGHT, 2));
            chest.setStack(15, new ItemStack(ModItems.PYROSITE_INGOT, 3));
            chest.setStack(22, new ItemStack(ModItems.AETHER_STEED_WHISTLE, 1));
        }

        world.spawnParticles(ParticleTypes.END_ROD, center.getX() + 0.5, center.getY() + 2.0, center.getZ() + 0.5, 100, 3.0, 2.0, 3.0, 0.15);
        world.playSound(null, center, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 1.5f, 1.1f);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.shrine_scroll").formatted(Formatting.GOLD));
    }
}
