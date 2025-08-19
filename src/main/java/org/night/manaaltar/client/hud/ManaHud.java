package org.night.manaaltar.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.night.manaaltar.Manaaltar;
import org.night.manaaltar.client.state.ClientMana;
import org.night.manaaltar.items.mana_sword.Sword;

@EventBusSubscriber(modid = Manaaltar.MOD_ID, value = Dist.CLIENT)
public final class ManaHud {
    private ManaHud() {}

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post e) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        if (mc.screen != null) return;

        Player p = mc.player;
        if (!(p.getMainHandItem().getItem() instanceof Sword) &&
                !(p.getOffhandItem().getItem() instanceof Sword)) {
            return;
        }

        int mana = Math.max(0, ClientMana.mana);
        int max  = Math.max(1, ClientMana.max);

        GuiGraphics g = e.getGuiGraphics();
        var font = mc.font;

        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();

        int hotbarW = 182;
        int hotbarH = 22;
        int hotbarX = (sw - hotbarW) / 2;
        int hotbarY = sh - hotbarH;

        int margin = 6;
        int barW = 90;
        int barH = 9;
        int bx = hotbarX - margin - barW;
        int by = hotbarY + (hotbarH - barH) / 2;
        if (bx < 2) bx = 2;

        double frac = mana / (double) max;
        int filled = (int) Math.round(barW * frac);

        g.fill(bx, by, bx + barW, by + barH, 0x88000000);
        g.fill(bx, by, bx + filled, by + barH, 0xFF00A6FF);
        g.fill(bx, by, bx + barW, by + 1, 0xFFFFFFFF);
        g.fill(bx, by + barH - 1, bx + barW, by + barH, 0xFFFFFFFF);
        g.fill(bx, by, bx + 1, by + barH, 0xFFFFFFFF);
        g.fill(bx + barW - 1, by, bx + barW, by + barH, 0xFFFFFFFF);

        String value = mana + " / " + max;
        int vx = bx + (barW - font.width(value)) / 2;
        int vy = by - 10;
        if (vy < 0) vy = 0;
        g.drawString(font, value, vx, vy, 0xA0E6FF, true);
    }
}
