package org.harbingers_of_chaos.mvm.listeners;

import org.harbingers_of_chaos.mvb.Discord;
import org.harbingers_of_chaos.mvlib.AccountLinking;
import org.harbingers_of_chaos.mvlib.mySQL;
import org.harbingers_of_chaos.mvm.event.PlayerConnectedCallback;

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
    }
}
