package org.harbingers_of_chaos.mvb.application;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.awt.*;
import java.util.Objects;

import static org.harbingers_of_chaos.mvb.Main.*;
import static org.harbingers_of_chaos.mvb.Main.log;
import static org.harbingers_of_chaos.mvb.application.ApplicationHandler.date;
import static org.harbingers_of_chaos.mvb.application.ApplicationHandler.format;

public class RejectButton {
    public static boolean onButton(ButtonInteractionEvent event) {
        try {
            guild = event.getGuild();
            assert guild != null;
            Role accessRole = guild.getRoleById("1160295664668913816");
            assert applicationsLogChat != null;
            assert accessRole != null;
            applicationsLogChat = jda.getTextChannelById("1189900614226944110");

            long authorId = Long.parseLong(Objects.requireNonNull(event.getButton().getId()))-2;
            log.info("Application rejected" +" от Id:" + authorId);
            if (authorId != 0) {
                try {
                    guild.addRoleToMember(UserSnowflake.fromId(authorId), Objects.requireNonNull(guild.getRoleById("1190023047822974996"))).queue();

                    EmbedBuilder applicationsRejectLog = new EmbedBuilder();
                    applicationsRejectLog.setTitle("Заявка от Id:" + authorId, null);
                    applicationsRejectLog.setColor(new Color(0xDA0000));
                    applicationsRejectLog.setDescription("### 🛑 Отклонено");
                    applicationsRejectLog.setFooter("Заявка была создана в " + format.format(date) + "  \nAppID: " + authorId);

                    assert applicationsLogChat != null;
                    applicationsLogChat.sendMessage("<@" + authorId + ">").setEmbeds(applicationsRejectLog.build()).queue();

                    event.getChannel().deleteMessageById(event.getMessage().getId()).queue();

                    log.info("Заявка от Id:" + authorId);
                } catch (Exception e) {
                    log.warning("Rejected application error: " + e);
                }
            } else {
                log.warning("id null");
                return false;
            }

        } catch (Exception e) {
            log.warning("Application warning: " + e);
            return false;
        }
        return true;
    }
}