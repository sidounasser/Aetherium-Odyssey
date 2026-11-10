package com.aetherium.registry;

import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Supplier;

public enum ModToolMaterials implements ToolMaterial {
    AETHERIUM(2650, 11.5f, 5.0f, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 22, () -> Ingredient.ofItems(ModItems.AETHERIUM_INGOT)),
    PYROSITE(2900, 12.0f, 6.0f, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 20, () -> Ingredient.ofItems(ModItems.PYROSITE_INGOT)),
    VOIDITE(3300, 13.5f, 7.0f, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 25, () -> Ingredient.ofItems(ModItems.VOIDITE_INGOT)),
    CELESTIAL(5000, 16.0f, 9.5f, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 30, () -> Ingredient.ofItems(ModItems.CELESTIAL_CORE)),
    INFINITY(9999, 22.0f, 14.0f, BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 35, () -> Ingredient.ofItems(ModItems.PRIMORDIAL_VOID_ESSENCE));

    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final TagKey<Block> inverseTag;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    ModToolMaterials(int itemDurability, float miningSpeed, float attackDamage, TagKey<Block> inverseTag, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.inverseTag = inverseTag;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public TagKey<Block> getInverseTag() {
        return this.inverseTag;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
