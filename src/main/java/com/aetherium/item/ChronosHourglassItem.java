package com.aetherium.item;

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
import net.minecraft.world.World;

import java.util.List;

public class ChronosHourglassItem extends Item {
    public ChronosHourglassItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            user.getItemCooldownManager().set(this, 60);

            if (user.isSneaking()) {
                // Toggle weather between Clear and Thunder
                if (serverWorld.isRaining() || serverWorld.isThundering()) {
                    serverWorld.setWeather(12000, 0, false, false);
                    user.sendMessage(Text.translatable("message.aetherium.weather_cleared").formatted(Formatting.YELLOW), true);
                } else {
                    serverWorld.setWeather(0, 6000, true, true);
                    user.sendMessage(Text.translatable("message.aetherium.weather_storm").formatted(Formatting.AQUA), true);
                }
            } else {
                // Toggle time between Solar Noon (6000) and Starlight Midnight (18000)
                long currentTime = serverWorld.getTimeOfDay() % 24000;
                if (currentTime >= 12000) {
                    serverWorld.setTimeOfDay(6000);
                    user.sendMessage(Text.translatable("message.aetherium.time_day").formatted(Formatting.GOLD, Formatting.BOLD), true);
                } else {
                    serverWorld.setTimeOfDay(18000);
                    user.sendMessage(Text.translatable("message.aetherium.time_night").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD), true);
                }
            }

            serverWorld.spawnParticles(ParticleTypes.END_ROD, user.getX(), user.getY() + 1.2, user.getZ(), 45, 0.8, 0.8, 0.8, 0.12);
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_BELL_RESONATE, SoundCategory.PLAYERS, 1.2f, 1.3f);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.aetherium.chronos_hourglass.time").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.aetherium.chronos_hourglass.weather").formatted(Formatting.AQUA));
    }
}
