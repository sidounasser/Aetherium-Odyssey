package com.aetherium.command;

import com.aetherium.entity.AetheriumArchonEntity;
import com.aetherium.entity.ModEntities;
import com.aetherium.item.AetherCodexItem;
import com.aetherium.registry.ModBlocks;
import com.aetherium.registry.ModItems;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ModCommands {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("aetherium")
                    .then(CommandManager.literal("guide")
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                                AetherCodexItem.sendCodexGuide(player);
                                return 1;
                            }))
                    .then(CommandManager.literal("codex")
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                                player.getInventory().insertStack(new ItemStack(ModItems.AETHER_CODEX));
                                player.sendMessage(Text.translatable("message.aetherium.received_codex").formatted(Formatting.AQUA), false);
                                return 1;
                            }))
                    .then(CommandManager.literal("kit")
                            .requires(source -> source.hasPermissionLevel(2))
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                                player.getInventory().insertStack(new ItemStack(ModItems.AETHER_CODEX));
                                player.getInventory().insertStack(new ItemStack(ModItems.AETHER_PORTAL_KEY));
                                player.getInventory().insertStack(new ItemStack(ModItems.AETHER_STEED_WHISTLE));
                                player.getInventory().insertStack(new ItemStack(ModItems.STARLIGHT_BOW));
                                player.getInventory().insertStack(new ItemStack(ModItems.AETHER_GRAPPLE));
                                player.getInventory().insertStack(new ItemStack(ModItems.SHRINE_SCROLL, 4));
                                player.getInventory().insertStack(new ItemStack(ModItems.CELESTIAL_HALBERD));
                                player.getInventory().insertStack(new ItemStack(ModItems.AETHERIUM_SWORD));
                                player.getInventory().insertStack(new ItemStack(ModItems.PYROSITE_GREATSWORD));
                                player.getInventory().insertStack(new ItemStack(ModItems.VOID_SCYTHE));
                                player.getInventory().insertStack(new ItemStack(ModItems.AETHERIUM_PICKAXE));
                                player.getInventory().insertStack(new ItemStack(ModItems.STAFF_OF_TEMPEST));
                                player.getInventory().insertStack(new ItemStack(ModItems.STAFF_OF_METEORS));
                                player.getInventory().insertStack(new ItemStack(ModItems.STAFF_OF_HEALING));
                                player.getInventory().insertStack(new ItemStack(ModItems.WIND_TALISMAN));
                                player.getInventory().insertStack(new ItemStack(ModItems.RING_OF_HASTE));
                                player.getInventory().insertStack(new ItemStack(ModItems.RING_OF_IMMORTALITY));
                                player.getInventory().insertStack(new ItemStack(ModItems.MAGNET_CHARM));
                                player.getInventory().insertStack(new ItemStack(ModItems.AETHERIUM_HELMET));
                                player.getInventory().insertStack(new ItemStack(ModItems.AETHERIUM_CHESTPLATE));
                                player.getInventory().insertStack(new ItemStack(ModItems.AETHERIUM_LEGGINGS));
                                player.getInventory().insertStack(new ItemStack(ModItems.AETHERIUM_BOOTS));
                                player.getInventory().insertStack(new ItemStack(ModItems.CELESTIAL_AMBROSIA, 8));
                                player.getInventory().insertStack(new ItemStack(ModBlocks.AETHER_ALTAR, 1));
                                player.getInventory().insertStack(new ItemStack(ModItems.ARCHON_SUMMONER, 2));
                                player.sendMessage(Text.translatable("message.aetherium.kit_granted").formatted(Formatting.GOLD, Formatting.BOLD), false);
                                return 1;
                            }))
                    .then(CommandManager.literal("summon_boss")
                            .requires(source -> source.hasPermissionLevel(2))
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
                                ServerWorld world = context.getSource().getWorld();
                                AetheriumArchonEntity boss = ModEntities.AETHERIUM_ARCHON.create(world);
                                if (boss != null) {
                                    boss.refreshPositionAndAngles(player.getX() + 3.0, player.getY(), player.getZ() + 3.0, 0.0f, 0.0f);
                                    boss.initialize(world, world.getLocalDifficulty(player.getBlockPos()), SpawnReason.COMMAND, null);
                                    world.spawnEntity(boss);
                                    player.sendMessage(Text.translatable("message.aetherium.archon_awakened").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD), false);
                                }
                                return 1;
                            }))
            );
        });
    }
}
