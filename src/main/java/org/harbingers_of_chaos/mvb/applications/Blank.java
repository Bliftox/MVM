package org.harbingers_of_chaos.mvb.applications;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Blank extends ListenerAdapter {
        @Override
        public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
                if (event.getName().equals("заявка")) {
                        try {
                                Path application_button = FabricLoader.getInstance().getConfigDir().resolve("mvm").resolve("application_button.json");
                                DataObject json = DataObject.fromJson(Files.newInputStream(application_button));
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
