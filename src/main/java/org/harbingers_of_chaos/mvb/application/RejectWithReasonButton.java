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
import static org.harbingers_of_chaos.mvb.Main.log;

public class RejectWithReasonButton {
    public static boolean onButton(ButtonInteractionEvent event, SimpleDateFormat format, Date date) {
        try {
            Guild guild = event.getGuild();
            assert guild != null;
            TextChannel applicationsLogChat = jda.getTextChannelById("1189900614226944110");
            Role accessRole = guild.getRoleById("1160295664668913816");
            assert applicationsLogChat != null;
            assert accessRole != null;

            long authorId = Long.parseLong(Objects.requireNonNull(event.getButton().getId()))-1;
            log.info("Application №" + check + " rejected with reason");
            if (authorId != 0) {
                try {
                    guild.addRoleToMember(UserSnowflake.fromId(authorId), Objects.requireNonNull(guild.getRoleById("1190023047822974996"))).queue();

                    EmbedBuilder applicationsLog = new EmbedBuilder();
                    applicationsLog.setTitle("Заявка №" + check + " от Id:" + authorId, null);
                    applicationsLog.setColor(new Color(0xFAD000));
                    applicationsLog.setDescription(
                            "### ⚠️ Отклонено с причиной" + "\n Причина: " );
                    applicationsLog.setFooter("Заявка была создана в " + format.format(date) + "  \nAppID: " + authorId);

                    log.info("Application №" + check + " rejected" +" от Id:" + authorId);


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
                } catch (Exception e) {
                    log.warning("Rejected with reason application error: " + e);
                }
            } else {
                log.warning("id null");
                return false;
            }
        } catch (Exception e) {
            log.warning("tyt "+ e);
        }
        return true;
    }
}