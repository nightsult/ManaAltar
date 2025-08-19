package org.night.manaaltar.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.night.manaaltar.Manaaltar;

public record ScreenShakePayload(int durationTicks, float intensity) implements CustomPacketPayload {
    public static final Type<ScreenShakePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Manaaltar.MOD_ID, "screen_shake"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ScreenShakePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, ScreenShakePayload::durationTicks,
                    ByteBufCodecs.FLOAT,   ScreenShakePayload::intensity,
                    ScreenShakePayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
