package org.night.manaaltar.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.night.manaaltar.network.payload.ScreenShakePayload;

public final class ScreenShake {
    private ScreenShake() {}

    public static void trigger(ServerPlayer player, int durationTicks, float intensity) {
        PacketDistributor.sendToPlayer(player, new ScreenShakePayload(durationTicks, intensity));
    }
}
