package org.night.manaaltar.mana;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public final class Data {
    private Data() {}

    public static final String KEY_MANA = "manaaltar.mana";
    public static final String KEY_TICK = "manaaltar.tickCounter";
    public static final int MAX_MANA = 100;

    public static void ensureInitialized(Player player) {
        CompoundTag tag = player.getPersistentData();
        if (!tag.contains(KEY_MANA)) tag.putInt(KEY_MANA, 0);
        if (!tag.contains(KEY_TICK)) tag.putInt(KEY_TICK, 0);
    }

    public static int getMana(Player player) {
        return player.getPersistentData().getInt(KEY_MANA);
    }

    public static void setMana(Player player, int value) {
        player.getPersistentData().putInt(KEY_MANA, clamp(value, 0, MAX_MANA));
    }

    public static void addMana(Player player, int delta) {
        setMana(player, getMana(player) + delta);
    }

    public static int getTickCounter(Player player) {
        return player.getPersistentData().getInt(KEY_TICK);
    }

    public static void setTickCounter(Player player, int ticks) {
        player.getPersistentData().putInt(KEY_TICK, Math.max(0, ticks));
    }

    public static void incTick(Player player) {
        setTickCounter(player, getTickCounter(player) + 1);
    }

    private static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}