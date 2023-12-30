package org.harbingers_of_chaos.mvb.application;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static org.harbingers_of_chaos.mvb.Main.*;

public class AcceptButton {
    public static boolean onButton(ButtonInteractionEvent event, SimpleDateFormat format, Date date) {
        try {
            Guild guild = event.getGuild();
            assert guild != null;
            TextChannel applicationsLogChat = jda.getTextChannelById("1189900614226944110");
            Role accessRole = guild.getRoleById("1160295664668913816");
            assert applicationsLogChat != null;
            assert accessRole != null;

            //получает айди кнопки которую нажали
            long authorId = Long.parseLong(Objects.requireNonNull(event.getButton().getId()));
            log.info("Application №" + check + " access");

            if (authorId != 0) {
                try {
                    guild.addRoleToMember(UserSnowflake.fromId(authorId), Objects.requireNonNull(guild.getRoleById("1160295664668913816"))).queue();

                    EmbedBuilder applicationsLog = new EmbedBuilder();
                    applicationsLog.setTitle("Заявка №" + check + " от Id:" + authorId, null);
                    applicationsLog.setColor(new Color(0x0bda51));
                    applicationsLog.setDescription("### ✅ Одобрено");
                    applicationsLog.setFooter("Заявка была создана в " + format.format(date) + "  \nAppID: " + authorId);

                    assert applicationsLogChat != null;
                    applicationsLogChat.sendMessage("<@" + authorId + ">").setEmbeds(applicationsLog.build()).queue();

                    event.getChannel().deleteMessageById(event.getMessage().getId()).queue();

                    log.info("Заявка №" + check + " от Id:" + authorId);
                } catch (Exception e) {
                    log.warning("Access application error: " + e);
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