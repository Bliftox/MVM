package org.harbingers_of_chaos.mvb.suggestion;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.awt.*;

import static org.harbingers_of_chaos.mvb.Main.*;
import static org.harbingers_of_chaos.mvb.application.ApplicationHandler.date;
import static org.harbingers_of_chaos.mvb.application.ApplicationHandler.format;

public class SuggestHandler extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("suggestButton")) {
            TextInput suggestText = TextInput.create("suggestText", "[💡] Что вы хотите предложить?", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Введите ваше предложение")
                    .setMinLength(1)
                    .setMaxLength(500)
                    .setRequired(true)
                    .build();

            Modal suggestModal = Modal.create("suggestModal", "Заявка на сервер MystiVerse")
                    .addComponents(ActionRow.of(suggestText))
                    .build();

            event.replyModal(suggestModal).queue();
            log.info("Suggest");
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        try {
            if (event.getModalId().equals("suggestModal")) {
                String suggestAuthorId = event.getUser().getId();
                String suggestAuthorName = event.getUser().getName();

                String suggestText = event.getValue("suggestText").getAsString();

                TextChannel suggestChannel = jda.getTextChannelById("1190289941817733140");

                if (suggestChannel != null) {
                    log.info("Suggest added" + suggestAuthorId);
                    EmbedBuilder suggestEmbed = new EmbedBuilder();
                    suggestEmbed.setTitle("[💡] Предложение от " + suggestAuthorName);
                    suggestEmbed.setDescription("```" + suggestText + "```");
                    suggestEmbed.setColor(new Color(0x0097F5));
                    suggestEmbed.setFooter("Дата создания: " + format.format(date) + "  \nAuthorID: " + suggestAuthorId);

                    event.reply("Предложение успешно отправлено! 🎄").setEphemeral(true).queue();

                    suggestChannel.sendMessage("<@&1190022838829199382> <@" + suggestAuthorId + ">").setEmbeds(suggestEmbed.build()).queue();

                }
            }
        } catch (Exception e) {
            log.warning("Suggest exception " + e);
        }
    }
}
