package org.night.manaaltar.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.night.manaaltar.Manaaltar;

public final class ManaWorldgen {
    private ManaWorldgen() {}

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, Manaaltar.MOD_ID);
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED =
            DeferredRegister.create(Registries.CONFIGURED_FEATURE, Manaaltar.MOD_ID);
    public static final DeferredRegister<PlacedFeature> PLACED =
            DeferredRegister.create(Registries.PLACED_FEATURE, Manaaltar.MOD_ID);

    public static final DeferredHolder<Feature<?>, ManaEggFeature> MANA_EGG_FEATURE =
            FEATURES.register("mana_egg", () -> new ManaEggFeature(NoneFeatureConfiguration.CODEC));

    public static final DeferredHolder<ConfiguredFeature<?, ?>, ConfiguredFeature<NoneFeatureConfiguration, ?>>
            MANA_EGG_CONFIGURED = CONFIGURED.register("mana_egg",
            () -> new ConfiguredFeature<>(MANA_EGG_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));

    public static final DeferredHolder<PlacedFeature, PlacedFeature> MANA_EGG_PLACED =
            PLACED.register("mana_egg", () -> new PlacedFeature(
                    net.minecraft.core.Holder.direct(MANA_EGG_CONFIGURED.get()),
                    java.util.List.of(
                            RarityFilter.onAverageOnceEvery(3906),
                            InSquarePlacement.spread(),
                            HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                            BiomeFilter.biome()
                    )
            ));
}
