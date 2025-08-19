package org.night.manaaltar.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.night.manaaltar.Manaaltar;

public record SyncManaPayload(int mana, int max) implements CustomPacketPayload {
    public static final Type<SyncManaPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Manaaltar.MOD_ID, "sync_mana"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncManaPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, SyncManaPayload::mana,
                    ByteBufCodecs.VAR_INT, SyncManaPayload::max,
                    SyncManaPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
