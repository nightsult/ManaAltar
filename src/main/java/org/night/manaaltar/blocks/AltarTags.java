package org.night.manaaltar.blocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.night.manaaltar.Manaaltar;

public final class AltarTags {
    private AltarTags() {}
    public static final TagKey<Item> ALTAR_BREAKERS =
            TagKey.create(Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(Manaaltar.MOD_ID, "altar_breakers"));
}
