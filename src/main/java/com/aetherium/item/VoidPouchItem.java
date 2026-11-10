package com.aetherium.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class VoidPouchItem extends Item {
    public VoidPouchItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        EnderChestInventory enderChest = user.getEnderChestInventory();
        if (!world.isClient && enderChest != null) {
            user.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, playerInventory, player) -> GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, enderChest),
                    Text.translatable("item.aetherium.void_pouch")
            ));
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.PORTAL, user.getX(), user.getBodyY(0.5), user.getZ(), 20, 0.3, 0.4, 0.3, 0.1);
            }
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.PLAYERS, 1.0f, 1.1f);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.void_pouch").formatted(Formatting.DARK_PURPLE));
    }
}
