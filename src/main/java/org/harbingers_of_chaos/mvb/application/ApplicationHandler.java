package org.harbingers_of_chaos.mvb.application;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.harbingers_of_chaos.mvb.Discord;
import org.harbingers_of_chaos.mvlib.mySQL;
import org.harbingers_of_chaos.mvlib.Config;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;

public class ApplicationHandler extends ListenerAdapter {
    private static String reason;
    private static String userName;
    public static String userId;
    public static SimpleDateFormat format = new SimpleDateFormat(" HH:mm  dd/MM/yyyy");
    public static Date date = new Date();
    private TextChannel applicationsChat;
    public static String nickname;
    private static int years;
    private static String sex;
    private static String bio;
    private static String whyWe;
    private static Guild guild;
    private static int appInt;

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        try {
            if (event.getModalId().equals("application")) {
                guild = event.getGuild();
                nickname = Objects.requireNonNull(event.getValue("nickname")).getAsString();
                years = Integer.parseInt(Objects.requireNonNull(event.getValue("years")).getAsString());
                sex = Objects.requireNonNull(event.getValue("sex")).getAsString();
                bio = Objects.requireNonNull(event.getValue("bio")).getAsString();
                whyWe = Objects.requireNonNull(event.getValue("whyWe")).getAsString();
                appInt = Config.INSTANCE.discord.appInt;

                userName = event.getUser().getName();
                userId = event.getUser().getId();

                applicationsChat = Discord.getJda().getTextChannelById("1189996402164629575");

                Config.INSTANCE.discord.appInt++;

                if (applicationsChat != null) {
                    guild.addRoleToMember(UserSnowflake.fromId(userId), Objects.requireNonNull(guild.getRoleById("1190724252966596701"))).queue();

                    EmbedBuilder applicationEmbed = new EmbedBuilder();
                    applicationEmbed.setImage("https://cdn.discordapp.com/attachments/991951225962639524/1189687904415535195/-27-12-2023_8.png?ex=659f1233&is=658c9d33&hm=7b814ffffdfae944045e1df19636c44494fa4f380c9228cd1090a8dd9c0dc8b8&");
                    applicationEmbed.setTitle("Заявка №" + appInt + " от " + userName, null);
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
                            Button.success(String.valueOf(appInt), "✅ Принять"),
                            Button.secondary(String.valueOf(appInt+1), "⚠️ Отклонить с причиной"),
                            Button.danger(String.valueOf(appInt+2), "🛑 Отклонить")
                    ).queue();
                    mySQL.addApplication(appInt,userName, userId,nickname,years,sex,bio,whyWe);
                    LOGGER.info("Application №" + appInt + " created access");
                } else {
                    LOGGER.warn("Failed to receive text channel");
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Error application: " + e);
            event.reply("Произошла ошибка! ⛔").setEphemeral(true).queue();
        }
    }

    private static boolean hasRole(Member member, String roleName) {
        try {
            for (Role role : member.getRoles()) {
                if (role.getName().equalsIgnoreCase(roleName)) {
                    return true; // Роль найдена
                }
            }
            return false; // Роль не найдена
        } catch (Exception e) {LOGGER.warn("Role error: " + e);}
        return false;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        /*                       Модальное окно                           */
        try {
            if (event.getComponentId().equals("Hello")) {
                HelloButton.onButton(event);
            }
            if (event.getComponent().getLabel().equals("✅ Принять")) {
                AcceptButton.onButton(event, format, date);
            }
            if (event.getComponent().getLabel().equals("🛑 Отклонить")) {
                RejectButton.onButton(event);
            }
        } catch (Exception e) {LOGGER.warn("Modal error: " + e);}
    }
}


