package org.night.manaaltar.client.effects;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.night.manaaltar.Manaaltar;
import org.night.manaaltar.client.state.ClientShake;

@EventBusSubscriber(modid = Manaaltar.MOD_ID, value = Dist.CLIENT)
public final class ShakeHandlers {
    private ShakeHandlers() {}

    @SubscribeEvent
    public static void onCamera(ViewportEvent.ComputeCameraAngles event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        long gt = mc.level.getGameTime();
        if (!ClientShake.active(gt)) return;

        double pt = event.getPartialTick();

        float f = ClientShake.factor(gt, (float) pt);
        if (f <= 0f) return;

        double t = gt + pt;
        float intensity = ClientShake.baseIntensity * f;

        float yawJ   = (float) (Math.sin(t * 25.0) * intensity * 2.0);
        float pitchJ = (float) (Math.cos(t * 27.0) * intensity * 1.5);
        float rollJ  = (float) (Math.sin(t * 21.0) * intensity * 1.0);

        event.setYaw(event.getYaw() + yawJ);
        event.setPitch(event.getPitch() + pitchJ);
        event.setRoll(event.getRoll() + rollJ);
    }
}
