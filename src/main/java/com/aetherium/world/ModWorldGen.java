package com.aetherium.world;

import com.aetherium.AetheriumOdyssey;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

public class ModWorldGen {
    public static final RegistryKey<PlacedFeature> AETHERIUM_ORE_PLACED_KEY = RegistryKey.of(
            RegistryKeys.PLACED_FEATURE, Identifier.of(AetheriumOdyssey.MOD_ID, "aetherium_ore_placed"));

    public static final RegistryKey<PlacedFeature> PYROSITE_ORE_PLACED_KEY = RegistryKey.of(
            RegistryKeys.PLACED_FEATURE, Identifier.of(AetheriumOdyssey.MOD_ID, "pyrosite_ore_placed"));

    public static final RegistryKey<PlacedFeature> VOIDITE_ORE_PLACED_KEY = RegistryKey.of(
            RegistryKeys.PLACED_FEATURE, Identifier.of(AetheriumOdyssey.MOD_ID, "voidite_ore_placed"));

    public static final RegistryKey<PlacedFeature> AETHER_GEODE_PLACED_KEY = RegistryKey.of(
            RegistryKeys.PLACED_FEATURE, Identifier.of(AetheriumOdyssey.MOD_ID, "aether_geode_placed"));

    public static void init() {
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                AETHERIUM_ORE_PLACED_KEY
        );

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.LOCAL_MODIFICATIONS,
                AETHER_GEODE_PLACED_KEY
        );

        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheNether(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                PYROSITE_ORE_PLACED_KEY
        );

        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                VOIDITE_ORE_PLACED_KEY
        );
    }
}
