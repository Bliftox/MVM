package org.harbingers_of_chaos.mvb.application;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.harbingers_of_chaos.mvb.Discord;

import java.awt.*;
import java.util.Objects;


import org.harbingers_of_chaos.mvb.SQLite;
import org.harbingers_of_chaos.mvm.Config;
import static org.harbingers_of_chaos.mvb.application.ApplicationHandler.date;
import static org.harbingers_of_chaos.mvb.application.ApplicationHandler.format;
import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;

public class RejectButton {
    private static TextChannel applicationsLogChat;
    private static Guild guild;
    public static boolean onButton(ButtonInteractionEvent event) {
        try {
            guild = event.getGuild();
            assert guild != null;
            Role accessRole = guild.getRoleById("1160295664668913816");
            applicationsLogChat = Discord.getJda().getTextChannelById(Config.INSTANCE.discord.LogChat);
            assert applicationsLogChat != null;
            assert accessRole != null;

            int appInt = Integer.parseInt(Objects.requireNonNull(event.getButton().getId()))-1;
            long authorId = SQLite.getApplicationUser_id(appInt);
            LOGGER.info("Application rejected" +" от Id:" + authorId);
            if (authorId != 0) {
                try {
                    guild.addRoleToMember(UserSnowflake.fromId(authorId), Objects.requireNonNull(guild.getRoleById("1190023047822974996"))).queue();
                    guild.removeRoleFromMember(UserSnowflake.fromId(authorId), Objects.requireNonNull(guild.getRoleById("1190724252966596701"))).queue();

                    EmbedBuilder applicationsRejectLog = new EmbedBuilder();
                    applicationsRejectLog.setTitle("Заявка от Id:" + authorId, null);
                    applicationsRejectLog.setColor(new Color(0xDA0000));
                    applicationsRejectLog.setDescription("### 🛑 Отклонено - <@" + event.getUser().getId() + ">");
                    applicationsRejectLog.setFooter("Заявка была создана в " + format.format(date) + "  \nAppID: " + authorId);

                    assert applicationsLogChat != null;
                    applicationsLogChat.sendMessage("<@" + authorId + ">").setEmbeds(applicationsRejectLog.build()).queue();

                    event.getChannel().deleteMessageById(event.getMessage().getId()).queue();

                    LOGGER.info("Заявка от Id:" + authorId);
                } catch (Exception e) {
                    LOGGER.warn("Rejected application error: " + e);
                }
            } else {
                LOGGER.warn("id null");
                return false;
            }

        } catch (Exception e) {
            LOGGER.warn("Application warning: " + e);
            return false;
        }
        return true;
    }
}