package org.night.manaaltar.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.night.manaaltar.network.payload.SyncManaPayload;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ManaSync {
    private ManaSync() {}

    private static final Map<UUID, Integer> last = new HashMap<>();

    public static void syncIfNeeded(ServerPlayer sp, int mana, int max) {
        Integer prev = last.put(sp.getUUID(), mana);
        if (prev == null || prev != mana) {
            PacketDistributor.sendToPlayer(sp, new SyncManaPayload(mana, max));
        }
    }
}
