package com.aetherium.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class StarlightBowItem extends Item {
    public StarlightBowItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            user.getItemCooldownManager().set(this, 16); // Fast 0.8s fire rate

            // Fires a 3-arrow astral volley without requiring arrows in inventory!
            float[] yawOffsets = new float[]{-5.0f, 0.0f, 5.0f};
            for (float offset : yawOffsets) {
                ArrowEntity arrow = new ArrowEntity(world, user, new ItemStack(Items.SPECTRAL_ARROW), stack);
                arrow.setVelocity(user, user.getPitch(), user.getYaw() + offset, 0.0f, 3.6f, 0.5f);
                arrow.setCritical(true);
                arrow.setDamage(4.5);
                arrow.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                world.spawnEntity(arrow);
            }

            serverWorld.spawnParticles(ParticleTypes.END_ROD, user.getX(), user.getEyeY() - 0.2, user.getZ(), 10, 0.2, 0.2, 0.2, 0.08);
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.3f);
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.PLAYERS, 0.8f, 1.5f);

            if (!user.isCreative()) {
                stack.damage(1, user, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.starlight_bow").formatted(Formatting.AQUA));
    }
}
