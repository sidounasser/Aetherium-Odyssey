package com.aetherium.item;

import com.aetherium.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
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

public class AetheriumPickaxeItem extends PickaxeItem {
    public AetheriumPickaxeItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && miner instanceof PlayerEntity player && !player.isSneaking()) {
            if (state.isIn(BlockTags.PICKAXE_MINEABLE)) {
                // Mine adjacent blocks in a 3x1x3 horizontal & vertical cross around the target if stone/deepslate/netherrack/ore
                BlockPos[] offsets = new BlockPos[]{
                        pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west()
                };
                for (BlockPos extraPos : offsets) {
                    BlockState extraState = world.getBlockState(extraPos);
                    if (extraState.isIn(BlockTags.PICKAXE_MINEABLE)
                            && extraState.getHardness(world, extraPos) > 0
                            && extraState.getHardness(world, extraPos) <= 10.0f
                            && world.getBlockEntity(extraPos) == null) {
                        world.breakBlock(extraPos, true, player);
                        stack.damage(1, miner, EquipmentSlot.MAINHAND);
                    }
                }
            }
        }
        return super.postMine(stack, world, state, pos, miner);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            user.getItemCooldownManager().set(this, 100); // 5s cooldown

            int radius = 12;
            int aetheriumCount = 0;
            int valuableCount = 0;
            BlockPos nearest = null;
            double bestDist = Double.MAX_VALUE;
            BlockPos center = user.getBlockPos();

            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        BlockPos check = center.add(dx, dy, dz);
                        Block b = world.getBlockState(check).getBlock();
                        boolean isCustomOre = b == ModBlocks.AETHERIUM_ORE || b == ModBlocks.DEEPSLATE_AETHERIUM_ORE
                                || b == ModBlocks.PYROSITE_ORE || b == ModBlocks.VOIDITE_ORE;
                        boolean isVanillaRare = b == Blocks.DIAMOND_ORE || b == Blocks.DEEPSLATE_DIAMOND_ORE
                                || b == Blocks.ANCIENT_DEBRIS || b == Blocks.EMERALD_ORE || b == Blocks.DEEPSLATE_EMERALD_ORE;

                        if (isCustomOre || isVanillaRare) {
                            if (isCustomOre) aetheriumCount++;
                            else valuableCount++;

                            double dist = center.getSquaredDistance(check);
                            if (dist < bestDist) {
                                bestDist = dist;
                                nearest = check;
                            }
                        }
                    }
                }
            }

            serverWorld.spawnParticles(ParticleTypes.END_ROD, user.getX(), user.getY() + 1.0, user.getZ(), 25, 0.8, 0.5, 0.8, 0.08);
            world.playSound(null, center, SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.PLAYERS, 1.2f, 1.4f);

            if (nearest != null) {
                int blocksAway = (int) Math.sqrt(bestDist);
                user.sendMessage(Text.translatable("message.aetherium.radar_found", aetheriumCount, valuableCount, blocksAway)
                        .formatted(Formatting.AQUA), true);
            } else {
                user.sendMessage(Text.translatable("message.aetherium.radar_empty").formatted(Formatting.GRAY), true);
            }
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.aetherium_pickaxe.passive").formatted(Formatting.AQUA));
        tooltip.add(Text.translatable("tooltip.aetherium.aetherium_pickaxe.active").formatted(Formatting.GOLD));
    }
}
