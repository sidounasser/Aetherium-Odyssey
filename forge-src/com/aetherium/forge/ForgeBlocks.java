package com.aetherium.forge;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public class ForgeBlocks {
    public static final RegistryObject<Block> AETHERIUM_ORE = register("aetherium_ore",
            () -> new DropExperienceBlock(UniformInt.of(3, 7), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(3.5f, 4.0f).lightLevel(s -> 5).sound(SoundType.STONE)), Rarity.COMMON);

    public static final RegistryObject<Block> DEEPSLATE_AETHERIUM_ORE = register("deepslate_aetherium_ore",
            () -> new DropExperienceBlock(UniformInt.of(4, 8), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE).requiresCorrectToolForDrops().strength(4.8f, 5.0f).lightLevel(s -> 6).sound(SoundType.DEEPSLATE)), Rarity.COMMON);

    public static final RegistryObject<Block> PYROSITE_ORE = register("pyrosite_ore",
            () -> new DropExperienceBlock(UniformInt.of(4, 8), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NETHER).requiresCorrectToolForDrops().strength(3.5f, 4.5f).lightLevel(s -> 7).sound(SoundType.NETHER_ORE)), Rarity.UNCOMMON);

    public static final RegistryObject<Block> VOIDITE_ORE = register("voidite_ore",
            () -> new DropExperienceBlock(UniformInt.of(6, 11), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(5.0f, 9.0f).lightLevel(s -> 8).sound(SoundType.AMETHYST)), Rarity.RARE);

    public static final RegistryObject<Block> RAW_AETHERIUM_BLOCK = register("raw_aetherium_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(5.0f, 6.0f).lightLevel(s -> 6).sound(SoundType.STONE)), Rarity.COMMON);

    public static final RegistryObject<Block> AETHERIUM_BLOCK = register("aetherium_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).requiresCorrectToolForDrops().strength(6.0f, 12.0f).lightLevel(s -> 12).sound(SoundType.NETHERITE_BLOCK)), Rarity.RARE);

    public static final RegistryObject<Block> PYROSITE_BLOCK = register("pyrosite_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).requiresCorrectToolForDrops().strength(6.0f, 12.0f).lightLevel(s -> 14).sound(SoundType.NETHERITE_BLOCK)), Rarity.RARE);

    public static final RegistryObject<Block> VOIDITE_BLOCK = register("voidite_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(7.0f, 20.0f).lightLevel(s -> 10).sound(SoundType.AMETHYST)), Rarity.EPIC);

    public static final RegistryObject<Block> AETHER_LAMP = register("aether_lamp",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).strength(1.5f, 3.0f).lightLevel(s -> 15).sound(SoundType.AMETHYST_CLUSTER)), Rarity.UNCOMMON);

    public static final RegistryObject<Block> AETHER_ALTAR = register("aether_altar",
            () -> new AltarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).requiresCorrectToolForDrops().strength(6.0f, 1200.0f).lightLevel(s -> 14).sound(SoundType.LODESTONE)), Rarity.EPIC);

    public static final RegistryObject<Block> HEALING_PYLON = register("healing_pylon",
            () -> new PylonBlock(BlockBehaviour.Properties.of().mapColor(MapColor.EMERALD).requiresCorrectToolForDrops().strength(4.0f, 15.0f).lightLevel(s -> 13).sound(SoundType.AMETHYST)), Rarity.RARE);

    public static final RegistryObject<Block> ELEVATOR_PAD = register("elevator_pad",
            () -> new ElevatorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).requiresCorrectToolForDrops().strength(3.5f, 10.0f).lightLevel(s -> 9).sound(SoundType.METAL)), Rarity.RARE);

    private static RegistryObject<Block> register(String name, Supplier<Block> supplier, Rarity rarity) {
        RegistryObject<Block> blockObj = AetheriumForgeMod.BLOCKS.register(name, supplier);
        AetheriumForgeMod.ITEMS.register(name, () -> new BlockItem(blockObj.get(), new Item.Properties().rarity(rarity)));
        return blockObj;
    }

    public static void init() {}

    public static class AltarBlock extends Block {
        public AltarBlock(Properties props) { super(props); }

        @Override
        protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            if (stack.is(ForgeItems.ARCHON_SUMMONER.get())) {
                if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
                    if (!player.isCreative()) stack.shrink(1);

                    LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
                    if (bolt != null) {
                        bolt.moveTo(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
                        bolt.setVisualOnly(true);
                        serverLevel.addFreshEntity(bolt);
                    }

                    ForgeArchonBoss boss = AetheriumForgeMod.AETHERIUM_ARCHON.get().create(serverLevel);
                    if (boss != null) {
                        boss.moveTo(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, level.random.nextFloat() * 360.0f, 0.0f);
                        boss.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null);
                        serverLevel.addFreshEntity(boss);
                    }

                    serverLevel.sendParticles(ParticleTypes.END_ROD, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 80, 1.2, 1.5, 1.2, 0.15);
                    level.playSound(null, pos, SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 1.5f, 0.85f);
                    for (ServerPlayer sp : serverLevel.players()) {
                        sp.sendSystemMessage(Component.translatable("message.aetherium.archon_awakened").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            if (stack.is(Items.DIAMOND)) {
                if (player.experienceLevel >= 3 || player.isCreative()) {
                    if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
                        if (!player.isCreative()) {
                            player.giveExperienceLevels(-3);
                            stack.shrink(1);
                        }
                        player.getInventory().placeItemBackInInventory(new ItemStack(ForgeItems.AETHERIUM_SHARD.get(), 2));
                        serverLevel.sendParticles(ParticleTypes.ENCHANT, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 45, 0.5, 0.5, 0.5, 0.2);
                        level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.5f, 1.2f);
                        player.displayClientMessage(Component.translatable("message.aetherium.altar_infused_diamond").withStyle(ChatFormatting.AQUA), true);
                    }
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                } else {
                    if (!level.isClientSide) {
                        player.displayClientMessage(Component.translatable("message.aetherium.altar_need_xp").withStyle(ChatFormatting.RED), true);
                    }
                    return ItemInteractionResult.FAIL;
                }
            }

            if (stack.is(Items.GOLDEN_APPLE)) {
                if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
                    if (!player.isCreative()) stack.shrink(1);
                    player.getInventory().placeItemBackInInventory(new ItemStack(ForgeItems.AETHER_APPLE.get(), 1));
                    serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 35, 0.4, 0.6, 0.4, 0.15);
                    level.playSound(null, pos, SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 1.2f, 1.4f);
                    player.displayClientMessage(Component.translatable("message.aetherium.altar_infused_apple").withStyle(ChatFormatting.GOLD), true);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            return super.useItemOn(stack, state, level, pos, player, hand, hit);
        }

        @Override
        protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
            if (!level.isClientSide) {
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 45, 0, true, false, true));
                player.displayClientMessage(Component.translatable("message.aetherium.altar_info").withStyle(ChatFormatting.AQUA), true);
                level.playSound(null, pos, SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 1.0f, 1.2f);
            }
            return InteractionResult.SUCCESS;
        }

        @Override
        public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
            level.addParticle(ParticleTypes.END_ROD, pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.8, pos.getY() + 1.0 + random.nextDouble() * 0.6, pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.8, 0.0, 0.03, 0.0);
        }
    }

    public static class PylonBlock extends Block {
        public PylonBlock(Properties props) { super(props); }

        @Override
        protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
            super.onPlace(state, level, pos, oldState, isMoving);
            if (!level.isClientSide) level.scheduleTick(pos, this, 40);
        }

        @Override
        protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
            AABB area = new AABB(pos).inflate(10.0);
            for (Player p : level.getEntitiesOfClass(Player.class, area)) {
                p.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1, true, false, true));
                p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0, true, false, true));
                if (p.getFoodData().needsFood()) p.getFoodData().eat(1, 0.5f);
            }
            for (Monster m : level.getEntitiesOfClass(Monster.class, new AABB(pos).inflate(7.0))) {
                m.hurt(level.damageSources().magic(), 2.5f);
                m.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60, 0));
            }
            level.sendParticles(ParticleTypes.HEART, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 4, 0.4, 0.3, 0.4, 0.02);
            level.scheduleTick(pos, this, 60);
        }

        @Override
        protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
            if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
                player.heal(8.0f);
                player.removeEffect(MobEffects.POISON);
                player.removeEffect(MobEffects.WITHER);
                player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                player.removeEffect(MobEffects.WEAKNESS);
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 400, 1));
                serverLevel.sendParticles(ParticleTypes.HEART, player.getX(), player.getY(0.5), player.getZ(), 12, 0.5, 0.5, 0.5, 0.1);
                level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0f, 1.4f);
                player.displayClientMessage(Component.translatable("message.aetherium.pylon_healed").withStyle(ChatFormatting.GREEN), true);
            }
            return InteractionResult.SUCCESS;
        }
    }

    public static class ElevatorBlock extends Block {
        public ElevatorBlock(Properties props) { super(props); }

        @Override
        protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
            if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
                boolean down = player.isShiftKeyDown();
                BlockPos target = findTarget(level, pos, down ? -1 : 1);
                if (target != null) {
                    teleport(serverLevel, player, target, down);
                } else {
                    player.displayClientMessage(Component.translatable(down ? "message.aetherium.elevator_none_below" : "message.aetherium.elevator_none_above").withStyle(ChatFormatting.YELLOW), true);
                }
            }
            return InteractionResult.SUCCESS;
        }

        @Override
        public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
            if (!level.isClientSide && level instanceof ServerLevel serverLevel && entity instanceof Player player) {
                if (player.isShiftKeyDown() && !player.getCooldowns().isOnCooldown(this.asItem())) {
                    BlockPos target = findTarget(level, pos, -1);
                    if (target != null) {
                        player.getCooldowns().addCooldown(this.asItem(), 20);
                        teleport(serverLevel, player, target, true);
                    }
                }
            }
            super.stepOn(level, pos, state, entity);
        }

        private BlockPos findTarget(Level level, BlockPos start, int dir) {
            for (int i = 2; i <= 64; i++) {
                BlockPos check = start.offset(0, i * dir, 0);
                if (level.getBlockState(check).is(this)) {
                    if (level.getBlockState(check.above()).getCollisionShape(level, check.above(), CollisionContext.empty()).isEmpty()
                            && level.getBlockState(check.above(2)).getCollisionShape(level, check.above(2), CollisionContext.empty()).isEmpty()) {
                        return check;
                    }
                }
            }
            return null;
        }

        private void teleport(ServerLevel level, Player player, BlockPos target, boolean down) {
            player.teleportTo(target.getX() + 0.5, target.getY() + 1.05, target.getZ() + 0.5);
            player.fallDistance = 0.0f;
            level.sendParticles(ParticleTypes.REVERSE_PORTAL, target.getX() + 0.5, target.getY() + 1.5, target.getZ() + 0.5, 35, 0.3, 0.5, 0.3, 0.08);
            level.playSound(null, target, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, down ? 0.85f : 1.25f);
            player.displayClientMessage(Component.translatable(down ? "message.aetherium.elevator_down" : "message.aetherium.elevator_up").withStyle(ChatFormatting.AQUA), true);
        }
    }
}
