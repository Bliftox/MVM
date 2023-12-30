package org.harbingers_of_chaos.mvb.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.entities.Role;

import static org.harbingers_of_chaos.mvb.Main.*;

public class CommandHandler extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {return;}
        String message = event.getMessage().getContentDisplay();

        // Получение объекта роли по её ID
        Role role = event.getGuild().getRoleById("1185653602056933436");

        // Проверка, не является ли роль null (не была ли роль найдена)
        if (role != null) {
            // Проверка наличия у роли конкретного права (например, ADMINISTRATOR)
            boolean hasAdminPermission = role.hasPermission(Permission.ADMINISTRATOR);

            // Вывод результата в консоль или другую обработку
            if (hasAdminPermission) {
                if (message.equalsIgnoreCase("&stop")) {
                    event.getJDA().shutdown();
                    event.getMessage().reply("Успешная остановка! 🎄").queue();

                } else if (message.equalsIgnoreCase("$buttons")) {
                    event.getMessage().reply("jepa").addActionRow(Button.primary("Hello", "Jepa")).queue();
                    event.getMessage().reply("jepa1").addActionRow(Button.primary("suggestButton", "suggestion")).queue();
                    log.info("Command");
                } else if (message.equalsIgnoreCase("$help")) {
                    event.getMessage().reply("```&stop``` - остановить бота." +
                            "\n```$buttons``` - кнопки взаимодействия.").queue();

                }

            } else {
                log.warning("Not enough rights");
            }
        } else {
            log.warning("Role not found");
        }
    }
}
