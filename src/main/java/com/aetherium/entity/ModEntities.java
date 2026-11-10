package com.aetherium.entity;

import com.aetherium.AetheriumOdyssey;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<AetheriumArchonEntity> AETHERIUM_ARCHON = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(AetheriumOdyssey.MOD_ID, "aetherium_archon"),
            EntityType.Builder.create(AetheriumArchonEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.85f, 2.65f)
                    .makeFireImmune()
                    .maxTrackingRange(10)
                    .build("aetherium_archon")
    );

    public static void init() {
        FabricDefaultAttributeRegistry.register(AETHERIUM_ARCHON, AetheriumArchonEntity.createArchonAttributes());
    }
}
