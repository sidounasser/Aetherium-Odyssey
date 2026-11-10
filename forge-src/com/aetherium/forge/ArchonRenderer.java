package com.aetherium.forge;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;

public class ArchonRenderer extends SkeletonRenderer<ForgeArchonBoss> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/entity/skeleton/wither_skeleton.png");

    public ArchonRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ForgeArchonBoss entity) {
        return TEXTURE;
    }
}
