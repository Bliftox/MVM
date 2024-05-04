package org.harbingers_of_chaos.mvb.linkingCode;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.harbingers_of_chaos.mvlib.AccountLinking;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;
import static org.harbingers_of_chaos.mvm.MystiVerseModServer.ACCOUNT_LINKING;

public class CodeListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User user = event.getAuthor();
        Message message = event.getMessage();

        if (user.isBot()) {return;}

        if (message.getChannel() instanceof PrivateChannel channel) {
            String code = message.getContentRaw();
            LOGGER.info(code);

            switch (ACCOUNT_LINKING.tryLinkAccount(code, user.getId())) {
                case INVALID_CODE ->
                        channel
                                .sendMessage(MessageCreateData.fromContent("Invalid linking code!"))
                                .queue();
                case ACCOUNT_LINKED ->
                        channel
                                .sendMessage(MessageCreateData.fromContent("Your account was already linked!"))
                                .queue();
                case SUCCESS ->
                        channel
                                .sendMessage(MessageCreateData.fromContent("Your account was successfully linked!"))
                                .queue();
            }
        }
    }
}