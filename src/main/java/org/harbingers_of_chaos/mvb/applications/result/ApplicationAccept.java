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
import org.harbingers_of_chaos.mvlib.Config;
import org.harbingers_of_chaos.mvm.MystiVerseModServer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.SQLException;
import java.time.Instant;

public class ApplicationAccept extends ListenerAdapter {
    private static final Color ACCEPT_COLOR = Color.decode("#66ff66");


    private TextChannel textChannel;

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        String acceptChannelId = Config.instance.discord.applicationsLogChannelId;
        if (acceptChannelId == null || acceptChannelId.isEmpty()) {
            return;
        }
        textChannel = Bot.getJda().getTextChannelById(acceptChannelId);

    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getButton().getId().equals(Application.getButtonAcceptId())) {
            String id;
            String nickname;

            try {
                id = new MySQL().getApplicationUserId(event.getMessageId());
                nickname = new MySQL().getFieldsValue(event.getMessageId()).get(0);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (textChannel != null) {
                sendMessageAndEmbed(event, id, textChannel);
            } else {
                MystiVerseModServer.LOGGER.warn("It's impossible to send the accept notification because the channel doesn't exist.");
            }

            changeNickname(event, id, nickname);
            manageRoles(event, id);

            event.getMessage().delete().queue();
            MystiVerseModServer.LOGGER.info("[LDBot] The application from " + event.getGuild().getMemberById(id).getEffectiveName() + " has been successfully accepted.");
        }
    }

    private void sendMessageAndEmbed(ButtonInteractionEvent event, String id, TextChannel textChannel) {
        EmbedBuilder acceptEmbed = new EmbedBuilder()
                .setColor(ACCEPT_COLOR)
                .setTitle("✅ Принят")
                .setTimestamp(Instant.now());

        textChannel.sendMessage(event.getGuild().getMemberById(id).getAsMention()).setEmbeds(acceptEmbed.build()).queue();
    }


    private void changeNickname(ButtonInteractionEvent event, String id, String nickname) {
        if (!Config.instance.discord.changeNickname) {
            try {
                event.getGuild().getMemberById(id).modifyNickname(nickname).queue();
            } catch (HierarchyException e) {
                MystiVerseModServer.LOGGER.warn("Cannot change user role higher than bot");
            }
        }
    }

    private void manageRoles(ButtonInteractionEvent event, String id) {
        for (String roleId : Config.instance.discord.accessRoleIds) {
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
