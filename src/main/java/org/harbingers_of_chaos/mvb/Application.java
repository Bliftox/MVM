package org.harbingers_of_chaos.mvb;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

public class Application extends ListenerAdapter {
        public static final String ACCESS_BUTTON_ID = "access";
        public static final String REJECT_BUTTON_ID = "reject";
        public static final String SEND_BUTTON_ID = "send";

        @Override
        public void onModalInteraction(ModalInteractionEvent event) {
                if (!event.getModalId().equals(Form.APPLICATION_MODAL_ID)) return;

                // не закончено, в ожидании бд

                /*
                * Сохранение в базу даннных
                */

                TextChannel channel = Bot.getJda().getTextChannelById("1233351984132788276");

                EmbedBuilder embed = new EmbedBuilder();

                String application = String.format("## Ник: ```%s```\n## Лет: ```%s```\n## Пол: ```%s```\n## Био: ```%s```\n## Чупапи  ```%s``` ", event.getValue("0").getAsString(), event.getValue("1").getAsString(), event.getValue("2").getAsString(), event.getValue("3").getAsString(), event.getValue("4").getAsString());

                embed.setColor(Color.decode("#ff0066"));
                embed.setTimestamp(Instant.now());
                embed.setDescription(application);
                embed.setImage("https://cdn.discordapp.com/attachments/1160292488377020456/1233359852194107392/image.png?ex=662ccf5f&is=662b7ddf&hm=79f0f641253cabb6354ee1fc18c5053278790f774ac5b79f65ad8cac354d6ad4&");


                channel.sendMessageEmbeds(embed.build()).setContent(event.getMember().getAsMention()).addActionRow(
                        Button.of(
                                ButtonStyle.SUCCESS,
                                Application.ACCESS_BUTTON_ID,
                                "Принять"
                        ),

                        Button.of(
                                ButtonStyle.DANGER,
                                Application.REJECT_BUTTON_ID,
                                "Отклонить"
                        )
                ).queue();
                event.reply("Успешно").setEphemeral(true).queue();
        }


        @Override
        public void onButtonInteraction(ButtonInteractionEvent event) {
                if (event.getButton().getId().equals(Application.ACCESS_BUTTON_ID)) {

                        // не закончено, в ожидании бд

                        TextChannel channel = event.getGuild().getTextChannelById("1233361269453750272");

                        try {
                                DataObject json = DataObject.fromJson(Files.newInputStream(Path.of("/home/ling/IdeaProjects/MVM/src/main/resources/configs/access_application.json")));
                                EmbedBuilder embed = EmbedBuilder.fromData(json);

                                // нужно получить айди пользователя из базы данных
                                channel.sendMessageEmbeds(embed.build()).setContent("").queue();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }


                } else if (event.getButton().getId().equals(Application.REJECT_BUTTON_ID)) {

                        TextInput reason = TextInput.create("reason", "Причина", TextInputStyle.PARAGRAPH).setPlaceholder("Не обязательно").setMaxLength(256).build();

                        Modal.Builder modal = Modal.create("reasonModal", "Укажите причина отказа").addActionRows(ActionRow.of(reason));

                        event.replyModal(modal.build()).queue();
                        // не закончено, в ожидании бд

                }
        }


}
