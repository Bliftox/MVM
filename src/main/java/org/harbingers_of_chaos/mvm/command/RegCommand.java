package org.harbingers_of_chaos.mvm.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.harbingers_of_chaos.mvlib.SQL;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;

public class RegCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("register")
                .then(argument("password", StringArgumentType.word())
                        .then(argument("repeatPassword", StringArgumentType.word())
                                .executes(RegCommand::run)
                        )
                )
        );
        dispatcher.register(literal("reg")
                .then(argument("password", StringArgumentType.word())
                        .then(argument("repeatPassword", StringArgumentType.word())
                                .executes(RegCommand::run)
                        )
                )
        );
        dispatcher.register(literal("r")
                .then(argument("password", StringArgumentType.word())
                        .then(argument("repeatPassword", StringArgumentType.word())
                                .executes(RegCommand::run)
                        )
                )
        );
    }
    private static int run(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        assert player != null;

        if (SQL.hasPassword(player.getEntityName()))return 0;

        String password = StringArgumentType.getString(ctx, "password");
        String passwordRepeat = StringArgumentType.getString(ctx, "repeatPassword");

         if (password.equals(passwordRepeat)) {
             SQL.setPassword(player, password);
         }else throw new SimpleCommandExceptionType(new LiteralMessage("Пароли не одинаковые")).create();
        ctx.getSource().sendFeedback(() -> Text.literal("Вы вошли"), false);

        return 1;
    }

}
