package com.aetherium.item;

import com.aetherium.AetheriumOdyssey;
import com.aetherium.registry.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AetherPortalKeyItem extends Item {
    public static final RegistryKey<World> AETHER_REALM_KEY = RegistryKey.of(
            RegistryKeys.WORLD, Identifier.of(AetheriumOdyssey.MOD_ID, "aether_realm"));

    public AetherPortalKeyItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && user instanceof ServerPlayerEntity serverPlayer) {
            MinecraftServer server = serverPlayer.getServer();
            if (server == null) return TypedActionResult.fail(stack);

            user.getItemCooldownManager().set(this, 100); // 5s cooldown

            boolean inAether = world.getRegistryKey().equals(AETHER_REALM_KEY);
            RegistryKey<World> targetKey = inAether ? World.OVERWORLD : AETHER_REALM_KEY;
            ServerWorld targetWorld = server.getWorld(targetKey);

            if (targetWorld == null) {
                user.sendMessage(Text.translatable("message.aetherium.dimension_unavailable").formatted(Formatting.RED), true);
                return TypedActionResult.fail(stack);
            }

            BlockPos targetPos;
            if (!inAether) {
                // Going to Aether Realm: build or locate safe sanctuary platform
                int tx = serverPlayer.getBlockX();
                int tz = serverPlayer.getBlockZ();
                int topY = targetWorld.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, tx, tz);
                if (topY <= targetWorld.getBottomY() + 5) {
                    topY = 100;
                    buildSanctuaryPlatform(targetWorld, new BlockPos(tx, topY - 1, tz));
                } else {
                    ensureSafeFooting(targetWorld, new BlockPos(tx, topY - 1, tz));
                }
                targetPos = new BlockPos(tx, topY, tz);
            } else {
                // Returning to Overworld
                BlockPos spawn = serverPlayer.getSpawnPointPosition();
                if (spawn == null) {
                    spawn = targetWorld.getSpawnPos();
                }
                int topY = targetWorld.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, spawn.getX(), spawn.getZ());
                targetPos = new BlockPos(spawn.getX(), Math.max(topY, 64), spawn.getZ());
            }

            serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200, 0));
            serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 160, 2));

            TeleportTarget teleportTarget = new TeleportTarget(
                    targetWorld,
                    new Vec3d(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5),
                    Vec3d.ZERO,
                    serverPlayer.getYaw(),
                    serverPlayer.getPitch(),
                    TeleportTarget.NO_OP
            );
            serverPlayer.teleportTo(teleportTarget);

            targetWorld.spawnParticles(ParticleTypes.PORTAL, targetPos.getX() + 0.5, targetPos.getY() + 1.0, targetPos.getZ() + 0.5, 60, 0.8, 1.0, 0.8, 0.2);
            targetWorld.spawnParticles(ParticleTypes.END_ROD, targetPos.getX() + 0.5, targetPos.getY() + 1.0, targetPos.getZ() + 0.5, 40, 0.8, 1.0, 0.8, 0.15);
            targetWorld.playSound(null, targetPos, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.7f, 1.2f);

            String msgKey = inAether ? "message.aetherium.returned_overworld" : "message.aetherium.entered_aether_realm";
            serverPlayer.sendMessage(Text.translatable(msgKey).formatted(Formatting.AQUA, Formatting.BOLD), true);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    public static void buildSanctuaryPlatform(ServerWorld world, BlockPos center) {
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                BlockPos p = center.add(dx, 0, dz);
                if (Math.abs(dx) == 3 || Math.abs(dz) == 3) {
                    world.setBlockState(p, ModBlocks.AETHER_LAMP.getDefaultState());
                } else {
                    world.setBlockState(p, ModBlocks.AETHERIUM_BLOCK.getDefaultState());
                }
                // Clear 4 blocks of air above
                for (int dy = 1; dy <= 4; dy++) {
                    world.setBlockState(p.up(dy), Blocks.AIR.getDefaultState());
                }
            }
        }
        world.setBlockState(center.add(2, 1, 0), ModBlocks.HEALING_PYLON.getDefaultState());
        world.setBlockState(center.add(-2, 1, 0), ModBlocks.AETHER_ALTAR.getDefaultState());
    }

    private static void ensureSafeFooting(ServerWorld world, BlockPos center) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos p = center.add(dx, 0, dz);
                if (world.getBlockState(p).isAir()) {
                    world.setBlockState(p, ModBlocks.AETHERIUM_BLOCK.getDefaultState());
                }
            }
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.aether_portal_key").formatted(Formatting.LIGHT_PURPLE));
    }
}
