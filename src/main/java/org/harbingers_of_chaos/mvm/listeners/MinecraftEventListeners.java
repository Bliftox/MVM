package org.harbingers_of_chaos.mvm.listeners;

import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.harbingers_of_chaos.mvb.Discord;
import org.harbingers_of_chaos.mvlib.AccountLinking;
import org.harbingers_of_chaos.mvlib.mySQL;
import org.harbingers_of_chaos.mvm.command.Start;
import org.harbingers_of_chaos.mvm.event.PlayerConnectedCallback;

import java.sql.SQLException;

import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;


public final class MinecraftEventListeners {
    private MinecraftEventListeners() {}

    public static void init(AccountLinking accountLinking) {
        PlayerConnectedCallback.EVENT.register((player, server) -> {
            LOGGER.info("connect:"+ mySQL.hasPlayerNick(player.getName().getString()));
            LOGGER.info("connect:"+ mySQL.hasPlayerIp(player.getIp()));
            if(!mySQL.hasPlayerNick(player.getName().getString())) {
                Discord.kickForApp(player);
                return;
            }else if (!mySQL.hasPlayerIp(player.getIp())) {
                LOGGER.info(player.getIp());
                LOGGER.info("s");
                Discord.kickForUnlinkedAccount(player);
                return;
            }else if (!mySQL.getPlayerNickToIP(player.getName().getString()).equals(player.getIp())){
                LOGGER.info(player.getIp());
                LOGGER.info(mySQL.getPlayerNickToIP(player.getName().getString()));
                LOGGER.info("a");
                Discord.kickForUnlinkedAccount(player);
                    return;
            }
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            LiteralCommandNode<ServerCommandSource> startNode = CommandManager
                    .literal("start")
                    .requires(Permissions.require("mvs.command.root", 2))
                    .then((CommandManager.argument("target", GameProfileArgumentType.gameProfile())
                            .then((CommandManager.argument("target1", GameProfileArgumentType.gameProfile())
                                    .then((CommandManager.argument("target2", GameProfileArgumentType.gameProfile())
                                            .then((CommandManager.argument("target3", GameProfileArgumentType.gameProfile())
                                                    .executes(Start::start)))))))))
                    .build();

            LiteralCommandNode<ServerCommandSource> sendNode = CommandManager
                    .literal("send")
                    .requires(Permissions.require("mvs.command.inv", 2))
                    .then(CommandManager.argument("target", GameProfileArgumentType.gameProfile())
                            .executes(Start::send))
                    .build();

            LiteralCommandNode<ServerCommandSource> invNode = CommandManager
                    .literal("inv")
                    .requires(Permissions.require("mvs.command.inv", 2))
                    .then(CommandManager.argument("target", GameProfileArgumentType.gameProfile())
                            .executes(Start::inv))
                    .build();

            LiteralCommandNode<ServerCommandSource> echestNode = CommandManager
                    .literal("echest")
                    .requires(Permissions.require("mvs.command.echest", 2))
                    .then(CommandManager.argument("target", GameProfileArgumentType.gameProfile())
                            .executes(Start::eChest))
                    .build();


            dispatcher.getRoot().addChild(startNode);
            startNode.addChild(invNode);
            startNode.addChild(echestNode);
            startNode.addChild(sendNode);

        });
    }
}
