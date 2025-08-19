package org.night.manaaltar.client.neres;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.night.manaaltar.Manaaltar;
import org.night.manaaltar.blocks.neres.NeresEntity;

@EventBusSubscriber(modid = Manaaltar.MOD_ID, value = Dist.CLIENT)
public final class NeresHud {
    private NeresHud() {}

    @SubscribeEvent
    public static void onRenderHud(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        if (mc.screen != null) return;

        HitResult hit = mc.hitResult;
        if (!(hit instanceof BlockHitResult bhr) || hit.getType() != HitResult.Type.BLOCK) return;

        BlockPos pos = bhr.getBlockPos();
        if (!(mc.level.getBlockEntity(pos) instanceof NeresEntity be)) return;

        int mana = be.getMana();
        int max  = be.getMaxMana();
        int kills = be.getCreepersKilled();

        GuiGraphics g = event.getGuiGraphics();
        var font = mc.font;
        int w = mc.getWindow().getGuiScaledWidth();

        String title = "Altar de Neres";
        String killsTxt = "Creepers: " + kills;
        String manaTxt  = "Mana: " + mana + " / " + max;

        int margin = 6;
        int y = 6;

        int titleX = w - margin - font.width(title);
        int killsX = w - margin - font.width(killsTxt);
        int manaX  = w - margin - font.width(manaTxt);

        g.drawString(font, title,    titleX, y,        0xFFFFFF, true);
        g.drawString(font, killsTxt, killsX, y + 10,   0xFFD670, true);
        g.drawString(font, manaTxt,  manaX,  y + 20,   0xA0FFD6, true);

        // Barrinha de mana
        int barW = 100, barH = 8;
        int filled = Math.max(0, Math.min(barW, (int)Math.round(barW * (mana / (double)max))));
        int bx = w - margin - barW;
        int by = y + 34;

        g.fill(bx, by, bx + barW, by + barH, 0x88000000);
        g.fill(bx, by, bx + filled, by + barH, 0xFF00D8FF);
        g.fill(bx, by, bx + barW, by + 1, 0xFFFFFFFF);
        g.fill(bx, by + barH - 1, bx + barW, by + barH, 0xFFFFFFFF);
        g.fill(bx, by, bx + 1, by + barH, 0xFFFFFFFF);
        g.fill(bx + barW - 1, by, bx + barW, by + barH, 0xFFFFFFFF);
    }
}
