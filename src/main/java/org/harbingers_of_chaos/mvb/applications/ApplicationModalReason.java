package org.harbingers_of_chaos.mvb.applications;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ApplicationModalReason extends ListenerAdapter {

        @Override
        public void onModalInteraction(ModalInteractionEvent event) {
                if (!event.getModalId().equals("reasonModal")) return;


                TextChannel channel = event.getGuild().getTextChannelById("1233361269453750272");

                try {
                        DataObject json = DataObject.fromJson(Files.newInputStream(Path.of("/home/ling/IdeaProjects/MVM/src/main/resources/configs/reject_application.json")));
                        EmbedBuilder embed = EmbedBuilder.fromData(json);

                        embed.setDescription(String.format("```%s```", event.getValue("reason")));

                        // нужно получить айди пользователя из базы данных
                        channel.sendMessageEmbeds(embed.build()).setContent("").queue();
                } catch (IOException e) {
                        e.printStackTrace();
                }

        }
}
