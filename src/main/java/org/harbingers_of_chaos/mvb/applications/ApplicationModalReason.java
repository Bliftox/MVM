package org.harbingers_of_chaos.mvb.applications;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.fabricmc.loader.api.FabricLoader;
import org.harbingers_of_chaos.mvlib.MySQL;
import org.harbingers_of_chaos.mvlib.discord.DataBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ApplicationModalReason extends ListenerAdapter {

        @Override
        public void onModalInteraction(ModalInteractionEvent event) {
                if (!event.getModalId().equals("reasonModal")) return;


                TextChannel channel = event.getGuild().getTextChannelById("1233361269453750272");

                try {
                        Path reject_application = FabricLoader.getInstance().getConfigDir().resolve("mvm").resolve("reject_application.json");
                        DataObject json = DataObject.fromJson(Files.newInputStream(reject_application));
                        EmbedBuilder embed = EmbedBuilder.fromData(json);

                        embed.setDescription(String.format("```%s```", event.getValue("reason")));

                        channel.sendMessageEmbeds(embed.build()).setContent("").queue();
                } catch (IOException e) {
                        e.printStackTrace();
                }

        }
}
