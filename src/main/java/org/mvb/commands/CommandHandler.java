package org.mvb.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import static org.mvb.Main.log;

public class CommandHandler extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {return;}
        String message = event.getMessage().getContentDisplay();

        if (message.equalsIgnoreCase("$привет")) {
            event.getMessage().reply("jepa").addActionRow(Button.primary("Hello", "Jepa")).queue();
            event.getMessage().reply("jepa1").addActionRow(Button.primary("suggestButton", "suggestion")).queue();
            log.info("Command");

        }
    }


}
