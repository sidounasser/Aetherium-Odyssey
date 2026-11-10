package com.aetherium.forge;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ForgeCustomItems {

    // 1. Aetherium Sword
    public static class AetherSword extends SwordItem {
        public AetherSword(Tier tier, Properties props) { super(tier, props); }

        @Override
        public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
            target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 30, 1), attacker);
            target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0), attacker);
            if (attacker.level() instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.END_ROD, target.getX(), target.getY(0.5), target.getZ(), 12, 0.3, 0.4, 0.3, 0.08);
            }
            return super.hurtEnemy(stack, target, attacker);
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            if (!level.isClientSide && level instanceof ServerLevel sl) {
                user.getCooldowns().addCooldown(this, 80);
                user.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1));
                for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(6.0, 3.0, 6.0), e -> e != user && !e.isAlliedTo(user))) {
                    e.hurt(level.damageSources().playerAttack(user), 7.5f);
                    e.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 35, 1));
                    Vec3 kb = e.position().subtract(user.position()).normalize().scale(1.3);
                    e.push(kb.x, 0.45, kb.z);
                    e.hurtMarked = true;
                }
                sl.sendParticles(ParticleTypes.END_ROD, user.getX(), user.getY() + 0.8, user.getZ(), 45, 2.0, 0.5, 2.0, 0.1);
                level.playSound(null, user.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.2f, 1.2f);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tip, TooltipFlag flag) {
            tip.add(Component.translatable("tooltip.aetherium.aetherium_sword.passive").withStyle(ChatFormatting.AQUA));
            tip.add(Component.translatable("tooltip.aetherium.aetherium_sword.active").withStyle(ChatFormatting.GOLD));
        }
    }

    // 2. Pyrosite Greatsword
    public static class PyrositeSword extends SwordItem {
        public PyrositeSword(Tier tier, Properties props) { super(tier, props); }

        @Override
        public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
            target.igniteForSeconds(8.0f);
            attacker.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 140, 0, true, false, true));
            if (attacker.level() instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.FLAME, target.getX(), target.getY(0.5), target.getZ(), 18, 0.35, 0.4, 0.35, 0.06);
            }
            return super.hurtEnemy(stack, target, attacker);
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            if (!level.isClientSide && level instanceof ServerLevel sl) {
                user.getCooldowns().addCooldown(this, 120);
                user.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0));
                user.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 160, 0));
                for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(6.5, 3.0, 6.5), e -> e != user && !e.isAlliedTo(user))) {
                    e.igniteForSeconds(10.0f);
                    e.hurt(level.damageSources().playerAttack(user), 9.0f);
                }
                sl.sendParticles(ParticleTypes.FLAME, user.getX(), user.getY() + 0.8, user.getZ(), 75, 2.5, 0.5, 2.5, 0.08);
                level.playSound(null, user.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.0f, 1.15f);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tip, TooltipFlag flag) {
            tip.add(Component.translatable("tooltip.aetherium.pyrosite_greatsword.passive").withStyle(ChatFormatting.RED));
            tip.add(Component.translatable("tooltip.aetherium.pyrosite_greatsword.active").withStyle(ChatFormatting.GOLD));
        }
    }

    // 3. Void Scythe
    public static class VoidScythe extends SwordItem {
        public VoidScythe(Tier tier, Properties props) { super(tier, props); }

        @Override
        public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 1), attacker);
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1), attacker);
            attacker.heal(2.5f);
            return super.hurtEnemy(stack, target, attacker);
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            if (!level.isClientSide && level instanceof ServerLevel sl) {
                user.getCooldowns().addCooldown(this, 70);
                Vec3 start = user.getEyePosition();
                Vec3 look = user.getLookAngle();
                Vec3 end = start.add(look.scale(14.0));
                BlockHitResult hit = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, user));
                Vec3 dest = hit.getType() == HitResult.Type.MISS ? end : hit.getLocation().subtract(look);
                user.teleportTo(dest.x, dest.y + 0.2, dest.z);
                user.fallDistance = 0.0f;
                user.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 1));
                for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(3.5), e -> e != user && !e.isAlliedTo(user))) {
                    e.hurt(level.damageSources().magic(), 6.0f);
                    e.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 1));
                    user.heal(1.5f);
                }
                sl.sendParticles(ParticleTypes.REVERSE_PORTAL, user.getX(), user.getY(0.5), user.getZ(), 40, 0.4, 0.6, 0.4, 0.1);
                level.playSound(null, user.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.2f, 0.9f);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tip, TooltipFlag flag) {
            tip.add(Component.translatable("tooltip.aetherium.void_scythe.passive").withStyle(ChatFormatting.DARK_PURPLE));
            tip.add(Component.translatable("tooltip.aetherium.void_scythe.active").withStyle(ChatFormatting.GOLD));
        }
    }

    // 4. Celestial Halberd
    public static class CelestialHalberd extends SwordItem {
        public CelestialHalberd(Tier tier, Properties props) { super(tier, props); }

        @Override
        public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
            attacker.heal(2.0f);
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1), attacker);
            if (attacker.level() instanceof ServerLevel sl) {
                for (LivingEntity e : sl.getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(3.0), e -> e != attacker && e != target && !e.isAlliedTo(attacker))) {
                    e.hurt(sl.damageSources().magic(), 5.0f);
                }
                sl.sendParticles(ParticleTypes.SONIC_BOOM, target.getX(), target.getY(0.5), target.getZ(), 1, 0, 0, 0, 0);
            }
            return super.hurtEnemy(stack, target, attacker);
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            if (!level.isClientSide && level instanceof ServerLevel sl) {
                user.getCooldowns().addCooldown(this, 140);
                user.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 240, 2));
                user.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1));
                user.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0));
                int bolts = 0;
                for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(8.5, 4.0, 8.5), e -> e != user && !e.isAlliedTo(user))) {
                    e.hurt(level.damageSources().magic(), 14.0f);
                    e.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 25, 2));
                    if (bolts < 5) {
                        LightningBolt b = EntityType.LIGHTNING_BOLT.create(sl);
                        if (b != null) { b.moveTo(e.getX(), e.getY(), e.getZ()); b.setVisualOnly(true); sl.addFreshEntity(b); }
                        bolts++;
                    }
                }
                sl.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, user.getX(), user.getY() + 1.0, user.getZ(), 45, 2.0, 1.0, 2.0, 0.2);
                level.playSound(null, user.blockPosition(), SoundEvents.TRIDENT_THUNDER.value(), SoundSource.PLAYERS, 1.5f, 1.0f);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        @Override public boolean isFoil(ItemStack stack) { return true; }

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tip, TooltipFlag flag) {
            tip.add(Component.translatable("tooltip.aetherium.celestial_halberd.passive").withStyle(ChatFormatting.LIGHT_PURPLE));
            tip.add(Component.translatable("tooltip.aetherium.celestial_halberd.active").withStyle(ChatFormatting.GOLD));
        }
    }

    // 5. Infinity Blade
    public static class InfinityBlade extends SwordItem {
        public InfinityBlade(Tier tier, Properties props) { super(tier, props); }

        @Override
        public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
            attacker.heal(4.0f);
            target.igniteForSeconds(10.0f);
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 120, 2), attacker);
            if (attacker.level() instanceof ServerLevel sl) {
                for (LivingEntity e : sl.getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(4.0), e -> e != attacker && e != target && !e.isAlliedTo(attacker))) {
                    e.hurt(sl.damageSources().magic(), 9.0f);
                    e.igniteForSeconds(5.0f);
                }
                sl.sendParticles(ParticleTypes.SONIC_BOOM, target.getX(), target.getY(0.5), target.getZ(), 1, 0, 0, 0, 0);
            }
            return super.hurtEnemy(stack, target, attacker);
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            if (!level.isClientSide && level instanceof ServerLevel sl) {
                user.getCooldowns().addCooldown(this, 120);
                user.heal(user.getMaxHealth());
                user.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 2));
                user.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 1));
                user.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 300, 2));
                user.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 2));
                user.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 400, 3));
                int bolts = 0;
                for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(11.0, 5.0, 11.0), e -> e != user && !e.isAlliedTo(user))) {
                    e.hurt(level.damageSources().magic(), 22.0f);
                    e.igniteForSeconds(12.0f);
                    e.addEffect(new MobEffectInstance(MobEffects.WITHER, 140, 2));
                    if (bolts < 8) {
                        LightningBolt b = EntityType.LIGHTNING_BOLT.create(sl);
                        if (b != null) { b.moveTo(e.getX(), e.getY(), e.getZ()); b.setVisualOnly(true); sl.addFreshEntity(b); }
                        bolts++;
                    }
                }
                sl.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, user.getX(), user.getY() + 1.0, user.getZ(), 100, 3.0, 1.5, 3.0, 0.25);
                level.playSound(null, user.blockPosition(), SoundEvents.TRIDENT_THUNDER.value(), SoundSource.PLAYERS, 1.6f, 0.9f);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        @Override public boolean isFoil(ItemStack stack) { return true; }

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tip, TooltipFlag flag) {
            tip.add(Component.translatable("tooltip.aetherium.infinity_blade.passive").withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));
            tip.add(Component.translatable("tooltip.aetherium.infinity_blade.active").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        }
    }

    // 6. Seismic Pickaxe (3x3 + Radar)
    public static class SeismicPickaxe extends PickaxeItem {
        public SeismicPickaxe(Tier tier, Properties props) { super(tier, props); }

        @Override
        public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
            if (!level.isClientSide && miner instanceof Player player && !player.isShiftKeyDown() && state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
                BlockPos[] adj = new BlockPos[]{pos.above(), pos.below(), pos.north(), pos.south(), pos.east(), pos.west()};
                for (BlockPos p : adj) {
                    BlockState s = level.getBlockState(p);
                    if (s.is(BlockTags.MINEABLE_WITH_PICKAXE) && s.getDestroySpeed(level, p) > 0 && s.getDestroySpeed(level, p) <= 10.0f && level.getBlockEntity(p) == null) {
                        level.destroyBlock(p, true, player);
                        stack.hurtAndBreak(1, miner, EquipmentSlot.MAINHAND);
                    }
                }
            }
            return super.mineBlock(stack, level, state, pos, miner);
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            if (!level.isClientSide) {
                user.getCooldowns().addCooldown(this, 100);
                int custom = 0, rare = 0;
                double best = Double.MAX_VALUE;
                BlockPos center = user.blockPosition();
                for (int dx = -12; dx <= 12; dx++) {
                    for (int dy = -12; dy <= 12; dy++) {
                        for (int dz = -12; dz <= 12; dz++) {
                            BlockPos p = center.offset(dx, dy, dz);
                            Block b = level.getBlockState(p).getBlock();
                            boolean isCust = b == ForgeBlocks.AETHERIUM_ORE.get() || b == ForgeBlocks.DEEPSLATE_AETHERIUM_ORE.get() || b == ForgeBlocks.PYROSITE_ORE.get() || b == ForgeBlocks.VOIDITE_ORE.get();
                            boolean isVan = b == Blocks.DIAMOND_ORE || b == Blocks.DEEPSLATE_DIAMOND_ORE || b == Blocks.ANCIENT_DEBRIS || b == Blocks.EMERALD_ORE;
                            if (isCust || isVan) {
                                if (isCust) custom++; else rare++;
                                double d = center.distSqr(p);
                                if (d < best) best = d;
                            }
                        }
                    }
                }
                level.playSound(null, center, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 1.2f, 1.4f);
                if (best < Double.MAX_VALUE) {
                    user.displayClientMessage(Component.translatable("message.aetherium.radar_found", custom, rare, (int) Math.sqrt(best)).withStyle(ChatFormatting.AQUA), true);
                } else {
                    user.displayClientMessage(Component.translatable("message.aetherium.radar_empty").withStyle(ChatFormatting.GRAY), true);
                }
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tip, TooltipFlag flag) {
            tip.add(Component.translatable("tooltip.aetherium.aetherium_pickaxe.passive").withStyle(ChatFormatting.AQUA));
            tip.add(Component.translatable("tooltip.aetherium.aetherium_pickaxe.active").withStyle(ChatFormatting.GOLD));
        }
    }

    // 7. Staves, Bow, Grapple, Talisman, Magnet, Relics, Armors, Portal Key, Steed Whistle, Shrine Scroll, Hourglass, Void Pouch, Codex
    public static class TempestStaff extends Item {
        public TempestStaff(Properties props) { super(props); }
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            if (!level.isClientSide && level instanceof ServerLevel sl) {
                user.getCooldowns().addCooldown(this, 60);
                Vec3 start = user.getEyePosition();
                Vec3 end = start.add(user.getLookAngle().scale(32.0));
                BlockHitResult hit = level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, user));
                Vec3 p = hit.getLocation();
                LightningBolt b = EntityType.LIGHTNING_BOLT.create(sl);
                if (b != null) { b.moveTo(p.x, p.y, p.z); b.setVisualOnly(true); sl.addFreshEntity(b); }
                for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, new AABB(p.x - 4, p.y - 3, p.z - 4, p.x + 4, p.y + 4, p.z + 4), e -> e != user && !e.isAlliedTo(user))) {
                    e.hurt(level.damageSources().lightningBolt(), 11.0f);
                }
                level.playSound(null, user.blockPosition(), SoundEvents.TRIDENT_THUNDER.value(), SoundSource.PLAYERS, 1.3f, 1.2f);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        @Override public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {
            t.add(Component.translatable("tooltip.aetherium.staff_of_tempest").withStyle(ChatFormatting.AQUA));
        }
    }

    public static class MeteorStaff extends Item {
        public MeteorStaff(Properties props) { super(props); }
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            if (!level.isClientSide) {
                user.getCooldowns().addCooldown(this, 35);
                Vec3 look = user.getLookAngle();
                for (int i = -1; i <= 1; i++) {
                    Vec3 dir = look.yRot((float) Math.toRadians(i * 6.0));
                    SmallFireball fb = new SmallFireball(level, user, dir);
                    fb.setPos(user.getX() + dir.x * 1.2, user.getEyeY() - 0.1, user.getZ() + dir.z * 1.2);
                    level.addFreshEntity(fb);
                }
                level.playSound(null, user.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.2f, 0.9f);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        @Override public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {
            t.add(Component.translatable("tooltip.aetherium.staff_of_meteors").withStyle(ChatFormatting.GOLD));
        }
    }

    public static class HealingStaff extends Item {
        public HealingStaff(Properties props) { super(props); }
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            if (!level.isClientSide && level instanceof ServerLevel sl) {
                user.getCooldowns().addCooldown(this, 100);
                for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(8.0), e -> !(e instanceof Monster))) {
                    e.heal(10.0f);
                    e.removeEffect(MobEffects.POISON);
                    e.removeEffect(MobEffects.WITHER);
                    e.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                    e.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 160, 1));
                    e.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 400, 1));
                }
                sl.sendParticles(ParticleTypes.HEART, user.getX(), user.getY(0.5), user.getZ(), 18, 1.0, 0.5, 1.0, 0.1);
                level.playSound(null, user.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.5f, 1.3f);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        @Override public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {
            t.add(Component.translatable("tooltip.aetherium.staff_of_healing").withStyle(ChatFormatting.GREEN));
        }
    }

    public static class StarlightBow extends Item {
        public StarlightBow(Properties props) { super(props); }
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            if (!level.isClientSide) {
                user.getCooldowns().addCooldown(this, 16);
                for (float off : new float[]{-5.0f, 0.0f, 5.0f}) {
                    Arrow arrow = new Arrow(level, user, new ItemStack(Items.ARROW), stack);
                    arrow.shootFromRotation(user, user.getXRot(), user.getYRot() + off, 0.0f, 3.6f, 0.5f);
                    arrow.setCritArrow(true);
                    arrow.setBaseDamage(4.5);
                    arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    level.addFreshEntity(arrow);
                }
                level.playSound(null, user.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0f, 1.3f);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        @Override public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {
            t.add(Component.translatable("tooltip.aetherium.starlight_bow").withStyle(ChatFormatting.AQUA));
        }
    }

    public static class GrappleItem extends Item {
        public GrappleItem(Properties props) { super(props); }
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            Vec3 start = user.getEyePosition();
            Vec3 end = start.add(user.getLookAngle().scale(45.0));
            BlockHitResult hit = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, user));
            if (hit.getType() != HitResult.Type.MISS) {
                user.getCooldowns().addCooldown(this, 30);
                Vec3 pull = hit.getLocation().subtract(user.position()).normalize().scale(2.15);
                user.setDeltaMovement(pull.x, Math.max(0.45, pull.y * 1.15 + 0.25), pull.z);
                user.hurtMarked = true;
                user.fallDistance = 0.0f;
                level.playSound(null, user.blockPosition(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.PLAYERS, 1.2f, 1.3f);
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
            }
            return InteractionResultHolder.pass(stack);
        }
        @Override public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {
            t.add(Component.translatable("tooltip.aetherium.aether_grapple").withStyle(ChatFormatting.AQUA));
        }
    }

    public static class WindTalisman extends Item {
        public WindTalisman(Properties props) { super(props); }
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            user.getCooldowns().addCooldown(this, 45);
            Vec3 look = user.getLookAngle();
            user.setDeltaMovement(look.x * 1.65, Math.max(0.65, look.y * 1.35 + 0.35), look.z * 1.65);
            user.hurtMarked = true;
            user.fallDistance = 0.0f;
            if (!level.isClientSide) {
                user.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 120, 0, true, false, true));
                user.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, true, false, true));
                level.playSound(null, user.blockPosition(), SoundEvents.BREEZE_JUMP, SoundSource.PLAYERS, 1.3f, 1.15f);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        @Override public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {
            t.add(Component.translatable("tooltip.aetherium.wind_talisman").withStyle(ChatFormatting.AQUA));
        }
    }

    public static class MagnetItem extends Item {
        public MagnetItem(Properties props) { super(props); }
        @Override
        public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
            if (!level.isClientSide && entity instanceof Player p && !p.isShiftKeyDown() && (slot < 9 || p.getOffhandItem() == stack)) {
                pull(level, p, 10.0);
            }
        }
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            if (!level.isClientSide) {
                user.getCooldowns().addCooldown(this, 30);
                int c = pull(level, user, 18.0);
                user.displayClientMessage(Component.translatable("message.aetherium.magnet_pulled", c).withStyle(ChatFormatting.AQUA), true);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        private int pull(Level level, Player p, double r) {
            int c = 0;
            for (ItemEntity ie : level.getEntitiesOfClass(ItemEntity.class, p.getBoundingBox().inflate(r))) {
                if (!ie.hasPickUpDelay()) {
                    ie.setDeltaMovement(p.position().add(0, 0.5, 0).subtract(ie.position()).normalize().scale(0.45));
                    ie.hurtMarked = true;
                    c++;
                }
            }
            for (ExperienceOrb xo : level.getEntitiesOfClass(ExperienceOrb.class, p.getBoundingBox().inflate(r))) {
                xo.setDeltaMovement(p.position().add(0, 0.5, 0).subtract(xo.position()).normalize().scale(0.45));
                xo.hurtMarked = true;
                c++;
            }
            return c;
        }
        @Override public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {
            t.add(Component.translatable("tooltip.aetherium.magnet_charm").withStyle(ChatFormatting.AQUA));
        }
    }

    public static class RelicItem extends Item {
        private final String key;
        private final ChatFormatting color;
        private final boolean foil;
        private final int mode; // 1 = Haste Ring, 2 = Immortality Ring

        public RelicItem(Properties props, String key, ChatFormatting color, boolean foil, int mode) {
            super(props);
            this.key = key;
            this.color = color;
            this.foil = foil;
            this.mode = mode;
        }

        @Override
        public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
            if (!level.isClientSide && entity instanceof Player p && level.getGameTime() % 20 == 0 && (slot < 9 || p.getOffhandItem() == stack)) {
                if (mode == 1) {
                    p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 1, true, false, true));
                    p.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 60, 1, true, false, true));
                    p.addEffect(new MobEffectInstance(MobEffects.JUMP, 60, 0, true, false, true));
                } else if (mode == 2) {
                    p.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 0, true, false, true));
                    if (p.getHealth() <= 8.0f && !p.getCooldowns().isOnCooldown(this)) {
                        p.getCooldowns().addCooldown(this, 500);
                        p.heal(10.0f);
                        p.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 160, 2));
                        p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 160, 1));
                        p.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 240, 2));
                        level.playSound(null, p.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 0.8f, 1.2f);
                        p.displayClientMessage(Component.translatable("message.aetherium.phoenix_triggered").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), true);
                    }
                }
            }
        }

        @Override public boolean isFoil(ItemStack stack) { return foil || super.isFoil(stack); }
        @Override public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {
            t.add(Component.translatable(key).withStyle(color));
        }
    }

    public static class CustomArmor extends ArmorItem {
        private final int tierType; // 1 = Aetherium, 2 = Pyrosite, 3 = Voidite
        public CustomArmor(Holder<ArmorMaterial> mat, Type type, Properties props, int tierType) {
            super(mat, type, props);
            this.tierType = tierType;
        }

        @Override
        public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
            if (!level.isClientSide && entity instanceof Player p) {
                if (this.getEquipmentSlot() == EquipmentSlot.FEET && p.getItemBySlot(EquipmentSlot.FEET) == stack) {
                    p.fallDistance = 0.0f;
                }
                if (level.getGameTime() % 20 == 0 && this.getEquipmentSlot() == EquipmentSlot.HEAD && p.getItemBySlot(EquipmentSlot.HEAD) == stack) {
                    p.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, true, false, true));
                    if (tierType == 1) {
                        p.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0, true, false, true));
                        p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 0, true, false, true));
                        p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60, 0, true, false, true));
                        p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 0, true, false, true));
                    } else if (tierType == 2) {
                        p.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 0, true, false, true));
                        p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60, 1, true, false, true));
                        p.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 60, 0, true, false, true));
                        for (Monster m : level.getEntitiesOfClass(Monster.class, p.getBoundingBox().inflate(4.5))) {
                            m.igniteForSeconds(4.0f);
                            m.hurt(level.damageSources().onFire(), 2.0f);
                        }
                    } else if (tierType == 3) {
                        p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60, 1, true, false, true));
                        p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 1, true, false, true));
                        p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 1, true, false, true));
                        p.addEffect(new MobEffectInstance(MobEffects.JUMP, 60, 1, true, false, true));
                        p.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 1, true, false, true));
                    }
                }
            }
            super.inventoryTick(stack, level, entity, slot, selected);
        }

        @Override
        public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {
            if (tierType == 1) {
                t.add(Component.translatable("tooltip.aetherium.armor_set_bonus").withStyle(ChatFormatting.AQUA));
                t.add(Component.translatable("tooltip.aetherium.armor_set_effects").withStyle(ChatFormatting.GOLD));
            } else if (tierType == 2) {
                t.add(Component.translatable("tooltip.aetherium.pyrosite_armor_bonus").withStyle(ChatFormatting.GOLD));
                t.add(Component.translatable("tooltip.aetherium.pyrosite_armor_effects").withStyle(ChatFormatting.RED));
            } else {
                t.add(Component.translatable("tooltip.aetherium.voidite_armor_bonus").withStyle(ChatFormatting.LIGHT_PURPLE));
                t.add(Component.translatable("tooltip.aetherium.voidite_armor_effects").withStyle(ChatFormatting.DARK_PURPLE));
            }
        }
    }

    public static class PortalKeyItem extends Item {
        public static final ResourceKey<Level> AETHER_REALM = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("aetherium", "aether_realm"));
        public PortalKeyItem(Properties props) { super(props); }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            if (!level.isClientSide && user instanceof ServerPlayer sp) {
                MinecraftServer server = sp.getServer();
                if (server == null) return InteractionResultHolder.fail(stack);
                user.getCooldowns().addCooldown(this, 100);
                boolean inAether = level.dimension().equals(AETHER_REALM);
                ServerLevel targetLevel = server.getLevel(inAether ? Level.OVERWORLD : AETHER_REALM);
                if (targetLevel == null) {
                    sp.displayClientMessage(Component.translatable("message.aetherium.dimension_unavailable").withStyle(ChatFormatting.RED), true);
                    return InteractionResultHolder.fail(stack);
                }
                int tx = sp.getBlockX(), tz = sp.getBlockZ();
                int topY = targetLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, tx, tz);
                if (!inAether && topY <= targetLevel.getMinBuildHeight() + 5) {
                    topY = 100;
                    for (int dx = -3; dx <= 3; dx++) {
                        for (int dz = -3; dz <= 3; dz++) {
                            targetLevel.setBlockAndUpdate(new BlockPos(tx + dx, topY - 1, tz + dz), ForgeBlocks.AETHERIUM_BLOCK.get().defaultBlockState());
                        }
                    }
                }
                sp.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 0));
                sp.changeDimension(new DimensionTransition(targetLevel, new Vec3(tx + 0.5, Math.max(topY, 64) + 0.5, tz + 0.5), Vec3.ZERO, sp.getYRot(), sp.getXRot(), DimensionTransition.DO_NOTHING));
                sp.displayClientMessage(Component.translatable(inAether ? "message.aetherium.returned_overworld" : "message.aetherium.entered_aether_realm").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD), true);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        @Override public boolean isFoil(ItemStack s) { return true; }
        @Override public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {
            t.add(Component.translatable("tooltip.aetherium.aether_portal_key").withStyle(ChatFormatting.LIGHT_PURPLE));
        }
    }

    public static class SteedWhistleItem extends Item {
        public SteedWhistleItem(Properties props) { super(props); }
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            if (!level.isClientSide && level instanceof ServerLevel sl) {
                if (user.getVehicle() instanceof SkeletonHorse steed) {
                    user.getCooldowns().addCooldown(this, 40);
                    Vec3 look = user.getLookAngle();
                    steed.setDeltaMovement(look.x * 1.5, 0.85, look.z * 1.5);
                    steed.hurtMarked = true;
                    steed.fallDistance = 0.0f;
                    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
                }
                user.getCooldowns().addCooldown(this, 160);
                SkeletonHorse steed = EntityType.SKELETON_HORSE.create(sl);
                if (steed != null) {
                    steed.moveTo(user.getX(), user.getY(), user.getZ(), user.getYRot(), 0.0f);
                    steed.setTamed(true);
                    steed.setOwnerUUID(user.getUUID());
                    steed.equipSaddle(stack, SoundSource.PLAYERS);
                    steed.setCustomName(Component.translatable("entity.aetherium.celestial_steed").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
                    steed.setCustomNameVisible(true);
                    AttributeInstance hp = steed.getAttribute(Attributes.MAX_HEALTH);
                    if (hp != null) hp.setBaseValue(80.0);
                    steed.setHealth(80.0f);
                    AttributeInstance spd = steed.getAttribute(Attributes.MOVEMENT_SPEED);
                    if (spd != null) spd.setBaseValue(0.40);
                    AttributeInstance jmp = steed.getAttribute(Attributes.JUMP_STRENGTH);
                    if (jmp != null) jmp.setBaseValue(1.15);
                    sl.addFreshEntity(steed);
                    user.startRiding(steed);
                }
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        @Override public boolean isFoil(ItemStack s) { return true; }
        @Override public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {
            t.add(Component.translatable("tooltip.aetherium.aether_steed_whistle").withStyle(ChatFormatting.AQUA));
        }
    }

    public static class ShrineScrollItem extends Item {
        public ShrineScrollItem(Properties props) { super(props); }
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            if (!level.isClientSide && level instanceof ServerLevel sl) {
                user.getCooldowns().addCooldown(this, 100);
                BlockPos center = user.blockPosition().relative(user.getDirection(), 7);
                for (int dx = -4; dx <= 4; dx++) {
                    for (int dz = -4; dz <= 4; dz++) {
                        sl.setBlockAndUpdate(center.offset(dx, -1, dz), Blocks.QUARTZ_BRICKS.defaultBlockState());
                        for (int dy = 0; dy <= 5; dy++) sl.setBlockAndUpdate(center.offset(dx, dy, dz), Blocks.AIR.defaultBlockState());
                    }
                }
                sl.setBlockAndUpdate(center, ForgeBlocks.AETHER_ALTAR.get().defaultBlockState());
                sl.setBlockAndUpdate(center.offset(-3, 0, 0), ForgeBlocks.HEALING_PYLON.get().defaultBlockState());
                sl.setBlockAndUpdate(center.offset(3, 0, 0), ForgeBlocks.HEALING_PYLON.get().defaultBlockState());
                BlockPos cp = center.offset(0, 0, -2);
                sl.setBlockAndUpdate(cp, Blocks.CHEST.defaultBlockState());
                BlockEntity be = sl.getBlockEntity(cp);
                if (be instanceof ChestBlockEntity chest) {
                    chest.setItem(4, new ItemStack(ForgeItems.ARCHON_SUMMONER.get(), 1));
                    chest.setItem(13, new ItemStack(ForgeItems.AETHERIUM_INGOT.get(), 4));
                }
                if (!user.isCreative()) stack.shrink(1);
                user.displayClientMessage(Component.translatable("message.aetherium.shrine_built").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), true);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        @Override public boolean isFoil(ItemStack s) { return true; }
        @Override public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {
            t.add(Component.translatable("tooltip.aetherium.shrine_scroll").withStyle(ChatFormatting.GOLD));
        }
    }

    public static class HourglassItem extends Item {
        public HourglassItem(Properties props) { super(props); }
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            if (!level.isClientSide && level instanceof ServerLevel sl) {
                user.getCooldowns().addCooldown(this, 60);
                if (user.isShiftKeyDown()) {
                    if (sl.isRaining()) {
                        sl.setWeatherParameters(12000, 0, false, false);
                        user.displayClientMessage(Component.translatable("message.aetherium.weather_cleared").withStyle(ChatFormatting.YELLOW), true);
                    } else {
                        sl.setWeatherParameters(0, 6000, true, true);
                        user.displayClientMessage(Component.translatable("message.aetherium.weather_storm").withStyle(ChatFormatting.AQUA), true);
                    }
                } else {
                    long t = sl.getDayTime() % 24000;
                    sl.setDayTime(t >= 12000 ? 6000 : 18000);
                    user.displayClientMessage(Component.translatable(t >= 12000 ? "message.aetherium.time_day" : "message.aetherium.time_night").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), true);
                }
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        @Override public boolean isFoil(ItemStack s) { return true; }
        @Override public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {
            t.add(Component.translatable("tooltip.aetherium.chronos_hourglass.time").withStyle(ChatFormatting.GOLD));
            t.add(Component.translatable("tooltip.aetherium.chronos_hourglass.weather").withStyle(ChatFormatting.AQUA));
        }
    }

    public static class VoidPouchItem extends Item {
        public VoidPouchItem(Properties props) { super(props); }
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getItemInHand(hand);
            PlayerEnderChestContainer ec = user.getEnderChestInventory();
            if (!level.isClientSide && ec != null) {
                user.openMenu(new SimpleMenuProvider((id, inv, p) -> ChestMenu.threeRows(id, inv, ec), Component.translatable("item.aetherium.void_pouch")));
                level.playSound(null, user.blockPosition(), SoundEvents.ENDER_CHEST_OPEN, SoundSource.PLAYERS, 1.0f, 1.1f);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        @Override public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {
            t.add(Component.translatable("tooltip.aetherium.void_pouch").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }

    public static class CodexItem extends Item {
        public CodexItem(Properties props) { super(props); }
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            if (!level.isClientSide) sendCodexGuide(user);
            return InteractionResultHolder.sidedSuccess(user.getItemInHand(hand), level.isClientSide());
        }
        @Override public boolean isFoil(ItemStack s) { return true; }
        @Override public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {
            t.add(Component.translatable("tooltip.aetherium.aether_codex").withStyle(ChatFormatting.AQUA));
        }
    }

    public static void sendCodexGuide(Player user) {
        user.sendSystemMessage(Component.literal("==============================================").withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.BOLD));
        user.sendSystemMessage(Component.translatable("codex.aetherium.title").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
        user.sendSystemMessage(Component.translatable("codex.aetherium.section1").withStyle(ChatFormatting.YELLOW));
        user.sendSystemMessage(Component.translatable("codex.aetherium.section2").withStyle(ChatFormatting.GOLD));
        user.sendSystemMessage(Component.translatable("codex.aetherium.section3").withStyle(ChatFormatting.LIGHT_PURPLE));
        user.sendSystemMessage(Component.translatable("codex.aetherium.section4").withStyle(ChatFormatting.GREEN));
        user.sendSystemMessage(Component.translatable("codex.aetherium.section5").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        user.sendSystemMessage(Component.literal("==============================================").withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.BOLD));
    }
}
