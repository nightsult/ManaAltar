package org.night.manaaltar;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

import org.night.manaaltar.blocks.BlocksEntities;
import org.night.manaaltar.blocks.Blocks;
import org.night.manaaltar.client.neres.NeresHud;
import org.night.manaaltar.content.CreativeTab;
import org.night.manaaltar.items.mana_sword.Dash;
import org.night.manaaltar.items.mana_sword.Bonus;
import org.night.manaaltar.items.Items;
import org.night.manaaltar.mana.ManaCommands;
import org.night.manaaltar.mana.Events;

@Mod(Manaaltar.MOD_ID)
public final class Manaaltar {
    public static final String MOD_ID = "manaaltar";

    public Manaaltar(IEventBus modBus) {
        Blocks.BLOCKS.register(modBus);
        BlocksEntities.BLOCK_ENTITIES.register(modBus);
        Items.ITEMS.register(modBus);

        modBus.addListener(CreativeTab::onBuildTabs);

        NeoForge.EVENT_BUS.register(new Dash());
        NeoForge.EVENT_BUS.register(new Bonus());
        NeoForge.EVENT_BUS.register(new Events());
        NeoForge.EVENT_BUS.register(new ManaCommands());

        NeoForge.EVENT_BUS.register(NeresHud.class);

    }
}
