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

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.harbingers_of_chaos.mvb.Main.*;

public class ApplicationHandler extends ListenerAdapter {
    private static String userId;
    private static String user;
    private static String reason;
    private static TextChannel applicationsChat;
    private static TextChannel applicationsLogChat;
    public static SimpleDateFormat format = new SimpleDateFormat(" HH:mm  dd/MM/yyyy");
    public static Date date = new Date();

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        try {
            if (event.getModalId().equals("application")) {
                String nickname = event.getValue("nickname").getAsString();
                String years = event.getValue("years").getAsString();
                String sex = event.getValue("sex").getAsString();
                String bio = event.getValue("bio").getAsString();
                String whyWe = event.getValue("whyWe").getAsString();
                applicationsChat = jda.getTextChannelById("1189996402164629575");

                user = event.getUser().getName();
                userId = String.valueOf(event.getUser().getIdLong());

                if (applicationsChat != null) {
                    EmbedBuilder applicationEmbed = new EmbedBuilder();
                    applicationEmbed.setImage("https://cdn.discordapp.com/attachments/991951225962639524/1189687904415535195/-27-12-2023_8.png?ex=659f1233&is=658c9d33&hm=7b814ffffdfae944045e1df19636c44494fa4f380c9228cd1090a8dd9c0dc8b8&");
                    applicationEmbed.setTitle("Заявка №" + check + " от " + user, null);
                    applicationEmbed.setColor(new Color(0xCB3966));
                    applicationEmbed.setDescription(
                            "### 🎀 Ник в игре: \n```" + nickname +
                                    "```\n### 🧭 Сколько вам лет? \n```" + years +
                                    "```\n### 🎨 Ваш пол: \n```" + sex +
                                    "```\n### 📕 Роскажите о себе: \n```" + bio +
                                    "```\n### 💚 Почему именно наш сервер? \n```" + whyWe + "```");
                    applicationEmbed.setFooter("Заявка была создана в " + format.format(date) + "  \nAppID: " + userId);

                    event.reply("Заявка №" + check + " успешно отправлена! 🎄").setEphemeral(true).queue();

                    applicationsChat.sendMessage("<@&1189667213318295606>" + " <@" + userId + ">").setEmbeds(applicationEmbed.build()).addActionRow(
                            Button.success(userId, "✅ Принять"),
                            Button.secondary(userId + 1, "⚠️ Отклонить с причиной"),
                            Button.danger(userId + 2, "🛑 Отклонить")
                    ).queue();


                    log.info("Application №" + check + " created access");
                } else {
                    log.warning("Не удалось получить текстовый канал.");
                }
                check++;

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

            TextInput nickname = TextInput.create("nickname", "[🎀] Ник в игре:", TextInputStyle.SHORT)
                    .setPlaceholder("Введите ваш ник в игре")
                    .setMinLength(3)
                    .setMaxLength(15)
                    .build();

            TextInput years = TextInput.create("years", "[🧭] Сколько вам лет?", TextInputStyle.SHORT)
                    .setPlaceholder("Введите свой настоящий возраст")
                    .setMinLength(1)
                    .setMaxLength(2)
                    .setRequired(true)
                    .build();

            TextInput sex = TextInput.create("sex", "[🎨] Ваш пол:", TextInputStyle.SHORT)
                    .setPlaceholder("Введите ваш пол")
                    .setMinLength(1)
                    .setMaxLength(10)
                    .setRequired(true)
                    .build();

            TextInput bio = TextInput.create("bio", "[📕] Роскажите о себе:", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Поделитесь немного о себе: чем занимаетесь, какие у вас увлечения и интересы?")
                    .setMinLength(1)
                    .setMaxLength(200)
                    .setRequired(true)
                    .build();

            TextInput whyWe = TextInput.create("whyWe", "[💚] Почему именно наш сервер?", TextInputStyle.SHORT)
                    .setPlaceholder("Чем вам приглянулся наш сервер?")
                    .setMinLength(1)
                    .setMaxLength(100)
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("application", "Заявка на сервер MystiVerse")
                    .addComponents(ActionRow.of(nickname), ActionRow.of(years), ActionRow.of(sex), ActionRow.of(bio), ActionRow.of(whyWe))
                    .build();

            event.replyModal(modal).queue();

        }
        Guild guild = event.getGuild();

        Role accessRole = guild.getRoleById("1160295664668913816");
        Role rejectedRole = guild.getRoleById("1190023047822974996");

        applicationsLogChat = jda.getTextChannelById("1189900614226944110");

        try {
            String authorId = event.getButton().getId();
            
            if (event.getComponent().getLabel().equals("⚠️ Отклонить с причиной")) {
                log.info("Application №" + check + " rejected with reason");
                if (authorId != null) {
                    try {
                        guild.addRoleToMember(UserSnowflake.fromId(authorId), rejectedRole).queue();

                        EmbedBuilder applicationsLog = new EmbedBuilder();
                        applicationsLog.setTitle("Заявка №" + check + " от Id:" + authorId, null);
                        applicationsLog.setColor(new Color(0xFAD000));
                        applicationsLog.setDescription(
                                "### ⚠️ Отклонено с причиной" + "\n Причина: " + reason);
                        applicationsLog.setFooter("Заявка была создана в " + format.format(date) + "  \nAppID: " + authorId);

                        log.info(event.getMessage().getId());


                        TextInput reason = TextInput.create("reason", "[🃏] Причина:", TextInputStyle.PARAGRAPH)
                                .setPlaceholder("Введите причину отклонения анкеты")
                                .setMinLength(1)
                                .setMaxLength(250)
                                .setRequired(true)
                                .build();
                        Modal reasonModal = Modal.create("reasonModal", "Причина отклонения анкеты")
                                .addComponents(ActionRow.of(reason))
                                .build();
                        event.replyModal(reasonModal).queue();

                        if (reason != null) {
                            event.reply("Заявка №" + check + " успешно отправлена! 🎄").setEphemeral(true).queue();
                            log.info("deleete");
                            applicationsLogChat.sendMessage("<@" + authorId + ">").setEmbeds(applicationsLog.build()).queue();
                        } else {
                            return;
                        }

                    } catch (Exception e) {
                        log.warning("Rejected with reason application error: " + e);
                    }
                } else {
                    log.warning("id null");
                    return;
                }
            }
        } catch (Exception e) {
        log.warning("tyt "+ e);
        }


        try {
            //получает айди кнопки которую нажали

            
            if (event.getComponent().getLabel().equals("✅ Принять")) {
                Long authorId = Long.valueOf(event.getButton().getId());
                log.info("Application №" + check + " access");
                if (authorId != null) {
                    try {
                        guild.addRoleToMember(UserSnowflake.fromId(authorId), accessRole).queue();

                        EmbedBuilder applicationsLog = new EmbedBuilder();
                        applicationsLog.setTitle("Заявка №" + check + " от Id:" + authorId, null);
                        applicationsLog.setColor(new Color(0x0bda51));
                        applicationsLog.setDescription(
                                "### ✅ Одобрено");
                        applicationsLog.setFooter("Заявка была создана в " + format.format(date) + "  \nAppID: " + authorId);

                        applicationsLogChat.sendMessage("<@" + authorId + ">").setEmbeds(applicationsLog.build()).queue();

                        event.getChannel().deleteMessageById(event.getMessage().getId()).queue();

                        log.info(event.getMessage().getId());
                    } catch (Exception e) {
                        log.warning("Access application error: " + e);
                    }
                } else {
                    log.warning("id null");
                    return;
                }
            }
        } catch (Exception e) {
            log.warning("Application warning: " + e);
            return;
        }
    }
}


