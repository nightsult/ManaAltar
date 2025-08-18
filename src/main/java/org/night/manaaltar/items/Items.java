package org.night.manaaltar.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.night.manaaltar.Manaaltar;
import org.night.manaaltar.blocks.Blocks;
import org.night.manaaltar.items.mana_sword.Sword;

import java.util.function.Supplier;

public final class Items {
    private Items() {}

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Manaaltar.MOD_ID);

    public static final Supplier<Item> MANA_SWORD = ITEMS.registerItem(
            "mana_sword",
            Sword::new,
            new Item.Properties().durability(1561)
    );

    public static final Supplier<Item> MANA_ALTAR_ITEM = ITEMS.registerItem(
            "mana_altar",
            props -> new BlockItem(Blocks.MANA_ALTAR.get(), props),
            new Item.Properties()
    );
}
