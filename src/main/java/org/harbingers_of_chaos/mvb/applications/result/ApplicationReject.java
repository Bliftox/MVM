package org.harbingers_of_chaos.mvb.applications.result;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.harbingers_of_chaos.mvb.Bot;
import org.harbingers_of_chaos.mvb.applications.Application;
import org.harbingers_of_chaos.mvlib.MySQL;
import org.harbingers_of_chaos.mvlib.config.Config;
import org.harbingers_of_chaos.mvm.MystiVerseModServer;
import org.jetbrains.annotations.NotNull;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Objects;

public class ApplicationReject extends ListenerAdapter {
    private static final Color REJECT_COLOR = Color.decode("#ff4d4d");


    private TextChannel textChannel;

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        String rejectChannelId = Config.instance.discord.applicationsLogChannelId;
        if (rejectChannelId == null || rejectChannelId.isEmpty()) {
            return;
        }
        textChannel = Bot.getJda().getTextChannelById(rejectChannelId);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getButton().getId().equals(Application.getButtonRejectId())) {


            String id;
                id = new MySQL().getApplicationUserId(event.getMessageId());


            if (textChannel != null) {
                sendMessageAndEmbed(event, id, textChannel);
            } else {
                MystiVerseModServer.LOGGER.warn("It's impossible to send the reject notification because the channel doesn't exist.");
            }

            manageRoles(event, id);

            event.getChannel().asTextChannel().deleteMessageById(event.getMessageId()).queue();
            MystiVerseModServer.LOGGER.info("[LDBot] The application from " + event.getGuild().getMemberById(id).getEffectiveName() + " has been successfully rejected.");
        }
    }

    private void sendMessageAndEmbed(ButtonInteractionEvent event, String id, TextChannel textChannel) {
        EmbedBuilder rejectEmbed = new EmbedBuilder()
                .setColor(REJECT_COLOR)
                .setTitle("⛔ Отказано")
                .setTimestamp(Instant.now());

        textChannel.sendMessage(event.getGuild().getMemberById(id).getAsMention()).setEmbeds(rejectEmbed.build()).queue();
    }
    

    private void manageRoles(ButtonInteractionEvent event, String id) {
        for (String roleId : Config.instance.discord.rejectRoleIds) {
            if (roleId != null && !roleId.isEmpty()) {
                try {
                    event.getGuild().addRoleToMember(UserSnowflake.fromId(id), event.getGuild().getRoleById(roleId)).queue();
                } catch (HierarchyException e) {
                    MystiVerseModServer.LOGGER.warn("Cannot change user role higher than bot");
                } catch (IllegalArgumentException e) {
                    MystiVerseModServer.LOGGER.warn("Role does not exist");
                } catch (NullPointerException e) {
                    MystiVerseModServer.LOGGER.warn("Cannot change user role higher than bot");
                }
            }
        }

        for (String roleId : Config.instance.discord.inProgressRoleIds) {
            if (roleId != null && !roleId.isEmpty()) {
                try {
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(id), event.getGuild().getRoleById(roleId)).queue();
                } catch (HierarchyException e) {
                    MystiVerseModServer.LOGGER.warn("Cannot change user role higher than bot");
                } catch (IllegalArgumentException e) {
                    MystiVerseModServer.LOGGER.warn("Role does not exist");
                } catch (NullPointerException e) {
                    MystiVerseModServer.LOGGER.warn("Cannot change user role higher than bot");
                }
            }
        }
    }
}
