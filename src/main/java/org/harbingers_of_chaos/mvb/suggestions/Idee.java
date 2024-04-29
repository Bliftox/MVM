package org.harbingers_of_chaos.mvb.suggestions;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

import java.awt.*;
import java.time.Instant;

public class Idee extends ListenerAdapter {

        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (!event.getName().equals("идея")) return;

                TextInput title = TextInput.create("ideeTtile", "Заголовок", TextInputStyle.SHORT).setRequired(true).setMaxLength(100).build();
                TextInput desc = TextInput.create("ideeDesc", "Ваша идея", TextInputStyle.PARAGRAPH).setRequired(true).setMaxLength(500).build();
                TextInput image = TextInput.create("ideeImage", "Илюстрация", TextInputStyle.SHORT).setRequired(false).setPlaceholder("Не обязательно").build();

                Modal.Builder modal = Modal.create("ideeModal", "Идея").addActionRows(ActionRow.of(title), ActionRow.of(desc), ActionRow.of(image));

                event.replyModal(modal.build()).queue();
        }

        @Override
        public void onModalInteraction(ModalInteractionEvent event) {
                if (!event.getModalId().equals("ideeModal")) return;

                TextChannel channel = event.getGuild().getTextChannelById("1233384825524719656");

                EmbedBuilder embed = new EmbedBuilder();

                String title = event.getValue("ideeTtile").getAsString();
                embed.setTitle(title);
                embed.setDescription(String.format("```%s```", event.getValue("ideeDesc").getAsString()));
                ModalMapping image = event.getValue("ideeImage");
                if (image.getAsString() != null && !image.getAsString().isEmpty()) embed.setImage(image.getAsString());
                embed.setColor(Color.decode("#6666ff"));
                embed.setTimestamp(Instant.now());


                channel.sendMessageEmbeds(embed.build()).setContent(event.getMember().getAsMention() + " " + event.getGuild().getRoleById("1233386630564417628").getAsMention()).queue(message -> {
                        message.createThreadChannel(title).queue();
                        message.addReaction(Emoji.fromUnicode("U+1F44D")).queue();
                        message.addReaction(Emoji.fromUnicode("U+1F44E")).queue();
                });
                event.reply("Успешно!").setEphemeral(true).queue();
        }
}
