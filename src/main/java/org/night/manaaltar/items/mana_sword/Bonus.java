package org.night.manaaltar.items.mana_sword;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.night.manaaltar.Manaaltar;
import org.night.manaaltar.mana.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Bonus {

    private static final ResourceLocation BONUS_ID =
            ResourceLocation.fromNamespaceAndPath(Manaaltar.MOD_ID, "mana_sword_plus5");

    private static final double EXTRA_DAMAGE = 5.0;
    private static final Set<UUID> APPLIED_THIS_TICK = new HashSet<>();

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        int mana = Data.getMana(player);
        if (mana < 10) {
            event.setCanceled(true);
            player.displayClientMessage(
                    Component.literal("Você está exausto, sem mana para atacar!"),
                    true
            );
            return;
        }
        if (player.level().isClientSide()) return;
        if (!(event.getTarget() instanceof LivingEntity)) return;

        if (!(player.getMainHandItem().getItem() instanceof Sword)) return;
        if (Data.getMana(player) < Data.MAX_MANA / 2) return;

        AttributeInstance atk = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if (atk == null) return;

        atk.removeModifier(BONUS_ID);
        AttributeModifier mod = new AttributeModifier(
                BONUS_ID,
                EXTRA_DAMAGE,
                AttributeModifier.Operation.ADD_VALUE
        );
        atk.addTransientModifier(mod);
        APPLIED_THIS_TICK.add(player.getUUID());
    }

    @SubscribeEvent
    public void onPlayerTickEnd(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.level().isClientSide()) return;

        if (!APPLIED_THIS_TICK.remove(player.getUUID())) return;
        AttributeInstance atk = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if (atk != null) {
            atk.removeModifier(BONUS_ID);
        }
    }
}
