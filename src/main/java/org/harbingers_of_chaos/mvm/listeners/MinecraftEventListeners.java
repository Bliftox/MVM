package org.harbingers_of_chaos.mvm.listeners;

//import org.harbingers_of_chaos.mvb.Discord;
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

//            if (!mySQL.hasPlayerIp(player.getIp())) {
//                Discord.kickForUnlinkedAccount(player);
//                return;
//            }else if (mySQL.getPlayerIp(player.getIp()) != player.getName().getString()){
//                Discord.kickForUnlinkedAccount(player);
//                return;
//            }
        });
    }
}
