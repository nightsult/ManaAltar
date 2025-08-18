package org.night.manaaltar.blocks.neres;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.night.manaaltar.blocks.BlocksEntities;

public class NeresEntity extends BlockEntity {

    public static final int MAX_MANA = 1000;
    public static final int GEN_AMOUNT = 5;
    public static final int GEN_INTERVAL_TICKS = 20;

    private static final String TAG_MANA = "Mana";

    private int mana = 0;

    public NeresEntity(BlockPos pos, BlockState state) {
        super(BlocksEntities.NERES_ALTAR_BE.get(), pos, state);
    }

    public static void tickServer(Level level, BlockPos pos, BlockState state, NeresEntity be) {
        if (level == null || level.isClientSide) return;

        if ((level.getGameTime() % GEN_INTERVAL_TICKS) == 0L && be.mana < MAX_MANA) {
            int old = be.mana;
            be.mana = Math.min(MAX_MANA, be.mana + GEN_AMOUNT);
            if (be.mana != old) {
                be.sync();
            }
        }
    }

    public int getMana() {
        return mana;
    }

    public int getMaxMana() {
        return MAX_MANA;
    }

    public void setMana(int value) {
        int clamped = Math.max(0, Math.min(MAX_MANA, value));
        if (clamped != this.mana) {
            this.mana = clamped;
            sync();
        }
    }

    public void addMana(int delta) {
        setMana(this.mana + delta);
    }

    public void takeMana(int delta) {
        setMana(this.mana - delta);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.mana = Math.max(0, Math.min(MAX_MANA, tag.getInt(TAG_MANA)));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt(TAG_MANA, this.mana);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }

    public void load(CompoundTag tag) {
        this.mana = Math.max(0, Math.min(MAX_MANA, tag.getInt(TAG_MANA)));
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net,
                             ClientboundBlockEntityDataPacket pkt,
                             HolderLookup.Provider provider) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            this.mana = Math.max(0, Math.min(MAX_MANA, tag.getInt(TAG_MANA)));
        }
    }


    private void sync() {
        if (level != null && !level.isClientSide) {
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
        }
    }
}
