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
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static org.harbingers_of_chaos.mvb.Main.*;
import static org.harbingers_of_chaos.mvb.Main.log;
import static org.harbingers_of_chaos.mvb.application.ApplicationHandler.date;
import static org.harbingers_of_chaos.mvb.application.ApplicationHandler.format;

public class RejectWithReasonButton extends ListenerAdapter {
    private static Long authorId;

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponent().getLabel().equals("⚠️ Отклонить с причиной")) {
            try {
                guild = event.getGuild();
                assert guild != null;
                Role accessRole = guild.getRoleById("1160295664668913816");
                assert applicationsLogChat != null;
                assert accessRole != null;
                applicationsLogChat = jda.getTextChannelById("1189900614226944110");

                authorId = Long.parseLong(Objects.requireNonNull(event.getButton().getId())) - 1;
                log.info("Application №" + check + " rejected with reason");
                if (authorId != 0) {
                    try {

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
                    return;
                }
            } catch (Exception e) {
                log.warning("tyt " + e);
            }
        }
    }
    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        try {
            if (event.getModalId().equals("reasonModal")) {
                guild.addRoleToMember(UserSnowflake.fromId(authorId), Objects.requireNonNull(guild.getRoleById("1190023047822974996"))).queue();
                String reasonText = event.getValue("reason").getAsString();

                EmbedBuilder applicationsRejectWithReasonLog = new EmbedBuilder();
                applicationsRejectWithReasonLog.setTitle("Заявка №" + check + " от Id:" + authorId, null);
                applicationsRejectWithReasonLog.setColor(new Color(0xFAD000));
                applicationsRejectWithReasonLog.setDescription(
                        "### ⚠️ Отклонено с причиной" + "\n Причина: ```" + reasonText + "```");
                applicationsRejectWithReasonLog.setFooter("Заявка была создана в " + format.format(date) + "  \nAppID: " + authorId);

                event.reply("Заявка №" + check + " успешно отклонена! 🎄").setEphemeral(true).queue();
                event.getChannel().deleteMessageById(event.getMessage().getId()).queue();

                applicationsLogChat.sendMessage("<@" + authorId + ">").setEmbeds(applicationsRejectWithReasonLog.build()).queue();

                log.info("Application №" + check + " rejected" + " от Id:" + authorId);
            }
        } catch (Exception e) {
        log.warning("Error reject with reason application: " + e);
        event.reply("Произошла ошибка! ⛔").setEphemeral(true).queue();
        }
    }
}