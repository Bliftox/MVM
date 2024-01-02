package org.harbingers_of_chaos.mvb.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.entities.Role;

import static org.harbingers_of_chaos.mvb.Main.*;
import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;

public class CommandHandler extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {return;}
        String message = event.getMessage().getContentDisplay();
        User messageAuthor = event.getAuthor();

        // Получение объекта роли по её ID
        Role codderRole = event.getGuild().getRoleById("1185653602056933436");


        // Проверка, не является ли роль null (не была ли роль найдена)
        if (codderRole != null) {
            // Проверка наличия у роли конкретного права (например, ADMINISTRATOR)
            boolean hasAdminPermission = codderRole.hasPermission(Permission.ADMINISTRATOR);
            if (!hasAdminPermission) {
                LOGGER.warn("Not enough rights");
                return;}
            // Вывод результата в консоль или другую обработку
            if (messageAuthor != null && messageAuthor.getJDA().getRoles().stream().anyMatch(role -> role.getId().equals("1185653602056933436"))) {
                if (message.equalsIgnoreCase("&stop")) {
                    event.getJDA().shutdown();
                    event.getMessage().reply("Успешная остановка! 🎀").queue();
                    LOGGER.info("Command stop");

                } else if (message.equalsIgnoreCase("$buttons")) {
                    event.getMessage().reply("jepa").addActionRow(Button.primary("Hello", "Jepa")).queue();
                    event.getMessage().reply("jepa1").addActionRow(Button.primary("suggestButton", "suggestion")).queue();
                    event.getMessage().reply("cityJepa").addActionRow(Button.primary("cityButton", "City")).queue();
                    LOGGER.info("Command buttons");

                } else if (message.equalsIgnoreCase("$help")) {
                    event.getMessage().reply("```$help``` - помощь по командам." +
                            "```&stop``` - остановить бота." +
                            "\n```$buttons``` - кнопки взаимодействия." +
                            "\n```$appintreset``` - сбросить счетчки заявок.").queue();
                    LOGGER.info("Command help");

                } else if (message.equalsIgnoreCase("$appintreset")) {
                    prefs.putInt("appInt", appInt - appInt + 1);
                    event.getMessage().reply("Счетчик сброшен успешно! ☃️").queue();
                    LOGGER.info("Command appintreset");
                }

            } else {
                LOGGER.warn("Not enough role");
            }
        } else {
            LOGGER.warn("Role not found");
        }
    }
}