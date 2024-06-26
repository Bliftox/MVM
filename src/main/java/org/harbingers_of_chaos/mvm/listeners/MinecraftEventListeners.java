package org.harbingers_of_chaos.mvm.listeners;

//import org.harbingers_of_chaos.mvb.Bot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.harbingers_of_chaos.mvlib.MySQL;
import org.harbingers_of_chaos.mvm.event.PlayerConnectedCallback;

import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;
import static org.harbingers_of_chaos.mvm.MystiVerseModServer.ACCOUNT_LINKING;


public class  MinecraftEventListeners {


    public static void init(/*AccountLinking accountLinking*/) {
        PlayerConnectedCallback.EVENT.register((player, server) -> {
            LOGGER.info("connect:"+ MySQL.hasPlayerNick(player.getName().getString()));
            LOGGER.info("connect:"+ MySQL.hasPlayerIp(player.getIp()));

            if(!MySQL.hasPlayerNick(player.getName().getString()))                                  kickForRegistrationAccount(player);
            if(!MySQL.hasPlayerIp(player.getIp()))                                                  kickForUnlinkedAccount(player);
//            if(!MySQL.getPlayerNickname2Ip(player.getIp()).equals(player.getName().getString()))    kickBecauseRepeatedIp(player);

        });
    }
    private static void kickBecauseRepeatedIp(ServerPlayerEntity player){
        LOGGER.info("Повторный IP сообщите администрации сервера");
        MutableText reason = Text.empty()
                .append(Text.literal("Повторный IP сообщите администрации сервера\n"));

        player.networkHandler.disconnect(reason);
    }
    private static void kickForRegistrationAccount(ServerPlayerEntity player){
        LOGGER.info("Создайте заявку в дискорд сервере!\n");
        MutableText reason = Text.empty()
                .append(Text.literal("Создайте заявку в дискорд сервере!\n"))
                .append(Text.literal("И ожидайте одобрение администрацией сервера.\n"));
        player.networkHandler.disconnect(reason);
    }
    public static void kickForUnlinkedAccount(ServerPlayerEntity player){
        String ip = player.getIp();
        String id = MySQL.getPlayerId2Nickname(player.getName().getString());
        ACCOUNT_LINKING.tryQueueForLinking(ip,id);
        String code = ACCOUNT_LINKING.getCode(ip);
        LOGGER.info("Ваш код авторизации"+code );

        MutableText reason = Text.empty()
                .append(Text.literal("Ваш код авторизации "))
                .append(Text.literal(code)
                        .formatted(Formatting.BLUE, Formatting.UNDERLINE))
                .append(Text.literal("\nОтправте его в лс боту Chorny"));

        player.networkHandler.disconnect(reason);
    }
}
