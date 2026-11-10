package com.aetherium.registry;

import com.aetherium.AetheriumOdyssey;
import com.aetherium.item.*;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {
    // 1. Ores, Ingots, Gems & Crafting Components
    public static final Item RAW_AETHERIUM = register("raw_aetherium", new Item(new Item.Settings()));
    public static final Item AETHERIUM_SHARD = register("aetherium_shard", new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Item AETHERIUM_INGOT = register("aetherium_ingot", new Item(new Item.Settings().rarity(Rarity.RARE).fireproof()));
    public static final Item PYROSITE_CRYSTAL = register("pyrosite_crystal", new Item(new Item.Settings().rarity(Rarity.UNCOMMON).fireproof()));
    public static final Item PYROSITE_INGOT = register("pyrosite_ingot", new Item(new Item.Settings().rarity(Rarity.RARE).fireproof()));
    public static final Item VOIDITE_GEM = register("voidite_gem", new Item(new Item.Settings().rarity(Rarity.RARE).fireproof()));
    public static final Item VOIDITE_INGOT = register("voidite_ingot", new Item(new Item.Settings().rarity(Rarity.EPIC).fireproof()));

    public static final Item CELESTIAL_CORE = register("celestial_core",
            new PassiveRelicItem(new Item.Settings().rarity(Rarity.EPIC).fireproof(),
                    "tooltip.aetherium.celestial_core", Formatting.LIGHT_PURPLE, true, PassiveRelicItem.RelicType.NONE));

    public static final Item RUNE_OF_STORMS = register("rune_of_storms",
            new PassiveRelicItem(new Item.Settings().rarity(Rarity.RARE),
                    "tooltip.aetherium.rune_of_storms", Formatting.AQUA, true, PassiveRelicItem.RelicType.NONE));

    public static final Item RUNE_OF_FLAMES = register("rune_of_flames",
            new PassiveRelicItem(new Item.Settings().rarity(Rarity.RARE).fireproof(),
                    "tooltip.aetherium.rune_of_flames", Formatting.GOLD, true, PassiveRelicItem.RelicType.NONE));

    public static final Item RUNE_OF_VOID = register("rune_of_void",
            new PassiveRelicItem(new Item.Settings().rarity(Rarity.RARE).fireproof(),
                    "tooltip.aetherium.rune_of_void", Formatting.DARK_PURPLE, true, PassiveRelicItem.RelicType.NONE));

    public static final Item ARCHON_SUMMONER = register("archon_summoner",
            new PassiveRelicItem(new Item.Settings().rarity(Rarity.EPIC).fireproof().maxCount(16),
                    "tooltip.aetherium.archon_summoner", Formatting.LIGHT_PURPLE, true, PassiveRelicItem.RelicType.NONE));

    public static final Item AETHER_CODEX = register("aether_codex",
            new AetherCodexItem(new Item.Settings().rarity(Rarity.RARE).maxCount(1)));

    public static final Item AETHER_PORTAL_KEY = register("aether_portal_key",
            new AetherPortalKeyItem(new Item.Settings().rarity(Rarity.EPIC).maxCount(1).fireproof()));

    public static final Item AETHER_STEED_WHISTLE = register("aether_steed_whistle",
            new CelestialSteedWhistleItem(new Item.Settings().rarity(Rarity.EPIC).maxCount(1).fireproof()));

    public static final Item STARLIGHT_BOW = register("starlight_bow",
            new StarlightBowItem(new Item.Settings().rarity(Rarity.EPIC).maxDamage(450).fireproof()));

    public static final Item AETHER_GRAPPLE = register("aether_grapple",
            new AetherGrappleItem(new Item.Settings().rarity(Rarity.RARE).maxDamage(350).fireproof()));

    public static final Item SHRINE_SCROLL = register("shrine_scroll",
            new AetherShrineScrollItem(new Item.Settings().rarity(Rarity.RARE).maxCount(16).fireproof()));

    public static final Item PRIMORDIAL_VOID_ESSENCE = register("primordial_void_essence",
            new PassiveRelicItem(new Item.Settings().rarity(Rarity.EPIC).fireproof(),
                    "tooltip.aetherium.primordial_void_essence", Formatting.DARK_PURPLE, true, PassiveRelicItem.RelicType.NONE));

    public static final Item INFINITY_BLADE = register("infinity_blade",
            new InfinityBladeItem(ModToolMaterials.INFINITY, new Item.Settings()
                    .rarity(Rarity.EPIC).fireproof()
                    .attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.INFINITY, 9, -2.0f))));

    public static final Item CHRONOS_HOURGLASS = register("chronos_hourglass",
            new ChronosHourglassItem(new Item.Settings().rarity(Rarity.EPIC).maxCount(1).fireproof()));

    public static final Item VOID_POUCH = register("void_pouch",
            new VoidPouchItem(new Item.Settings().rarity(Rarity.RARE).maxCount(1).fireproof()));

    public static final Item PYROSITE_HELMET = register("pyrosite_helmet",
            new PyrositeArmorItem(ModArmorMaterials.PYROSITE, ArmorItem.Type.HELMET,
                    new Item.Settings().rarity(Rarity.RARE).fireproof().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(48))));

    public static final Item PYROSITE_CHESTPLATE = register("pyrosite_chestplate",
            new PyrositeArmorItem(ModArmorMaterials.PYROSITE, ArmorItem.Type.CHESTPLATE,
                    new Item.Settings().rarity(Rarity.RARE).fireproof().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(48))));

    public static final Item PYROSITE_LEGGINGS = register("pyrosite_leggings",
            new PyrositeArmorItem(ModArmorMaterials.PYROSITE, ArmorItem.Type.LEGGINGS,
                    new Item.Settings().rarity(Rarity.RARE).fireproof().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(48))));

    public static final Item PYROSITE_BOOTS = register("pyrosite_boots",
            new PyrositeArmorItem(ModArmorMaterials.PYROSITE, ArmorItem.Type.BOOTS,
                    new Item.Settings().rarity(Rarity.RARE).fireproof().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(48))));

    public static final Item VOIDITE_HELMET = register("voidite_helmet",
            new VoiditeArmorItem(ModArmorMaterials.VOIDITE, ArmorItem.Type.HELMET,
                    new Item.Settings().rarity(Rarity.EPIC).fireproof().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(55))));

    public static final Item VOIDITE_CHESTPLATE = register("voidite_chestplate",
            new VoiditeArmorItem(ModArmorMaterials.VOIDITE, ArmorItem.Type.CHESTPLATE,
                    new Item.Settings().rarity(Rarity.EPIC).fireproof().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(55))));

    public static final Item VOIDITE_LEGGINGS = register("voidite_leggings",
            new VoiditeArmorItem(ModArmorMaterials.VOIDITE, ArmorItem.Type.LEGGINGS,
                    new Item.Settings().rarity(Rarity.EPIC).fireproof().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(55))));

    public static final Item VOIDITE_BOOTS = register("voidite_boots",
            new VoiditeArmorItem(ModArmorMaterials.VOIDITE, ArmorItem.Type.BOOTS,
                    new Item.Settings().rarity(Rarity.EPIC).fireproof().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(55))));

    // 2. Tools
    public static final Item AETHERIUM_PICKAXE = register("aetherium_pickaxe",
            new AetheriumPickaxeItem(ModToolMaterials.AETHERIUM, new Item.Settings()
                    .rarity(Rarity.RARE).fireproof()
                    .attributeModifiers(PickaxeItem.createAttributeModifiers(ModToolMaterials.AETHERIUM, 1.5f, -2.7f))));

    public static final Item AETHERIUM_AXE = register("aetherium_axe",
            new AxeItem(ModToolMaterials.AETHERIUM, new Item.Settings()
                    .rarity(Rarity.RARE).fireproof()
                    .attributeModifiers(AxeItem.createAttributeModifiers(ModToolMaterials.AETHERIUM, 6.0f, -2.9f))));

    public static final Item AETHERIUM_SHOVEL = register("aetherium_shovel",
            new ShovelItem(ModToolMaterials.AETHERIUM, new Item.Settings()
                    .rarity(Rarity.RARE).fireproof()
                    .attributeModifiers(ShovelItem.createAttributeModifiers(ModToolMaterials.AETHERIUM, 1.5f, -3.0f))));

    public static final Item AETHERIUM_HOE = register("aetherium_hoe",
            new HoeItem(ModToolMaterials.AETHERIUM, new Item.Settings()
                    .rarity(Rarity.RARE).fireproof()
                    .attributeModifiers(HoeItem.createAttributeModifiers(ModToolMaterials.AETHERIUM, -3.0f, 0.0f))));

    // 3. Legendary Weapons
    public static final Item AETHERIUM_SWORD = register("aetherium_sword",
            new AetheriumSwordItem(ModToolMaterials.AETHERIUM, new Item.Settings()
                    .rarity(Rarity.RARE).fireproof()
                    .attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.AETHERIUM, 4, -2.2f))));

    public static final Item PYROSITE_GREATSWORD = register("pyrosite_greatsword",
            new PyrositeGreatswordItem(ModToolMaterials.PYROSITE, new Item.Settings()
                    .rarity(Rarity.EPIC).fireproof()
                    .attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.PYROSITE, 6, -2.6f))));

    public static final Item VOID_SCYTHE = register("void_scythe",
            new VoidScytheItem(ModToolMaterials.VOIDITE, new Item.Settings()
                    .rarity(Rarity.EPIC).fireproof()
                    .attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.VOIDITE, 5, -2.1f))));

    public static final Item CELESTIAL_HALBERD = register("celestial_halberd",
            new CelestialHalberdItem(ModToolMaterials.CELESTIAL, new Item.Settings()
                    .rarity(Rarity.EPIC).fireproof()
                    .attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.CELESTIAL, 8, -2.3f))));

    // 4. Magical Staves, Talisman & Passive Rings/Charms
    public static final Item STAFF_OF_TEMPEST = register("staff_of_tempest",
            new TempestStaffItem(new Item.Settings().rarity(Rarity.RARE).maxDamage(250).fireproof()));

    public static final Item STAFF_OF_METEORS = register("staff_of_meteors",
            new MeteorStaffItem(new Item.Settings().rarity(Rarity.RARE).maxDamage(250).fireproof()));

    public static final Item STAFF_OF_HEALING = register("staff_of_healing",
            new HealingStaffItem(new Item.Settings().rarity(Rarity.EPIC).maxDamage(200).fireproof()));

    public static final Item WIND_TALISMAN = register("wind_talisman",
            new WindTalismanItem(new Item.Settings().rarity(Rarity.RARE).maxDamage(300).fireproof()));

    public static final Item RING_OF_HASTE = register("ring_of_haste",
            new PassiveRelicItem(new Item.Settings().rarity(Rarity.RARE).maxCount(1).fireproof(),
                    "tooltip.aetherium.ring_of_haste", Formatting.YELLOW, true, PassiveRelicItem.RelicType.RING_OF_HASTE));

    public static final Item RING_OF_IMMORTALITY = register("ring_of_immortality",
            new PassiveRelicItem(new Item.Settings().rarity(Rarity.EPIC).maxCount(1).fireproof(),
                    "tooltip.aetherium.ring_of_immortality", Formatting.GOLD, true, PassiveRelicItem.RelicType.RING_OF_IMMORTALITY));

    public static final Item MAGNET_CHARM = register("magnet_charm",
            new MagnetCharmItem(new Item.Settings().rarity(Rarity.RARE).maxCount(1).fireproof()));

    // 5. Aetherium Armor Set
    public static final Item AETHERIUM_HELMET = register("aetherium_helmet",
            new AetheriumArmorItem(ModArmorMaterials.AETHERIUM, ArmorItem.Type.HELMET,
                    new Item.Settings().rarity(Rarity.RARE).fireproof().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(45))));

    public static final Item AETHERIUM_CHESTPLATE = register("aetherium_chestplate",
            new AetheriumArmorItem(ModArmorMaterials.AETHERIUM, ArmorItem.Type.CHESTPLATE,
                    new Item.Settings().rarity(Rarity.RARE).fireproof().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(45))));

    public static final Item AETHERIUM_LEGGINGS = register("aetherium_leggings",
            new AetheriumArmorItem(ModArmorMaterials.AETHERIUM, ArmorItem.Type.LEGGINGS,
                    new Item.Settings().rarity(Rarity.RARE).fireproof().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(45))));

    public static final Item AETHERIUM_BOOTS = register("aetherium_boots",
            new AetheriumArmorItem(ModArmorMaterials.AETHERIUM, ArmorItem.Type.BOOTS,
                    new Item.Settings().rarity(Rarity.RARE).fireproof().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(45))));

    // 6. Mystical Foods & Elixirs
    public static final Item AETHER_APPLE = register("aether_apple",
            new Item(new Item.Settings().rarity(Rarity.RARE).food(new FoodComponent.Builder()
                    .nutrition(6)
                    .saturationModifier(1.2f)
                    .alwaysEdible()
                    .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 300, 1), 1.0f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 1200, 2), 1.0f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 1), 1.0f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.0f)
                    .build())));

    public static final Item CELESTIAL_AMBROSIA = register("celestial_ambrosia",
            new PassiveRelicItem(new Item.Settings().rarity(Rarity.EPIC).fireproof().food(new FoodComponent.Builder()
                    .nutrition(12)
                    .saturationModifier(1.5f)
                    .alwaysEdible()
                    .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 600, 2), 1.0f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 2400, 1), 1.0f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 2400, 1), 1.0f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 3600, 0), 1.0f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 2400, 1), 1.0f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 3), 1.0f)
                    .build()),
                    "tooltip.aetherium.celestial_ambrosia", Formatting.LIGHT_PURPLE, true, PassiveRelicItem.RelicType.NONE));

    public static final Item ELIXIR_OF_FLIGHT = register("elixir_of_flight",
            new PassiveRelicItem(new Item.Settings().rarity(Rarity.RARE).maxCount(16).food(new FoodComponent.Builder()
                    .nutrition(2)
                    .saturationModifier(0.8f)
                    .alwaysEdible()
                    .statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 1200, 3), 1.0f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 1200, 0), 1.0f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1200, 2), 1.0f)
                    .build()),
                    "tooltip.aetherium.elixir_of_flight", Formatting.AQUA, true, PassiveRelicItem.RelicType.NONE));

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(AetheriumOdyssey.MOD_ID, name), item);
    }

    public static void init() {
        // Trigger static initialization
    }
}
