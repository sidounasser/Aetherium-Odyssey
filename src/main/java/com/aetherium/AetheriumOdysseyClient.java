package com.aetherium;

import com.aetherium.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.WitherSkeletonEntityRenderer;

public class AetheriumOdysseyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.AETHERIUM_ARCHON, WitherSkeletonEntityRenderer::new);
    }
}
