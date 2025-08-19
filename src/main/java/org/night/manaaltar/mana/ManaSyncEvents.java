package org.night.manaaltar.mana;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.night.manaaltar.Manaaltar;
import org.night.manaaltar.network.ManaSync;

@EventBusSubscriber(modid = Manaaltar.MOD_ID)
public final class ManaSyncEvents {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post e) {
        if (e.getEntity().level().isClientSide) return;
        if (!(e.getEntity() instanceof ServerPlayer sp)) return;

        int mana = Data.getMana(sp);
        int max  = Data.MAX_MANA;
        ManaSync.syncIfNeeded(sp, mana, max);
    }
}
