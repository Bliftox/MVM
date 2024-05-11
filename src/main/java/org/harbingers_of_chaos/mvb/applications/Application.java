package org.harbingers_of_chaos.mvb.applications;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.harbingers_of_chaos.mvb.Bot;
import org.harbingers_of_chaos.mvlib.config.Config;
import org.harbingers_of_chaos.mvlib.MySQL;
import org.harbingers_of_chaos.mvm.MystiVerseModServer;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Application extends ListenerAdapter {
        public static final String ACCESS_BUTTON_ID = "access";
        public static final String REJECT_BUTTON_ID = "reject";
        public static final String SEND_BUTTON_ID = "send";
        public static final String APPLICATION_DEFAULT_FIELD_TAG = "application_filed_";

        @Override
        public void onModalInteraction(ModalInteractionEvent event) {
                if (!event.getModalId().equals(Form.APPLICATION_MODAL_ID)) return;

                List<String> fields = new ArrayList<>();
                int length = event.getValues().size();
                for (int i = 0; i < length; i++) {
                        fields.add(event.getValue(APPLICATION_DEFAULT_FIELD_TAG + i).getAsString());
                }

                TextChannel channel = Bot.getJda().getTextChannelById("1233351984132788276");

                EmbedBuilder embed = new EmbedBuilder();
                String application = String.format("## Ник: ```%s```\n## Лет: ```%s```\n## Пол: ```%s```\n## Био: ```%s```\n## Чупапи  ```%s``` ",
                        event.getValue("application_filed_0").getAsString(), event.getValue("application_filed_1").getAsString(), event.getValue("application_filed_2").getAsString(),
                        event.getValue("application_filed_3").getAsString(), event.getValue("application_filed_4").getAsString());

                embed.setColor(Color.decode("#ff0066"));
                embed.setTimestamp(Instant.now());
                embed.setDescription(application);
                embed.setImage("https://cdn.discordapp.com/attachments/1160292488377020456/1233359852194107392/image.png?ex=662ccf5f&is=662b7ddf&hm=79f0f641253cabb6354ee1fc18c5053278790f774ac5b79f65ad8cac354d6ad4&");

                sendMessageWithButtons(channel, embed, event.getMember(), fields);
                event.reply("Успешно").setEphemeral(true).queue();
        }

        private void sendMessageWithButtons(TextChannel channel, EmbedBuilder embed, Member member, List<String> fields) {
                channel.sendMessageEmbeds(embed.build())
                        .setContent(member.getAsMention())
                        .addActionRow(
                                Button.of(ButtonStyle.SUCCESS, ACCESS_BUTTON_ID, "Принять"),
                                Button.of(ButtonStyle.DANGER, REJECT_BUTTON_ID, "Отклонить")
                        )
                        .queue(message -> new MySQL().saveApplication(message.getId(), member.getId(), fields));
        }

        @Override
        public void onButtonInteraction(ButtonInteractionEvent event) {
                if (event.getButton().getId().equals(SEND_BUTTON_ID)) return;

                Guild guild = event.getGuild();
                TextChannel channel = guild.getTextChannelById("1233361269453750272");
                String applicationId = event.getMessageId();
                String memberId = new MySQL().getApplicationUserId(applicationId);
                Member member = guild.getMemberById(memberId);

                if (event.getButton().getId().equals(ACCESS_BUTTON_ID)) {
                        addRoles(event, member, Config.instance.discord.accessRoleIds, guild);
                        removeRoles(event, member, Config.instance.discord.inProgressRoleIds, guild);
                } else if (event.getButton().getId().equals(REJECT_BUTTON_ID)) {
                        removeRoles(event, member, Config.instance.discord.rejectRoleIds, guild);
                        removeRoles(event, member, Config.instance.discord.inProgressRoleIds, guild);

                        TextInput reason = TextInput.create("reason", "Причина", TextInputStyle.PARAGRAPH).setPlaceholder("Не обязательно").setMaxLength(500).build();
                        Modal.Builder modal = Modal.create(memberId, "Укажите причина отказа").addActionRows(ActionRow.of(reason));
                        event.replyModal(modal.build()).queue();
                }
        }


        private void addRoles(ButtonInteractionEvent event, Member member, String[] roleIds, Guild guild) {
                for (String roleId : roleIds) {
                        Role role = guild.getRoleById(roleId);
                        if (role == null) {
                                String error = "Роли " + roleId + " не существует.";
                                MystiVerseModServer.LOGGER.warn(error);
                                event.reply(error).queue();
                                continue;
                        }
                        if (!member.getRoles().contains(role)) {
                                try {
                                        guild.addRoleToMember(member, role).queue();
                                } catch (HierarchyException e) {
                                        MystiVerseModServer.LOGGER.warn("невозможно изменить роль пользователю рангом выше чем бот");
                                }
                        }
                }
        }

        private void removeRoles(ButtonInteractionEvent event, Member member, String[] roleIds, Guild guild) {
                for (String roleId : roleIds) {
                        Role role = guild.getRoleById(roleId);
                        if (role == null) {
                                String error = "Роли " + roleId + " не существует.";
                                MystiVerseModServer.LOGGER.warn(error);
                                event.reply(error).queue();
                                continue;
                        }
                        if (member.getRoles().contains(role)) {
                                try {
                                        guild.removeRoleFromMember(member, role).queue();
                                } catch (HierarchyException e) {
                                        MystiVerseModServer.LOGGER.warn("невозможно изменить роль пользователю рангом выше чем бот");
                                }
                        }
                }
        }

}
