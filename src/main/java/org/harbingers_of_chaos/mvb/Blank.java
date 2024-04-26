package org.harbingers_of_chaos.mvb;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public class Blank extends ListenerAdapter {
        @Override
        public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
                if (event.getName().equals("заявка")) {
                        try {
                                DataObject json = DataObject.fromJson(Files.newInputStream(Path.of("/home/ling/IdeaProjects/MVM/src/main/resources/configs/application_button.json")));
                                EmbedBuilder embedBuilder = EmbedBuilder.fromData(json);

                                TextChannel textChannel = event.getChannel().asTextChannel();

                                textChannel.sendMessageEmbeds(embedBuilder.build()).addActionRow(Button.of(
                                                ButtonStyle.PRIMARY,
                                                Application.SEND_BUTTON_ID,
                                                "Подать")
                                        )
                                        .queue();

                                event.reply("✅ Успешное создание кнопки").setEphemeral(true).queue();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }

                }

        }
}
