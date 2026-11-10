package com.aetherium.forge;

import net.minecraft.ChatFormatting;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ForgeItems {
    public enum ModTiers implements Tier {
        AETHERIUM(2650, 11.5f, 5.0f, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 22, () -> Ingredient.of(AETHERIUM_INGOT.get())),
        PYROSITE(2900, 12.0f, 6.0f, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 20, () -> Ingredient.of(PYROSITE_INGOT.get())),
        VOIDITE(3300, 13.5f, 7.0f, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 25, () -> Ingredient.of(VOIDITE_INGOT.get())),
        CELESTIAL(5000, 16.0f, 9.5f, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 30, () -> Ingredient.of(CELESTIAL_CORE.get())),
        INFINITY(9999, 22.0f, 14.0f, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 35, () -> Ingredient.of(PRIMORDIAL_VOID_ESSENCE.get()));

        private final int uses;
        private final float speed;
        private final float damage;
        private final TagKey<Block> incorrect;
        private final int enchantability;
        private final Supplier<Ingredient> repair;

        ModTiers(int uses, float speed, float damage, TagKey<Block> incorrect, int enchantability, Supplier<Ingredient> repair) {
            this.uses = uses;
            this.speed = speed;
            this.damage = damage;
            this.incorrect = incorrect;
            this.enchantability = enchantability;
            this.repair = repair;
        }

        @Override public int getUses() { return uses; }
        @Override public float getSpeed() { return speed; }
        @Override public float getAttackDamageBonus() { return damage; }
        @Override public TagKey<Block> getIncorrectBlocksForDrops() { return incorrect; }
        @Override public int getEnchantmentValue() { return enchantability; }
        @Override public Ingredient getRepairIngredient() { return repair.get(); }
    }

    // Guide, Boss & Dimension Items
    public static final RegistryObject<Item> AETHER_CODEX = reg("aether_codex",
            () -> new ForgeCustomItems.CodexItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1)));
    public static final RegistryObject<Item> AETHER_PORTAL_KEY = reg("aether_portal_key",
            () -> new ForgeCustomItems.PortalKeyItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> AETHER_STEED_WHISTLE = reg("aether_steed_whistle",
            () -> new ForgeCustomItems.SteedWhistleItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> SHRINE_SCROLL = reg("shrine_scroll",
            () -> new ForgeCustomItems.ShrineScrollItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(16).fireResistant()));
    public static final RegistryObject<Item> CHRONOS_HOURGLASS = reg("chronos_hourglass",
            () -> new ForgeCustomItems.HourglassItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> VOID_POUCH = reg("void_pouch",
            () -> new ForgeCustomItems.VoidPouchItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> ARCHON_SUMMONER = reg("archon_summoner",
            () -> new ForgeCustomItems.RelicItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(16).fireResistant(), "tooltip.aetherium.archon_summoner", ChatFormatting.LIGHT_PURPLE, true, 0));
    public static final RegistryObject<Item> CELESTIAL_CORE = reg("celestial_core",
            () -> new ForgeCustomItems.RelicItem(new Item.Properties().rarity(Rarity.EPIC).fireResistant(), "tooltip.aetherium.celestial_core", ChatFormatting.LIGHT_PURPLE, true, 0));
    public static final RegistryObject<Item> PRIMORDIAL_VOID_ESSENCE = reg("primordial_void_essence",
            () -> new ForgeCustomItems.RelicItem(new Item.Properties().rarity(Rarity.EPIC).fireResistant(), "tooltip.aetherium.primordial_void_essence", ChatFormatting.DARK_PURPLE, true, 0));

    // Materials & Runes
    public static final RegistryObject<Item> RAW_AETHERIUM = reg("raw_aetherium", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AETHERIUM_SHARD = reg("aetherium_shard", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> AETHERIUM_INGOT = reg("aetherium_ingot", () -> new Item(new Item.Properties().rarity(Rarity.RARE).fireResistant()));
    public static final RegistryObject<Item> PYROSITE_CRYSTAL = reg("pyrosite_crystal", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant()));
    public static final RegistryObject<Item> PYROSITE_INGOT = reg("pyrosite_ingot", () -> new Item(new Item.Properties().rarity(Rarity.RARE).fireResistant()));
    public static final RegistryObject<Item> VOIDITE_GEM = reg("voidite_gem", () -> new Item(new Item.Properties().rarity(Rarity.RARE).fireResistant()));
    public static final RegistryObject<Item> VOIDITE_INGOT = reg("voidite_ingot", () -> new Item(new Item.Properties().rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> RUNE_OF_STORMS = reg("rune_of_storms",
            () -> new ForgeCustomItems.RelicItem(new Item.Properties().rarity(Rarity.RARE), "tooltip.aetherium.rune_of_storms", ChatFormatting.AQUA, true, 0));
    public static final RegistryObject<Item> RUNE_OF_FLAMES = reg("rune_of_flames",
            () -> new ForgeCustomItems.RelicItem(new Item.Properties().rarity(Rarity.RARE).fireResistant(), "tooltip.aetherium.rune_of_flames", ChatFormatting.GOLD, true, 0));
    public static final RegistryObject<Item> RUNE_OF_VOID = reg("rune_of_void",
            () -> new ForgeCustomItems.RelicItem(new Item.Properties().rarity(Rarity.RARE).fireResistant(), "tooltip.aetherium.rune_of_void", ChatFormatting.DARK_PURPLE, true, 0));

    // Weapons
    public static final RegistryObject<Item> INFINITY_BLADE = reg("infinity_blade",
            () -> new ForgeCustomItems.InfinityBlade(ModTiers.INFINITY, new Item.Properties().rarity(Rarity.EPIC).fireResistant().attributes(SwordItem.createAttributes(ModTiers.INFINITY, 9, -2.0f))));
    public static final RegistryObject<Item> CELESTIAL_HALBERD = reg("celestial_halberd",
            () -> new ForgeCustomItems.CelestialHalberd(ModTiers.CELESTIAL, new Item.Properties().rarity(Rarity.EPIC).fireResistant().attributes(SwordItem.createAttributes(ModTiers.CELESTIAL, 8, -2.3f))));
    public static final RegistryObject<Item> AETHERIUM_SWORD = reg("aetherium_sword",
            () -> new ForgeCustomItems.AetherSword(ModTiers.AETHERIUM, new Item.Properties().rarity(Rarity.RARE).fireResistant().attributes(SwordItem.createAttributes(ModTiers.AETHERIUM, 4, -2.2f))));
    public static final RegistryObject<Item> PYROSITE_GREATSWORD = reg("pyrosite_greatsword",
            () -> new ForgeCustomItems.PyrositeSword(ModTiers.PYROSITE, new Item.Properties().rarity(Rarity.EPIC).fireResistant().attributes(SwordItem.createAttributes(ModTiers.PYROSITE, 6, -2.6f))));
    public static final RegistryObject<Item> VOID_SCYTHE = reg("void_scythe",
            () -> new ForgeCustomItems.VoidScythe(ModTiers.VOIDITE, new Item.Properties().rarity(Rarity.EPIC).fireResistant().attributes(SwordItem.createAttributes(ModTiers.VOIDITE, 5, -2.1f))));
    public static final RegistryObject<Item> STARLIGHT_BOW = reg("starlight_bow",
            () -> new ForgeCustomItems.StarlightBow(new Item.Properties().rarity(Rarity.EPIC).durability(450).fireResistant()));
    public static final RegistryObject<Item> AETHER_GRAPPLE = reg("aether_grapple",
            () -> new ForgeCustomItems.GrappleItem(new Item.Properties().rarity(Rarity.RARE).durability(350).fireResistant()));

    // Tools
    public static final RegistryObject<Item> AETHERIUM_PICKAXE = reg("aetherium_pickaxe",
            () -> new ForgeCustomItems.SeismicPickaxe(ModTiers.AETHERIUM, new Item.Properties().rarity(Rarity.RARE).fireResistant().attributes(PickaxeItem.createAttributes(ModTiers.AETHERIUM, 1.5f, -2.7f))));
    public static final RegistryObject<Item> AETHERIUM_AXE = reg("aetherium_axe",
            () -> new AxeItem(ModTiers.AETHERIUM, new Item.Properties().rarity(Rarity.RARE).fireResistant().attributes(AxeItem.createAttributes(ModTiers.AETHERIUM, 6.0f, -2.9f))));
    public static final RegistryObject<Item> AETHERIUM_SHOVEL = reg("aetherium_shovel",
            () -> new ShovelItem(ModTiers.AETHERIUM, new Item.Properties().rarity(Rarity.RARE).fireResistant().attributes(ShovelItem.createAttributes(ModTiers.AETHERIUM, 1.5f, -3.0f))));
    public static final RegistryObject<Item> AETHERIUM_HOE = reg("aetherium_hoe",
            () -> new HoeItem(ModTiers.AETHERIUM, new Item.Properties().rarity(Rarity.RARE).fireResistant().attributes(HoeItem.createAttributes(ModTiers.AETHERIUM, -3.0f, 0.0f))));

    // Staves & Relics
    public static final RegistryObject<Item> STAFF_OF_TEMPEST = reg("staff_of_tempest",
            () -> new ForgeCustomItems.TempestStaff(new Item.Properties().rarity(Rarity.RARE).durability(250).fireResistant()));
    public static final RegistryObject<Item> STAFF_OF_METEORS = reg("staff_of_meteors",
            () -> new ForgeCustomItems.MeteorStaff(new Item.Properties().rarity(Rarity.RARE).durability(250).fireResistant()));
    public static final RegistryObject<Item> STAFF_OF_HEALING = reg("staff_of_healing",
            () -> new ForgeCustomItems.HealingStaff(new Item.Properties().rarity(Rarity.EPIC).durability(200).fireResistant()));
    public static final RegistryObject<Item> WIND_TALISMAN = reg("wind_talisman",
            () -> new ForgeCustomItems.WindTalisman(new Item.Properties().rarity(Rarity.RARE).durability(300).fireResistant()));
    public static final RegistryObject<Item> RING_OF_HASTE = reg("ring_of_haste",
            () -> new ForgeCustomItems.RelicItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1).fireResistant(), "tooltip.aetherium.ring_of_haste", ChatFormatting.YELLOW, true, 1));
    public static final RegistryObject<Item> RING_OF_IMMORTALITY = reg("ring_of_immortality",
            () -> new ForgeCustomItems.RelicItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).fireResistant(), "tooltip.aetherium.ring_of_immortality", ChatFormatting.GOLD, true, 2));
    public static final RegistryObject<Item> MAGNET_CHARM = reg("magnet_charm",
            () -> new ForgeCustomItems.MagnetItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1).fireResistant()));

    // 3 Armor Sets: Aetherium, Pyrosite, Voidite
    public static final RegistryObject<Item> AETHERIUM_HELMET = reg("aetherium_helmet",
            () -> new ForgeCustomItems.CustomArmor(AetheriumForgeMod.AETHERIUM_ARMOR_MAT, ArmorItem.Type.HELMET, new Item.Properties().rarity(Rarity.RARE).fireResistant().durability(ArmorItem.Type.HELMET.getDurability(45)), 1));
    public static final RegistryObject<Item> AETHERIUM_CHESTPLATE = reg("aetherium_chestplate",
            () -> new ForgeCustomItems.CustomArmor(AetheriumForgeMod.AETHERIUM_ARMOR_MAT, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.RARE).fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(45)), 1));
    public static final RegistryObject<Item> AETHERIUM_LEGGINGS = reg("aetherium_leggings",
            () -> new ForgeCustomItems.CustomArmor(AetheriumForgeMod.AETHERIUM_ARMOR_MAT, ArmorItem.Type.LEGGINGS, new Item.Properties().rarity(Rarity.RARE).fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(45)), 1));
    public static final RegistryObject<Item> AETHERIUM_BOOTS = reg("aetherium_boots",
            () -> new ForgeCustomItems.CustomArmor(AetheriumForgeMod.AETHERIUM_ARMOR_MAT, ArmorItem.Type.BOOTS, new Item.Properties().rarity(Rarity.RARE).fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(45)), 1));

    public static final RegistryObject<Item> PYROSITE_HELMET = reg("pyrosite_helmet",
            () -> new ForgeCustomItems.CustomArmor(AetheriumForgeMod.PYROSITE_ARMOR_MAT, ArmorItem.Type.HELMET, new Item.Properties().rarity(Rarity.RARE).fireResistant().durability(ArmorItem.Type.HELMET.getDurability(48)), 2));
    public static final RegistryObject<Item> PYROSITE_CHESTPLATE = reg("pyrosite_chestplate",
            () -> new ForgeCustomItems.CustomArmor(AetheriumForgeMod.PYROSITE_ARMOR_MAT, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.RARE).fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(48)), 2));
    public static final RegistryObject<Item> PYROSITE_LEGGINGS = reg("pyrosite_leggings",
            () -> new ForgeCustomItems.CustomArmor(AetheriumForgeMod.PYROSITE_ARMOR_MAT, ArmorItem.Type.LEGGINGS, new Item.Properties().rarity(Rarity.RARE).fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(48)), 2));
    public static final RegistryObject<Item> PYROSITE_BOOTS = reg("pyrosite_boots",
            () -> new ForgeCustomItems.CustomArmor(AetheriumForgeMod.PYROSITE_ARMOR_MAT, ArmorItem.Type.BOOTS, new Item.Properties().rarity(Rarity.RARE).fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(48)), 2));

    public static final RegistryObject<Item> VOIDITE_HELMET = reg("voidite_helmet",
            () -> new ForgeCustomItems.CustomArmor(AetheriumForgeMod.VOIDITE_ARMOR_MAT, ArmorItem.Type.HELMET, new Item.Properties().rarity(Rarity.EPIC).fireResistant().durability(ArmorItem.Type.HELMET.getDurability(55)), 3));
    public static final RegistryObject<Item> VOIDITE_CHESTPLATE = reg("voidite_chestplate",
            () -> new ForgeCustomItems.CustomArmor(AetheriumForgeMod.VOIDITE_ARMOR_MAT, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.EPIC).fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(55)), 3));
    public static final RegistryObject<Item> VOIDITE_LEGGINGS = reg("voidite_leggings",
            () -> new ForgeCustomItems.CustomArmor(AetheriumForgeMod.VOIDITE_ARMOR_MAT, ArmorItem.Type.LEGGINGS, new Item.Properties().rarity(Rarity.EPIC).fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(55)), 3));
    public static final RegistryObject<Item> VOIDITE_BOOTS = reg("voidite_boots",
            () -> new ForgeCustomItems.CustomArmor(AetheriumForgeMod.VOIDITE_ARMOR_MAT, ArmorItem.Type.BOOTS, new Item.Properties().rarity(Rarity.EPIC).fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(55)), 3));

    // Foods & Elixirs
    public static final RegistryObject<Item> AETHER_APPLE = reg("aether_apple",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE).food(new FoodProperties.Builder()
                    .nutrition(6).saturationModifier(1.2f).alwaysEdible()
                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 300, 1), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 2), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 1), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 1.0f)
                    .build())));

    public static final RegistryObject<Item> CELESTIAL_AMBROSIA = reg("celestial_ambrosia",
            () -> new ForgeCustomItems.RelicItem(new Item.Properties().rarity(Rarity.EPIC).fireResistant().food(new FoodProperties.Builder()
                    .nutrition(12).saturationModifier(1.5f).alwaysEdible()
                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 2), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2400, 1), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2400, 1), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3600, 0), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.DIG_SPEED, 2400, 1), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3), 1.0f)
                    .build()), "tooltip.aetherium.celestial_ambrosia", ChatFormatting.LIGHT_PURPLE, true, 0));

    public static final RegistryObject<Item> ELIXIR_OF_FLIGHT = reg("elixir_of_flight",
            () -> new ForgeCustomItems.RelicItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(16).food(new FoodProperties.Builder()
                    .nutrition(2).saturationModifier(0.8f).alwaysEdible()
                    .effect(new MobEffectInstance(MobEffects.JUMP, 1200, 3), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.SLOW_FALLING, 1200, 0), 1.0f)
                    .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 2), 1.0f)
                    .build()), "tooltip.aetherium.elixir_of_flight", ChatFormatting.AQUA, true, 0));

    private static RegistryObject<Item> reg(String name, Supplier<Item> sup) {
        return AetheriumForgeMod.ITEMS.register(name, sup);
    }

    public static void init() {}
}
