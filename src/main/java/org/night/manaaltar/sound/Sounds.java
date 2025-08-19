// org/night/manaaltar/sound/Sounds.java
package org.night.manaaltar.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.night.manaaltar.Manaaltar;

public final class Sounds {
    private Sounds() {}

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, Manaaltar.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> NERES_ZAP =
            SOUND_EVENTS.register("neres_zap",
                    () -> SoundEvent.createFixedRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(Manaaltar.MOD_ID, "neres_zap"),
                            12.0f));
}
