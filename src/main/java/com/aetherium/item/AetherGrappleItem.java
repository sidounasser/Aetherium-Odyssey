package com.aetherium.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

public class AetherGrappleItem extends Item {
    public AetherGrappleItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        Vec3d start = user.getCameraPosVec(1.0f);
        Vec3d look = user.getRotationVec(1.0f);
        Vec3d end = start.add(look.multiply(45.0));

        BlockHitResult hit = world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, user));
        if (hit.getType() != HitResult.Type.MISS) {
            user.getItemCooldownManager().set(this, 30); // 1.5s cooldown
            Vec3d target = hit.getPos();
            Vec3d pull = target.subtract(user.getPos()).normalize().multiply(2.15);
            user.setVelocity(pull.x, Math.max(0.45, pull.y * 1.15 + 0.25), pull.z);
            user.velocityModified = true;
            user.fallDistance = 0.0f;

            if (!world.isClient && world instanceof ServerWorld serverWorld) {
                double dist = start.distanceTo(target);
                for (double d = 1.0; d < dist; d += 1.0) {
                    Vec3d p = start.add(look.multiply(d));
                    serverWorld.spawnParticles(ParticleTypes.END_ROD, p.x, p.y, p.z, 1, 0.02, 0.02, 0.02, 0.0);
                }
                world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.PLAYERS, 1.2f, 1.3f);
                world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.6f, 1.5f);
                if (!user.isCreative()) {
                    stack.damage(1, user, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                }
            }
            return TypedActionResult.success(stack, world.isClient());
        }
        return TypedActionResult.pass(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.aether_grapple").formatted(Formatting.AQUA));
    }
}
