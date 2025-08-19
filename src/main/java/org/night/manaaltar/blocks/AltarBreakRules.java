package org.night.manaaltar.blocks;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public final class AltarBreakRules {

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent e) {
        var p = e.getPlayer();
        if (p == null || p.isCreative()) return;

        var b = e.getState().getBlock();
        if (b != Blocks.MANA_ALTAR.get() && b != Blocks.NERES_ALTAR.get()) return;

        var tool = p.getMainHandItem();

        boolean isPickaxe = tool.is(ItemTags.PICKAXES);
        boolean allowedTier = tool.is(AltarTags.ALTAR_BREAKERS) || tool.is(Items.NETHERITE_PICKAXE);

        if (!(isPickaxe && allowedTier)) {
            e.setCanceled(true);
        }
    }
}
