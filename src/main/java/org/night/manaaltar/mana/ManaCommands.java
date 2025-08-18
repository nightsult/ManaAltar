package org.night.manaaltar.mana;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class ManaCommands {

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("mana")
                        .requires(src -> src.hasPermission(2))

                        .then(Commands.literal("get")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(ctx -> {
                                            ServerPlayer target = EntityArgument.getPlayer(ctx, "player");
                                            return get(ctx.getSource(), target);
                                        })
                                )
                        )

                        .then(Commands.literal("give")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                .executes(ctx -> {
                                                    ServerPlayer target = EntityArgument.getPlayer(ctx, "player");
                                                    int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                                    return give(ctx.getSource(), target, amount);
                                                })
                                        )
                                )
                        )

                        .then(Commands.literal("set")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0, Data.MAX_MANA))
                                                .executes(ctx -> {
                                                    ServerPlayer target = EntityArgument.getPlayer(ctx, "player");
                                                    int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                                    return set(ctx.getSource(), target, amount);
                                                })
                                        )
                                )
                        )

                        .then(Commands.literal("take")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                .executes(ctx -> {
                                                    ServerPlayer target = EntityArgument.getPlayer(ctx, "player");
                                                    int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                                    return take(ctx.getSource(), target, amount);
                                                })
                                        )
                                )
                        )
        );
    }

    private int get(CommandSourceStack src, ServerPlayer target) {
        Data.ensureInitialized(target);
        int mana = Data.getMana(target);
        src.sendSuccess(
                () -> Component.literal("§a[MANA] §7Jogador §b" + target.getGameProfile().getName()
                        + "§7 possui §e" + mana + "§7/§e" + Data.MAX_MANA + "§7 de mana."), false);
        target.displayClientMessage(
                Component.literal("§aSua mana atual: §e" + mana + "§a/§e" + Data.MAX_MANA), false);
        return Command.SINGLE_SUCCESS;
    }

    private int give(CommandSourceStack src, ServerPlayer target, int amount) {
        Data.ensureInitialized(target);
        int before = Data.getMana(target);
        Data.addMana(target, amount);
        int after = Data.getMana(target);

        src.sendSuccess(() -> Component.literal("§a[MANA] §7Adicionado §a+" + (after - before)
                + "§7 de mana ao jogador §b" + target.getGameProfile().getName()
                + "§7 (agora: §e" + after + "§7/§e" + Data.MAX_MANA + "§7)."), true);

        target.displayClientMessage(Component.literal("§aVocê recebeu §a+" + (after - before)
                + "§a de mana. Atual: §e" + after + "§a/§e" + Data.MAX_MANA), false);
        return Command.SINGLE_SUCCESS;
    }

    private int set(CommandSourceStack src, ServerPlayer target, int amount) {
        Data.ensureInitialized(target);
        Data.setMana(target, amount);
        int after = Data.getMana(target);

        src.sendSuccess(() -> Component.literal("§a[MANA] §7Setado mana do jogador §b"
                + target.getGameProfile().getName() + "§7 para §e" + after
                + "§7/§e" + Data.MAX_MANA + "§7."), true);

        target.displayClientMessage(Component.literal("§aSua mana foi definida para §e"
                + after + "§a/§e" + Data.MAX_MANA), false);
        return Command.SINGLE_SUCCESS;
    }

    private int take(CommandSourceStack src, ServerPlayer target, int amount) {
        Data.ensureInitialized(target);
        int before = Data.getMana(target);
        int desired = Math.max(0, before - amount);
        Data.setMana(target, desired);
        int removed = before - desired;

        src.sendSuccess(() -> Component.literal("§a[MANA] §7Removido §c-" + removed
                + "§7 de mana do jogador §b" + target.getGameProfile().getName()
                + "§7 (agora: §e" + desired + "§7/§e" + Data.MAX_MANA + "§7)."), true);

        target.displayClientMessage(Component.literal("§cVocê perdeu §c-" + removed
                + "§c de mana. Atual: §e" + desired + "§c/§e" + Data.MAX_MANA), false);
        return Command.SINGLE_SUCCESS;
    }
}