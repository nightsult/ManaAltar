package org.night.manaaltar.blocks.neres;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.night.manaaltar.blocks.BlocksEntities;

public class NeresEntity extends BlockEntity {

    // Mana
    public static final int MAX_MANA = 1000;
    public static final int GEN_AMOUNT = 5;
    public static final int GEN_INTERVAL_TICKS = 20;

    // Defesa anti-creeper
    private static final int KILL_RADIUS = 50;
    private static final int MANA_PER_CREEPER = 25;
    private static final int SWEEP_INTERVAL_TICKS = 20;
    private static final int MAX_KILLS_PER_SWEEP = 8;

    private static final String TAG_MANA = "Mana";
    private static final String TAG_CREEPERS = "CreepersKilled";

    private static final int STEAL_RADIUS = 10;
    private static final int STEAL_INTERVAL_TICKS = 5;
    private static final int STEAL_PER_PULL = 5;
    private static final int STEAL_MAX_PER_SWEEP = 50;

    private int mana = 0;
    private int creepersKilled = 0;

    public NeresEntity(BlockPos pos, BlockState state) {
        super(BlocksEntities.NERES_ALTAR_BE.get(), pos, state);
    }

    public static void tickServer(Level level, BlockPos pos, BlockState state, NeresEntity be) {
        if (level == null || level.isClientSide) return;

        long gt = level.getGameTime();
        long phase = (pos.asLong() & 15L);

        if ((gt % GEN_INTERVAL_TICKS) == 0L && be.mana < MAX_MANA) {
            int old = be.mana;
            be.mana = Math.min(MAX_MANA, be.mana + GEN_AMOUNT);
            if (be.mana != old) be.sync();
        }

        if (level instanceof ServerLevel sl) {
            if (((gt + phase) % STEAL_INTERVAL_TICKS) == 0L && be.mana > 0) {

                int sweepBudget = Math.min(be.mana, STEAL_MAX_PER_SWEEP);
                if (sweepBudget > 0) {
                    AABB boxPlayers = new AABB(pos).inflate(STEAL_RADIUS);
                    var players = sl.getEntitiesOfClass(
                            ServerPlayer.class,
                            boxPlayers,
                            p -> p.isAlive()
                                    && p.isCrouching()
                                    && (
                                    p.getMainHandItem().getItem() instanceof org.night.manaaltar.items.mana_sword.Sword
                                            || p.getOffhandItem().getItem() instanceof org.night.manaaltar.items.mana_sword.Sword
                            )
                    );

                    boolean changed = false;

                    for (ServerPlayer sp : players) {
                        if (sweepBudget <= 0) break;

                        int playerMana = org.night.manaaltar.mana.Data.getMana(sp);
                        int playerMax  = org.night.manaaltar.mana.Data.MAX_MANA;
                        int space      = Math.max(0, playerMax - playerMana);
                        if (space <= 0) continue;

                        int give = Math.min(Math.min(STEAL_PER_PULL, sweepBudget), space);
                        if (give <= 0) continue;

                        org.night.manaaltar.mana.Data.addMana(sp, give);
                        be.mana -= give;
                        sweepBudget -= give;
                        changed = true;

                        spawnStealParticles(sl, sp);
                    }

                    if (changed) be.sync();
                }
            }
        }

        if (((gt + phase) % SWEEP_INTERVAL_TICKS) != 0L) return;

        int budget = be.mana / MANA_PER_CREEPER;
        if (budget <= 0) return;

        AABB box = new AABB(pos).inflate(KILL_RADIUS);
        var creepers = level.getEntitiesOfClass(Creeper.class, box, c -> c.isAlive() && !c.isRemoved());
        if (creepers.isEmpty()) return;

        int killed = 0;

        for (Creeper c : creepers) {
            if (killed >= MAX_KILLS_PER_SWEEP) break;
            if (budget <= 0) break;
            if (!(level instanceof ServerLevel sl)) break;

            float pitch = 0.95f + sl.random.nextFloat() * 0.1f;
            sl.playSeededSound(
                    null,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    org.night.manaaltar.sound.Sounds.NERES_ZAP.get(),
                    net.minecraft.sounds.SoundSource.BLOCKS,
                    1.0f,
                    pitch,
                    pos.asLong() ^ gt
            );

            boolean died = c.hurt(sl.damageSources().fellOutOfWorld(), 1000.0F)
                    || !c.isAlive() || c.isDeadOrDying() || c.isRemoved();
            if (!died) { c.discard(); died = true; }

            if (died) {
                be.creepersKilled++;
                be.mana = Math.max(0, be.mana - MANA_PER_CREEPER);
                budget--;
                killed++;
            }
        }

        if (killed > 0) be.sync();
    }


    private static void spawnStealParticles(ServerLevel sl, ServerPlayer sp) {
        double y = sp.getY() + 0.05;
        long t = sl.getGameTime();
        for (int i = 0; i < 10; i++) {
            double ang = ((t * 0.4) + i * 36.0) * (Math.PI / 180.0);
            double r = 0.45 + (sl.random.nextDouble() * 0.1);
            double x = sp.getX() + Math.cos(ang) * r;
            double z = sp.getZ() + Math.sin(ang) * r;

            double vx = (sp.getX() - x) * 0.15;
            double vy = 0.05;
            double vz = (sp.getZ() - z) * 0.15;

            sl.sendParticles(ParticleTypes.ENCHANT, x, y, z, 1, vx, vy, vz, 0.0);
        }
    }

    public int getMana() { return mana; }
    public int getMaxMana() { return MAX_MANA; }
    public void setMana(int value) {
        int clamped = Math.max(0, Math.min(MAX_MANA, value));
        if (clamped != this.mana) { this.mana = clamped; sync(); }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt(TAG_MANA, this.mana);
        tag.putInt(TAG_CREEPERS, this.creepersKilled);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.mana = Math.max(0, Math.min(MAX_MANA, tag.getInt(TAG_MANA)));
        this.creepersKilled = Math.max(0, tag.getInt(TAG_CREEPERS));
    }
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public void onDataPacket(Connection connection,
                             ClientboundBlockEntityDataPacket packet,
                             HolderLookup.Provider registries) {
        super.onDataPacket(connection, packet, registries);
    }
    private void sync() {
        if (level != null && !level.isClientSide) {
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
        }
    }
    public int getCreepersKilled() {
        return creepersKilled;
    }
}
