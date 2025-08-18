package org.night.manaaltar.items.mana_sword;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.night.manaaltar.mana.Data;

public class Dash {

    private static final String KEY_DASH_COOLDOWN_UNTIL = "manaaltar.dashCooldownUntil";
    private static final String KEY_DASH_BUFF_UNTIL     = "manaaltar.dashBuffUntil";

    private static final int TPS = 20;
    private static final long COOLDOWN_TICKS = 1L * TPS;
    private static final long BUFF_TICKS     = 15L * TPS;

    private static final double POWER = 2.6;
    private static final double LIFT  = 0.15;

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player p = event.getEntity();
        if (!(p instanceof ServerPlayer sp)) return;
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        if (!(sp.getMainHandItem().getItem() instanceof Sword)) return;

        long now = sp.level().getGameTime();

        long cdUntil   = sp.getPersistentData().getLong(KEY_DASH_COOLDOWN_UNTIL);
        if (now < cdUntil) {
            event.setCanceled(true);
            return;
        }

        long buffUntil = sp.getPersistentData().getLong(KEY_DASH_BUFF_UNTIL);
        boolean inBuffWindow = now <= buffUntil;

        if (!inBuffWindow) {
            int mana = Data.getMana(sp);
            if (mana < 100) {
                sp.displayClientMessage(Component.literal("§cPrecisa de §e100§c de mana para usar o impulso."), true);
                event.setCanceled(true);
                return;
            }
            Data.setMana(sp, mana - 100);
            sp.getPersistentData().putLong(KEY_DASH_BUFF_UNTIL, now + BUFF_TICKS);
        }

        sp.getPersistentData().putLong(KEY_DASH_COOLDOWN_UNTIL, now + COOLDOWN_TICKS);

        Vec3 look = sp.getLookAngle().normalize();
        Vec3 impulse = new Vec3(look.x * POWER, Math.max(LIFT, look.y * 0.2), look.z * POWER);
        sp.setDeltaMovement(sp.getDeltaMovement().add(impulse));
        sp.hurtMarked = true;

        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onPlayerTickEnd(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
        if (sp.level().isClientSide()) return;

        long now = sp.level().getGameTime();
        long buffUntil = sp.getPersistentData().getLong(KEY_DASH_BUFF_UNTIL);

        if (now <= buffUntil) {
            long ticksLeft = buffUntil - now;
            int secondsLeft = (int)Math.ceil(ticksLeft / (double)TPS);

            sp.displayClientMessage(
                    Component.literal("§bImpulso disponível: §f" + secondsLeft + "§bs"),
                    true
            );

        } else {
        }
    }
}
