package com.aetherium.registry;

import com.aetherium.AetheriumOdyssey;
import com.aetherium.block.AetherAltarBlock;
import com.aetherium.block.ElevatorPadBlock;
import com.aetherium.block.HealingPylonBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ModBlocks {
    public static final Block AETHERIUM_ORE = registerBlock("aetherium_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.CYAN)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresTool()
                            .strength(3.5f, 4.0f)
                            .luminance(state -> 5)
                            .sounds(BlockSoundGroup.STONE)));

    public static final Block DEEPSLATE_AETHERIUM_ORE = registerBlock("deepslate_aetherium_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(4, 8),
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.DEEPSLATE_GRAY)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresTool()
                            .strength(4.8f, 5.0f)
                            .luminance(state -> 6)
                            .sounds(BlockSoundGroup.DEEPSLATE)));

    public static final Block PYROSITE_ORE = registerBlock("pyrosite_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(4, 8),
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.DARK_RED)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresTool()
                            .strength(3.5f, 4.5f)
                            .luminance(state -> 7)
                            .sounds(BlockSoundGroup.NETHER_ORE)));

    public static final Block VOIDITE_ORE = registerBlock("voidite_ore",
            new ExperienceDroppingBlock(UniformIntProvider.create(6, 11),
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.PURPLE)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresTool()
                            .strength(5.0f, 9.0f)
                            .luminance(state -> 8)
                            .sounds(BlockSoundGroup.AMETHYST_BLOCK)));

    public static final Block RAW_AETHERIUM_BLOCK = registerBlock("raw_aetherium_block",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(5.0f, 6.0f)
                    .luminance(state -> 6)
                    .sounds(BlockSoundGroup.STONE)));

    public static final Block AETHERIUM_BLOCK = registerBlock("aetherium_block",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIAMOND_BLUE)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresTool()
                    .strength(6.0f, 12.0f)
                    .luminance(state -> 12)
                    .sounds(BlockSoundGroup.NETHERITE)));

    public static final Block PYROSITE_BLOCK = registerBlock("pyrosite_block",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.BRIGHT_RED)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresTool()
                    .strength(6.0f, 12.0f)
                    .luminance(state -> 14)
                    .sounds(BlockSoundGroup.NETHERITE)));

    public static final Block VOIDITE_BLOCK = registerBlock("voidite_block",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .instrument(NoteBlockInstrument.CHIME)
                    .requiresTool()
                    .strength(7.0f, 20.0f)
                    .luminance(state -> 10)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)));

    public static final Block AETHER_LAMP = registerBlock("aether_lamp",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_BLUE)
                    .instrument(NoteBlockInstrument.CHIME)
                    .strength(1.5f, 3.0f)
                    .luminance(state -> 15)
                    .sounds(BlockSoundGroup.AMETHYST_CLUSTER)));

    public static final Block AETHER_ALTAR = registerRareBlock("aether_altar",
            new AetherAltarBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIAMOND_BLUE)
                    .requiresTool()
                    .strength(6.0f, 1200.0f)
                    .luminance(state -> 14)
                    .sounds(BlockSoundGroup.LODESTONE)),
            Rarity.EPIC);

    public static final Block HEALING_PYLON = registerRareBlock("healing_pylon",
            new HealingPylonBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.EMERALD_GREEN)
                    .requiresTool()
                    .strength(4.0f, 15.0f)
                    .luminance(state -> 13)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)),
            Rarity.RARE);

    public static final Block ELEVATOR_PAD = registerRareBlock("elevator_pad",
            new ElevatorPadBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .requiresTool()
                    .strength(3.5f, 10.0f)
                    .luminance(state -> 9)
                    .sounds(BlockSoundGroup.METAL)),
            Rarity.RARE);

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block, new Item.Settings());
        return Registry.register(Registries.BLOCK, Identifier.of(AetheriumOdyssey.MOD_ID, name), block);
    }

    private static Block registerRareBlock(String name, Block block, Rarity rarity) {
        registerBlockItem(name, block, new Item.Settings().rarity(rarity));
        return Registry.register(Registries.BLOCK, Identifier.of(AetheriumOdyssey.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, Item.Settings settings) {
        return Registry.register(Registries.ITEM, Identifier.of(AetheriumOdyssey.MOD_ID, name),
                new BlockItem(block, settings));
    }

    public static void init() {
        // Trigger static initialization
    }
}
