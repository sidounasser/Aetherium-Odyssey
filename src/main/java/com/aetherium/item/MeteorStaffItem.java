package com.aetherium.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class MeteorStaffItem extends Item {
    public MeteorStaffItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            user.getItemCooldownManager().set(this, 35); // 1.75s cooldown

            Vec3d look = user.getRotationVec(1.0f);
            // Fire a spread of 3 solar fireballs
            for (int i = -1; i <= 1; i++) {
                Vec3d dir = look.rotateY((float) Math.toRadians(i * 6.0));
                SmallFireballEntity fireball = new SmallFireballEntity(world, user, dir);
                fireball.setPosition(user.getX() + dir.x * 1.2, user.getEyeY() - 0.1, user.getZ() + dir.z * 1.2);
                world.spawnEntity(fireball);
            }

            serverWorld.spawnParticles(ParticleTypes.FLAME, user.getX() + look.x, user.getEyeY(), user.getZ() + look.z, 20, 0.25, 0.25, 0.25, 0.08);
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1.2f, 0.9f);

            if (!user.isCreative()) {
                stack.damage(1, user, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.staff_of_meteors").formatted(Formatting.GOLD));
    }
}
