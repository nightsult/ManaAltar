package org.night.manaaltar.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ManaEggFeature extends Feature<NoneFeatureConfiguration> {
    public ManaEggFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel level = ctx.level();
        RandomSource random = ctx.random();

        BlockPos origin = ctx.origin();
        int x = origin.getX();
        int z = origin.getZ();

        int topY = origin.getY();
        if (topY <= level.getMinBuildHeight() + 5) return false;
        if (!level.getBlockState(new BlockPos(x, topY - 1, z)).getFluidState().isEmpty()) return false;

        int rx = 3, rz = 3, ry = 5;
        int centerY = Math.min(topY + (ry / 2), level.getMaxBuildHeight() - 2);
        BlockPos center = new BlockPos(x, centerY, z);

        var STONE    = net.minecraft.world.level.block.Blocks.STONE.defaultBlockState();
        var COBBLE   = net.minecraft.world.level.block.Blocks.COBBLESTONE.defaultBlockState();
        var ANDESITE = net.minecraft.world.level.block.Blocks.ANDESITE.defaultBlockState();
        var IRON_ORE = net.minecraft.world.level.block.Blocks.IRON_ORE.defaultBlockState();
        var ALTAR    = org.night.manaaltar.blocks.Blocks.MANA_ALTAR.get().defaultBlockState();

        BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();

        for (int dy = -ry; dy <= ry; dy++) {
            double ny = dy / (double) ry;
            for (int dx = -rx - 1; dx <= rx + 1; dx++) {
                double nx = dx / (double) rx;
                for (int dz = -rz - 1; dz <= rz + 1; dz++) {
                    double nz = dz / (double) rz;
                    double d = nx * nx + ny * ny + nz * nz;

                    if (d <= 1.0 && d >= 0.78) {
                        p.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                        if (level.isOutsideBuildHeight(p)) continue;

                        int r = random.nextInt(10);
                        var shell = (r == 0) ? IRON_ORE : (r < 4) ? ANDESITE : (r < 7) ? COBBLE : STONE;
                        level.setBlock(p, shell, 2);
                    }
                }
            }
        }

        level.setBlock(center, ALTAR, 2);

        return true;
    }
}
