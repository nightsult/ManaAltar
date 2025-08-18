package org.night.manaaltar.content;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.night.manaaltar.items.Items;

public final class CreativeTab {
    private CreativeTab() {}

    public static void onBuildTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(Items.MANA_SWORD.get());
        }
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(Items.MANA_ALTAR_ITEM.get());
        }
    }
}