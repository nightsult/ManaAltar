package org.night.manaaltar;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

import org.night.manaaltar.blocks.AltarBreakRules;
import org.night.manaaltar.blocks.BlocksEntities;
import org.night.manaaltar.blocks.Blocks;
import org.night.manaaltar.content.CreativeTab;
import org.night.manaaltar.items.mana_sword.Dash;
import org.night.manaaltar.items.mana_sword.Bonus;
import org.night.manaaltar.items.Items;
import org.night.manaaltar.mana.ManaCommands;
import org.night.manaaltar.mana.Events;
import org.night.manaaltar.sound.Sounds;
import org.night.manaaltar.worldgen.ManaWorldgen;

@Mod(Manaaltar.MOD_ID)
public final class Manaaltar {
    public static final String MOD_ID = "manaaltar";

    public Manaaltar(IEventBus modBus) {
        Blocks.BLOCKS.register(modBus);
        BlocksEntities.BLOCK_ENTITIES.register(modBus);
        Items.ITEMS.register(modBus);
        Sounds.SOUND_EVENTS.register(modBus);

        modBus.addListener(org.night.manaaltar.network.ManaNet::register);
        modBus.addListener(CreativeTab::onBuildTabs);

        ManaWorldgen.FEATURES.register(modBus);
        ManaWorldgen.CONFIGURED.register(modBus);
        ManaWorldgen.PLACED.register(modBus);

        NeoForge.EVENT_BUS.register(new Dash());
        NeoForge.EVENT_BUS.register(new Bonus());
        NeoForge.EVENT_BUS.register(new Events());
        NeoForge.EVENT_BUS.register(new ManaCommands());
        NeoForge.EVENT_BUS.register(new AltarBreakRules());

    }
}
