package org.night.manaaltar.blocks.altar;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class Altar extends Block {

    public Altar(Properties props) {
        super(props);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (!stack.is(Items.NETHER_STAR)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        player.swing(hand);

        if (!level.isClientSide) {
            level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0f, 1.0f);

            if (level instanceof ServerLevel serverLevel) {
                spawnActivationParticles(serverLevel, pos);
            }

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            if (player instanceof net.minecraft.server.level.ServerPlayer sp) {
                org.night.manaaltar.network.ScreenShake.trigger(sp, 30, 1.0f);
            }

            level.setBlockAndUpdate(pos, org.night.manaaltar.blocks.Blocks.NERES_ALTAR.get().defaultBlockState());
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    private void spawnActivationParticles(ServerLevel level, BlockPos pos) {
        RandomSource random = level.getRandom();
        Vec3 center = Vec3.atCenterOf(pos);

        for (int i = 0; i < 150; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 5.0;
            double offsetY = (random.nextDouble() - 0.5) * 5.0;
            double offsetZ = (random.nextDouble() - 0.5) * 5.0;
            Vec3 startPos = center.add(offsetX, offsetY, offsetZ);

            Vec3 velocity = center.subtract(startPos).normalize().scale(0.15);

            level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                    startPos.x(), startPos.y(), startPos.z(),
                    1,
                    velocity.x(), velocity.y(), velocity.z(),
                    0.1);
        }
    }
}