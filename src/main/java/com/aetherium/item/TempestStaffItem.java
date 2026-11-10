package com.aetherium.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

public class TempestStaffItem extends Item {
    public TempestStaffItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            user.getItemCooldownManager().set(this, 60); // 3s cooldown

            Vec3d start = user.getCameraPosVec(1.0f);
            Vec3d look = user.getRotationVec(1.0f);
            Vec3d end = start.add(look.multiply(32.0));

            BlockHitResult hit = world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, user));
            Vec3d strikePos = hit.getPos();

            // Particle beam along look vector
            double dist = start.distanceTo(strikePos);
            for (double d = 1.0; d < dist; d += 1.2) {
                Vec3d p = start.add(look.multiply(d));
                serverWorld.spawnParticles(ParticleTypes.ELECTRIC_SPARK, p.x, p.y, p.z, 3, 0.08, 0.08, 0.08, 0.02);
            }

            LightningEntity bolt = EntityType.LIGHTNING_BOLT.create(serverWorld);
            if (bolt != null) {
                bolt.refreshPositionAfterTeleport(strikePos.x, strikePos.y, strikePos.z);
                bolt.setCosmetic(true);
                serverWorld.spawnEntity(bolt);
            }

            Box area = new Box(strikePos.x - 4.0, strikePos.y - 3.0, strikePos.z - 4.0,
                    strikePos.x + 4.0, strikePos.y + 4.0, strikePos.z + 4.0);
            List<LivingEntity> victims = world.getEntitiesByClass(LivingEntity.class, area, e -> e != user && !e.isSpectator() && !e.isTeammate(user));
            for (LivingEntity victim : victims) {
                victim.damage(world.getDamageSources().lightningBolt(), 11.0f);
                serverWorld.spawnParticles(ParticleTypes.ELECTRIC_SPARK, victim.getX(), victim.getBodyY(0.5), victim.getZ(), 15, 0.3, 0.5, 0.3, 0.1);
            }

            world.playSound(null, user.getBlockPos(), SoundEvents.ITEM_TRIDENT_THUNDER.value(), SoundCategory.PLAYERS, 1.3f, 1.2f);
            if (!user.isCreative()) {
                stack.damage(1, user, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.staff_of_tempest").formatted(Formatting.AQUA));
    }
}
