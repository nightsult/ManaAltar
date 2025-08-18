package org.night.manaaltar.mana;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class Events {

    private static final int TICKS_PER_SECOND = 20;
    private static final int REGEN_INTERVAL_TICKS = 5 * TICKS_PER_SECOND;
    private static final int REGEN_AMOUNT = 1;
    private static final int KILL_HOSTILE_BONUS = 5;

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.isSpectator() || !player.isAlive()) return;

        Data.ensureInitialized(player);

        Data.incTick(player);
        int ticks = Data.getTickCounter(player);
        if (ticks >= REGEN_INTERVAL_TICKS) {
            Data.setTickCounter(player, 0);
            if (Data.getMana(player) < Data.MAX_MANA) {
                Data.addMana(player, REGEN_AMOUNT);
            }
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        Entity src = event.getSource().getEntity();
        if (!(src instanceof Player player)) return;
        if (!(victim instanceof Enemy)) return;

        Data.ensureInitialized(player);
        if (Data.getMana(player) < Data.MAX_MANA) {
            Data.addMana(player, KILL_HOSTILE_BONUS);
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (!(event.getOriginal() instanceof Player oldP)) return;
        if (!(event.getEntity()   instanceof Player newP)) return;

        Data.ensureInitialized(oldP);
        int oldMana = Data.getMana(oldP);

        Data.ensureInitialized(newP);
        Data.setMana(newP, oldMana);

        Data.setTickCounter(newP, 0);
    }

    @SubscribeEvent
    public void onLogin(PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof Player p) {
            Data.ensureInitialized(p);
        }
    }
}