package org.night.manaaltar.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.night.manaaltar.client.state.ClientMana;
import org.night.manaaltar.client.state.ClientShake;
import org.night.manaaltar.network.payload.ScreenShakePayload;
import org.night.manaaltar.network.payload.SyncManaPayload;

public final class ManaNet {
    private ManaNet() {}
    public static final String NET_VERSION = "1";

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar reg = event.registrar(NET_VERSION);
        reg.playToClient(
                SyncManaPayload.TYPE,
                SyncManaPayload.STREAM_CODEC,
                (payload, ctx) -> ctx.enqueueWork(() -> {
                    ClientMana.mana = payload.mana();
                    ClientMana.max  = payload.max();
                })
        );
        reg.playToClient(
                ScreenShakePayload.TYPE,
                ScreenShakePayload.STREAM_CODEC,
                (payload, ctx) -> ctx.enqueueWork(() -> {
                    var mc = net.minecraft.client.Minecraft.getInstance();
                    if (mc.level != null) {
                        ClientShake.start(mc.level.getGameTime(), payload.durationTicks(), payload.intensity());
                    }
                })
        );
    }
}
