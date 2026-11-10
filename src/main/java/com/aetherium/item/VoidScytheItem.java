package com.aetherium.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

public class VoidScytheItem extends SwordItem {
    public VoidScytheItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 80, 1), attacker);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1), attacker);
        attacker.heal(2.5f); // Vampiric life steal
        if (attacker.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.SOUL, target.getX(), target.getBodyY(0.5), target.getZ(), 10, 0.3, 0.4, 0.3, 0.05);
            serverWorld.spawnParticles(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getBodyY(0.6), target.getZ(), 4, 0.2, 0.2, 0.2, 0.05);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            user.getItemCooldownManager().set(this, 70); // 3.5s cooldown

            Vec3d start = user.getCameraPosVec(1.0f);
            Vec3d look = user.getRotationVec(1.0f);
            Vec3d end = start.add(look.multiply(14.0));

            BlockHitResult hit = world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, user));
            Vec3d dest = hit.getType() == HitResult.Type.MISS ? end : hit.getPos().subtract(look.multiply(1.0));

            serverWorld.spawnParticles(ParticleTypes.PORTAL, user.getX(), user.getBodyY(0.5), user.getZ(), 35, 0.3, 0.6, 0.3, 0.15);
            user.requestTeleport(dest.x, dest.y + 0.2, dest.z);
            user.fallDistance = 0.0f;
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 80, 1));

            // Shadow slash at destination
            Box arrivalBox = user.getBoundingBox().expand(3.5);
            List<LivingEntity> victims = world.getEntitiesByClass(LivingEntity.class, arrivalBox, e -> e != user && !e.isSpectator() && !e.isTeammate(user));
            for (LivingEntity victim : victims) {
                victim.damage(world.getDamageSources().magic(), 6.0f);
                victim.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 80, 1));
                user.heal(1.5f);
            }

            serverWorld.spawnParticles(ParticleTypes.REVERSE_PORTAL, user.getX(), user.getBodyY(0.5), user.getZ(), 40, 0.4, 0.6, 0.4, 0.1);
            serverWorld.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, user.getX(), user.getBodyY(0.5), user.getZ(), 20, 0.5, 0.4, 0.5, 0.05);
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.2f, 0.9f);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.void_scythe.passive").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("tooltip.aetherium.void_scythe.active").formatted(Formatting.GOLD));
    }
}
