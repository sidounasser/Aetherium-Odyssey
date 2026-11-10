package com.aetherium.forge;

import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.Util;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.List;

@Mod(AetheriumForgeMod.MOD_ID)
public class AetheriumForgeMod {
    public static final String MOD_ID = "aetherium";

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final Holder<ArmorMaterial> AETHERIUM_ARMOR_MAT = Holder.direct(new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 4);
                        map.put(ArmorItem.Type.LEGGINGS, 7);
                        map.put(ArmorItem.Type.CHESTPLATE, 9);
                        map.put(ArmorItem.Type.HELMET, 4);
                        map.put(ArmorItem.Type.BODY, 12);
                    }),
                    25,
                    SoundEvents.ARMOR_EQUIP_NETHERITE,
                    () -> Ingredient.of(ForgeItems.AETHERIUM_INGOT.get()),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MOD_ID, "aetherium"))),
                    4.0f,
                    0.15f
            ));

    public static final Holder<ArmorMaterial> PYROSITE_ARMOR_MAT = Holder.direct(new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 4);
                        map.put(ArmorItem.Type.LEGGINGS, 8);
                        map.put(ArmorItem.Type.CHESTPLATE, 10);
                        map.put(ArmorItem.Type.HELMET, 5);
                        map.put(ArmorItem.Type.BODY, 14);
                    }),
                    22,
                    SoundEvents.ARMOR_EQUIP_NETHERITE,
                    () -> Ingredient.of(ForgeItems.PYROSITE_INGOT.get()),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MOD_ID, "pyrosite"))),
                    4.5f,
                    0.20f
            ));

    public static final Holder<ArmorMaterial> VOIDITE_ARMOR_MAT = Holder.direct(new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 5);
                        map.put(ArmorItem.Type.LEGGINGS, 9);
                        map.put(ArmorItem.Type.CHESTPLATE, 11);
                        map.put(ArmorItem.Type.HELMET, 6);
                        map.put(ArmorItem.Type.BODY, 16);
                    }),
                    30,
                    SoundEvents.ARMOR_EQUIP_NETHERITE,
                    () -> Ingredient.of(ForgeItems.VOIDITE_INGOT.get()),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(MOD_ID, "voidite"))),
                    5.0f,
                    0.25f
            ));

    public static final RegistryObject<EntityType<ForgeArchonBoss>> AETHERIUM_ARCHON = ENTITY_TYPES.register("aetherium_archon",
            () -> EntityType.Builder.of(ForgeArchonBoss::new, MobCategory.MONSTER)
                    .sized(0.85f, 2.65f)
                    .fireImmune()
                    .clientTrackingRange(10)
                    .build("aetherium_archon"));

    public static final RegistryObject<CreativeModeTab> AETHERIUM_TAB = CREATIVE_TABS.register("aetherium_group",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ForgeItems.INFINITY_BLADE.get()))
                    .title(Component.translatable("itemGroup.aetherium.aetherium_group"))
                    .displayItems((params, output) -> {
                        for (RegistryObject<Item> item : ITEMS.getEntries()) {
                            output.accept(item.get());
                        }
                    })
                    .build());

    public AetheriumForgeMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ForgeBlocks.init();
        ForgeItems.init();

        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        ENTITY_TYPES.register(modBus);
        CREATIVE_TABS.register(modBus);

        modBus.addListener(this::registerAttributes);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modBus.addListener(this::registerRenderers);
        }

        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(AETHERIUM_ARCHON.get(), ForgeArchonBoss.createAttributes().build());
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AETHERIUM_ARCHON.get(), ArchonRenderer::new);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("aetherium")
                .then(Commands.literal("guide")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            ForgeCustomItems.sendCodexGuide(player);
                            return 1;
                        }))
                .then(Commands.literal("codex")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            player.getInventory().add(new ItemStack(ForgeItems.AETHER_CODEX.get()));
                            player.sendSystemMessage(Component.translatable("message.aetherium.received_codex").withStyle(ChatFormatting.AQUA));
                            return 1;
                        }))
                .then(Commands.literal("kit")
                        .requires(s -> s.hasPermission(2))
                        .executes(ctx -> {
                            ServerPlayer p = ctx.getSource().getPlayerOrException();
                            p.getInventory().add(new ItemStack(ForgeItems.AETHER_CODEX.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.INFINITY_BLADE.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.CELESTIAL_HALBERD.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.AETHERIUM_SWORD.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.PYROSITE_GREATSWORD.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.VOID_SCYTHE.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.AETHERIUM_PICKAXE.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.STARLIGHT_BOW.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.AETHER_GRAPPLE.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.STAFF_OF_TEMPEST.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.STAFF_OF_METEORS.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.STAFF_OF_HEALING.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.WIND_TALISMAN.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.RING_OF_HASTE.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.RING_OF_IMMORTALITY.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.MAGNET_CHARM.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.AETHER_PORTAL_KEY.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.AETHER_STEED_WHISTLE.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.CHRONOS_HOURGLASS.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.VOID_POUCH.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.SHRINE_SCROLL.get(), 4));
                            p.getInventory().add(new ItemStack(ForgeItems.VOIDITE_HELMET.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.VOIDITE_CHESTPLATE.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.VOIDITE_LEGGINGS.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.VOIDITE_BOOTS.get()));
                            p.getInventory().add(new ItemStack(ForgeItems.CELESTIAL_AMBROSIA.get(), 8));
                            p.getInventory().add(new ItemStack(ForgeBlocks.AETHER_ALTAR.get().asItem(), 1));
                            p.getInventory().add(new ItemStack(ForgeItems.ARCHON_SUMMONER.get(), 2));
                            p.sendSystemMessage(Component.translatable("message.aetherium.kit_granted").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
                            return 1;
                        }))
                .then(Commands.literal("summon_boss")
                        .requires(s -> s.hasPermission(2))
                        .executes(ctx -> {
                            ServerPlayer p = ctx.getSource().getPlayerOrException();
                            ServerLevel level = ctx.getSource().getLevel();
                            ForgeArchonBoss boss = AETHERIUM_ARCHON.get().create(level);
                            if (boss != null) {
                                boss.moveTo(p.getX() + 3.0, p.getY(), p.getZ() + 3.0, 0.0f, 0.0f);
                                boss.finalizeSpawn(level, level.getCurrentDifficultyAt(p.blockPosition()), MobSpawnType.COMMAND, null);
                                level.addFreshEntity(boss);
                                p.sendSystemMessage(Component.translatable("message.aetherium.archon_awakened").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));
                            }
                            return 1;
                        }))
        );
    }
}
