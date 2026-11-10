package com.aetherium;

import com.aetherium.command.ModCommands;
import com.aetherium.entity.ModEntities;
import com.aetherium.registry.ModArmorMaterials;
import com.aetherium.registry.ModBlocks;
import com.aetherium.registry.ModItemGroups;
import com.aetherium.registry.ModItems;
import com.aetherium.world.ModWorldGen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AetheriumOdyssey implements ModInitializer {
    public static final String MOD_ID = "aetherium";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Aetherium Odyssey...");

        ModArmorMaterials.init();
        ModBlocks.init();
        ModItems.init();
        ModItemGroups.init();
        ModEntities.init();
        ModWorldGen.init();
        ModCommands.init();

        registerLootTableModifiers();

        LOGGER.info("Aetherium Odyssey loaded successfully!");
    }

    private void registerLootTableModifiers() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (EntityType.WARDEN.getLootTableId().equals(key)
                    || EntityType.WITHER.getLootTableId().equals(key)
                    || EntityType.ENDER_DRAGON.getLootTableId().equals(key)) {
                LootPool.Builder pool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0f))
                        .with(ItemEntry.builder(ModItems.AETHERIUM_INGOT))
                        .with(ItemEntry.builder(ModItems.ARCHON_SUMMONER));
                tableBuilder.pool(pool);
            }

            if (LootTables.ANCIENT_CITY_CHEST.equals(key)
                    || LootTables.END_CITY_TREASURE_CHEST.equals(key)
                    || LootTables.BASTION_TREASURE_CHEST.equals(key)) {
                LootPool.Builder chestPool = LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(1.0f, 2.0f))
                        .with(ItemEntry.builder(ModItems.AETHERIUM_SHARD).weight(4))
                        .with(ItemEntry.builder(ModItems.AETHER_APPLE).weight(2))
                        .with(ItemEntry.builder(ModItems.RUNE_OF_STORMS).weight(1))
                        .with(ItemEntry.builder(ModItems.RUNE_OF_FLAMES).weight(1))
                        .with(ItemEntry.builder(ModItems.RUNE_OF_VOID).weight(1));
                tableBuilder.pool(chestPool);
            }
        });
    }
}
