package org.night.manaaltar.blocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.night.manaaltar.Manaaltar;
import org.night.manaaltar.blocks.altar.AltarEntity;
import org.night.manaaltar.blocks.neres.NeresEntity;

import java.util.function.Supplier;

public class BlocksEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Manaaltar.MOD_ID);

    public static final Supplier<BlockEntityType<AltarEntity>> MANA_ALTAR_BE =
            BLOCK_ENTITIES.register("mana_altar",
                    () -> BlockEntityType.Builder.of(
                            AltarEntity::new,
                            Blocks.MANA_ALTAR.get()
                    ).build(null)
            );
    public static final Supplier<BlockEntityType<NeresEntity>> NERES_ALTAR_BE =
            BLOCK_ENTITIES.register("neres_altar",
                    () -> BlockEntityType.Builder.of(
                            NeresEntity::new,
                            Blocks.NERES_ALTAR.get()
                    ).build(null)
            );
}
