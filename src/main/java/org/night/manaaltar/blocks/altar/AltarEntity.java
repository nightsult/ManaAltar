package org.night.manaaltar.blocks.altar;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.night.manaaltar.blocks.BlocksEntities;

public class AltarEntity extends BlockEntity {

    private static final int MAX_MANA = 500;
    private static final int GEN_AMOUNT = 5;
    private static final int GEN_INTERVAL_TICKS = 20;

    private int mana = 0;
    private int tickCounter = 0;

    public AltarEntity(BlockPos pos, BlockState state) {
        super(BlocksEntities.MANA_ALTAR_BE.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        super.saveAdditional(tag, lookup);
        tag.putInt("Mana", mana);
        tag.putInt("TickCounter", tickCounter);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        super.loadAdditional(tag, lookup);
        this.mana = tag.getInt("Mana");
        this.tickCounter = tag.getInt("TickCounter");
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider lookup) {
        CompoundTag tag = super.getUpdateTag(lookup);
        saveAdditional(tag, lookup);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookup) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            loadAdditional(tag, lookup);
        }
    }

    public static void tickServer(AltarEntity altar) {
        if (altar.level == null || altar.level.isClientSide()) return;

        if (altar.mana >= MAX_MANA) {
            return;
        }

        altar.tickCounter++;
        if (altar.tickCounter >= GEN_INTERVAL_TICKS) {
            altar.tickCounter = 0;
            int before = altar.mana;
            altar.mana = Math.min(MAX_MANA, altar.mana + GEN_AMOUNT);
            if (altar.mana != before) {
                altar.setChanged(); // persiste e sincroniza
            }
        }
    }

    public int getMana() {
        return mana;
    }

    public int getMaxMana() {
        return MAX_MANA;
    }
}
