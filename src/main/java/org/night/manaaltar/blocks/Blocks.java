package org.night.manaaltar.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.night.manaaltar.Manaaltar;
import org.night.manaaltar.blocks.altar.Altar;
import org.night.manaaltar.blocks.neres.Neres;

public class Blocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Manaaltar.MOD_ID);

    public static final DeferredBlock<Altar> MANA_ALTAR =
            BLOCKS.register("mana_altar",
                    () -> new Altar(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .strength(3.5f)
                            .requiresCorrectToolForDrops()
                            .pushReaction(PushReaction.NORMAL)
                    )
            );

    public static final DeferredBlock<Neres> NERES_ALTAR =
            BLOCKS.register("neres_altar",
                    () -> new Neres(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .strength(5.0f)
                            .lightLevel(state -> 7)
                            .requiresCorrectToolForDrops()
                            .pushReaction(PushReaction.BLOCK)
                    )
            );
}
