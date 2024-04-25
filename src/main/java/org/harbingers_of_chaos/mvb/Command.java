package org.harbingers_of_chaos.mvb;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Command extends ListenerAdapter {

        @Override
        public void onGuildReady(@NotNull GuildReadyEvent event) {

                List<CommandData> commandDataList = new ArrayList<>();
                commandDataList.add(Commands.slash("анкета", "Заполнить анкету"));

                event.getGuild().updateCommands().addCommands(commandDataList).queue();
        }


        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (!event.getName().equals("Анкета")) return;

                TextInput field = TextInput.create("field", "Test", TextInputStyle.SHORT).build();

                Modal.Builder builder = Modal.create("Modal", "Tust").addActionRow(field);
                event.replyModal(builder.build()).queue();
        }

        public enum CommandTag {
                QUESTIONNAIRE("questionnaire");

                private final String value;

                CommandTag(String value) {
                        this.value = value;
                }

                public String getValue() {
                        return value;
                }
        }
}
