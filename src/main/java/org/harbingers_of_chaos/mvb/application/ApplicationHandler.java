package org.harbingers_of_chaos.mvb.application;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static org.harbingers_of_chaos.mvb.Main.*;

public class ApplicationHandler extends ListenerAdapter {
    private static String reason;
    public static SimpleDateFormat format = new SimpleDateFormat(" HH:mm  dd/MM/yyyy");
    public static Date date = new Date();

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        try {
            if (event.getModalId().equals("application")) {
                String nickname = Objects.requireNonNull(event.getValue("nickname")).getAsString();
                String years = Objects.requireNonNull(event.getValue("years")).getAsString();
                String sex = Objects.requireNonNull(event.getValue("sex")).getAsString();
                String bio = Objects.requireNonNull(event.getValue("bio")).getAsString();
                String whyWe = Objects.requireNonNull(event.getValue("whyWe")).getAsString();
                appInt = prefs.getInt("appInt", 0);

                String user = event.getUser().getName();
                long userId = event.getUser().getIdLong();

                applicationsChat = jda.getTextChannelById("1189996402164629575");

                if (applicationsChat != null) {
                    EmbedBuilder applicationEmbed = new EmbedBuilder();
                    applicationEmbed.setImage("https://cdn.discordapp.com/attachments/991951225962639524/1189687904415535195/-27-12-2023_8.png?ex=659f1233&is=658c9d33&hm=7b814ffffdfae944045e1df19636c44494fa4f380c9228cd1090a8dd9c0dc8b8&");
                    applicationEmbed.setTitle("Заявка №" + appInt + " от " + user, null);
                    applicationEmbed.setColor(new Color(0xCB3966));
                    applicationEmbed.setDescription(
                            "### 🎀 Ник в игре: \n```" + nickname +
                                    "```\n### 🧭 Сколько вам лет? \n```" + years +
                                    "```\n### 🎨 Ваш пол: \n```" + sex +
                                    "```\n### 📕 Роскажите о себе: \n```" + bio +
                                    "```\n### 💚 Почему именно наш сервер? \n```" + whyWe + "```");
                    applicationEmbed.setFooter("Заявка была создана в " + format.format(date) + "  \nAppID: " + userId);

                    event.reply("Заявка №" + appInt + " успешно отправлена! 🎄").setEphemeral(true).queue();

                    applicationsChat.sendMessage("<@&1189667213318295606>" + " <@" + userId + ">").setEmbeds(applicationEmbed.build()).addActionRow(
                            Button.success(String.valueOf(userId), "✅ Принять"),
                            Button.secondary(String.valueOf(userId + 1), "⚠️ Отклонить с причиной"),
                            Button.danger(String.valueOf(userId + 2), "🛑 Отклонить")
                    ).queue();

                    log.info("Application №" + appInt + " created access");
                } else {
                    log.warning("Failed to receive text channel");
                }
                prefs.putInt("appInt", appInt + 1);
            }
        } catch (Exception e) {
            log.warning("Error application: " + e);
            event.reply("Произошла ошибка! ⛔").setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        /*                       Модальное окно                           */
        if (event.getComponentId().equals("Hello")) {
            HelloButton.onButton(event);
        }
        if (event.getComponent().getLabel().equals("✅ Принять")) {
            AcceptButton.onButton(event, format, date);
        }

        if (event.getComponent().getLabel().equals("🛑 Отклонить")) {
            RejectButton.onButton(event);
        }
    }
}


