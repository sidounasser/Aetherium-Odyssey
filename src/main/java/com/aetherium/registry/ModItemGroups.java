package com.aetherium.registry;

import com.aetherium.AetheriumOdyssey;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup AETHERIUM_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of(AetheriumOdyssey.MOD_ID, "aetherium_group"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.CELESTIAL_HALBERD))
                    .displayName(Text.translatable("itemGroup.aetherium.aetherium_group"))
                    .entries((displayContext, entries) -> {
                        // Guide & Boss Summoner
                        entries.add(ModItems.AETHER_CODEX);
                        entries.add(ModItems.AETHER_PORTAL_KEY);
                        entries.add(ModItems.AETHER_STEED_WHISTLE);
                        entries.add(ModItems.SHRINE_SCROLL);
                        entries.add(ModItems.ARCHON_SUMMONER);
                        entries.add(ModItems.CELESTIAL_CORE);

                        // Functional & Ritual Blocks
                        entries.add(ModBlocks.AETHER_ALTAR);
                        entries.add(ModBlocks.HEALING_PYLON);
                        entries.add(ModBlocks.ELEVATOR_PAD);
                        entries.add(ModBlocks.AETHER_LAMP);

                        // Ores & Storage Blocks
                        entries.add(ModBlocks.AETHERIUM_ORE);
                        entries.add(ModBlocks.DEEPSLATE_AETHERIUM_ORE);
                        entries.add(ModBlocks.PYROSITE_ORE);
                        entries.add(ModBlocks.VOIDITE_ORE);
                        entries.add(ModBlocks.RAW_AETHERIUM_BLOCK);
                        entries.add(ModBlocks.AETHERIUM_BLOCK);
                        entries.add(ModBlocks.PYROSITE_BLOCK);
                        entries.add(ModBlocks.VOIDITE_BLOCK);

                        // Materials & Runes
                        entries.add(ModItems.RAW_AETHERIUM);
                        entries.add(ModItems.AETHERIUM_SHARD);
                        entries.add(ModItems.AETHERIUM_INGOT);
                        entries.add(ModItems.PYROSITE_CRYSTAL);
                        entries.add(ModItems.PYROSITE_INGOT);
                        entries.add(ModItems.VOIDITE_GEM);
                        entries.add(ModItems.VOIDITE_INGOT);
                        entries.add(ModItems.RUNE_OF_STORMS);
                        entries.add(ModItems.RUNE_OF_FLAMES);
                        entries.add(ModItems.RUNE_OF_VOID);

                        // Legendary Weapons
                        entries.add(ModItems.INFINITY_BLADE);
                        entries.add(ModItems.CELESTIAL_HALBERD);
                        entries.add(ModItems.AETHERIUM_SWORD);
                        entries.add(ModItems.PYROSITE_GREATSWORD);
                        entries.add(ModItems.VOID_SCYTHE);
                        entries.add(ModItems.STARLIGHT_BOW);
                        entries.add(ModItems.AETHER_GRAPPLE);
                        entries.add(ModItems.CHRONOS_HOURGLASS);
                        entries.add(ModItems.VOID_POUCH);
                        entries.add(ModItems.PRIMORDIAL_VOID_ESSENCE);

                        // Tools
                        entries.add(ModItems.AETHERIUM_PICKAXE);
                        entries.add(ModItems.AETHERIUM_AXE);
                        entries.add(ModItems.AETHERIUM_SHOVEL);
                        entries.add(ModItems.AETHERIUM_HOE);

                        // Staves, Talisman & Relics
                        entries.add(ModItems.STAFF_OF_TEMPEST);
                        entries.add(ModItems.STAFF_OF_METEORS);
                        entries.add(ModItems.STAFF_OF_HEALING);
                        entries.add(ModItems.WIND_TALISMAN);
                        entries.add(ModItems.RING_OF_HASTE);
                        entries.add(ModItems.RING_OF_IMMORTALITY);
                        entries.add(ModItems.MAGNET_CHARM);

                        // Armor Set
                        entries.add(ModItems.AETHERIUM_HELMET);
                        entries.add(ModItems.AETHERIUM_CHESTPLATE);
                        entries.add(ModItems.AETHERIUM_LEGGINGS);
                        entries.add(ModItems.AETHERIUM_BOOTS);
                        entries.add(ModItems.PYROSITE_HELMET);
                        entries.add(ModItems.PYROSITE_CHESTPLATE);
                        entries.add(ModItems.PYROSITE_LEGGINGS);
                        entries.add(ModItems.PYROSITE_BOOTS);
                        entries.add(ModItems.VOIDITE_HELMET);
                        entries.add(ModItems.VOIDITE_CHESTPLATE);
                        entries.add(ModItems.VOIDITE_LEGGINGS);
                        entries.add(ModItems.VOIDITE_BOOTS);

                        // Mystical Foods & Elixirs
                        entries.add(ModItems.AETHER_APPLE);
                        entries.add(ModItems.CELESTIAL_AMBROSIA);
                        entries.add(ModItems.ELIXIR_OF_FLIGHT);
                    })
                    .build()
    );

    public static void init() {
        // Trigger static initialization
    }
}
