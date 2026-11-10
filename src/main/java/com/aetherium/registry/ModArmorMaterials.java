package com.aetherium.registry;

import com.aetherium.AetheriumOdyssey;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModArmorMaterials {
    public static final RegistryEntry<ArmorMaterial> AETHERIUM = register("aetherium",
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 4);
                map.put(ArmorItem.Type.LEGGINGS, 7);
                map.put(ArmorItem.Type.CHESTPLATE, 9);
                map.put(ArmorItem.Type.HELMET, 4);
                map.put(ArmorItem.Type.BODY, 12);
            }),
            25,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.ofItems(ModItems.AETHERIUM_INGOT),
            4.0f,
            0.15f
    );

    public static final RegistryEntry<ArmorMaterial> PYROSITE = register("pyrosite",
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 4);
                map.put(ArmorItem.Type.LEGGINGS, 8);
                map.put(ArmorItem.Type.CHESTPLATE, 10);
                map.put(ArmorItem.Type.HELMET, 5);
                map.put(ArmorItem.Type.BODY, 14);
            }),
            22,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.ofItems(ModItems.PYROSITE_INGOT),
            4.5f,
            0.20f
    );

    public static final RegistryEntry<ArmorMaterial> VOIDITE = register("voidite",
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 5);
                map.put(ArmorItem.Type.LEGGINGS, 9);
                map.put(ArmorItem.Type.CHESTPLATE, 11);
                map.put(ArmorItem.Type.HELMET, 6);
                map.put(ArmorItem.Type.BODY, 16);
            }),
            30,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.ofItems(ModItems.VOIDITE_INGOT),
            5.0f,
            0.25f
    );

    private static RegistryEntry<ArmorMaterial> register(
            String id,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantability,
            RegistryEntry<net.minecraft.sound.SoundEvent> equipSound,
            Supplier<Ingredient> repairIngredient,
            float toughness,
            float knockbackResistance
    ) {
        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(Identifier.of(AetheriumOdyssey.MOD_ID, id)));
        ArmorMaterial material = new ArmorMaterial(defense, enchantability, equipSound, repairIngredient, layers, toughness, knockbackResistance);
        return Registry.registerReference(Registries.ARMOR_MATERIAL, Identifier.of(AetheriumOdyssey.MOD_ID, id), material);
    }

    public static void init() {
        // Trigger static initialization
    }
}
