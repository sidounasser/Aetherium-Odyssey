package com.aetherium.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class AetherCodexItem extends Item {
    public AetherCodexItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            world.playSound(null, user.getBlockPos(), SoundEvents.ITEM_BOOK_PAGE_TURN, SoundCategory.PLAYERS, 1.0f, 1.1f);
            sendCodexGuide(user);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    public static void sendCodexGuide(PlayerEntity user) {
        user.sendMessage(Text.literal("==============================================").formatted(Formatting.DARK_AQUA, Formatting.BOLD), false);
        user.sendMessage(Text.translatable("codex.aetherium.title").formatted(Formatting.AQUA, Formatting.BOLD), false);
        user.sendMessage(Text.translatable("codex.aetherium.section1").formatted(Formatting.YELLOW), false);
        user.sendMessage(Text.translatable("codex.aetherium.section2").formatted(Formatting.GOLD), false);
        user.sendMessage(Text.translatable("codex.aetherium.section3").formatted(Formatting.LIGHT_PURPLE), false);
        user.sendMessage(Text.translatable("codex.aetherium.section4").formatted(Formatting.GREEN), false);
        user.sendMessage(Text.translatable("codex.aetherium.section5").formatted(Formatting.RED, Formatting.BOLD), false);
        user.sendMessage(Text.literal("==============================================").formatted(Formatting.DARK_AQUA, Formatting.BOLD), false);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.aether_codex").formatted(Formatting.AQUA));
    }
}
