package org.harbingers_of_chaos.mvb.suggestion;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.sticker.StickerSnowflake;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.harbingers_of_chaos.mvb.Discord;

import java.awt.*;
import java.util.Collection;
import java.util.TreeSet;

import static org.harbingers_of_chaos.mvb.Discord.*;
import static org.harbingers_of_chaos.mvb.application.ApplicationHandler.date;
import static org.harbingers_of_chaos.mvb.application.ApplicationHandler.format;
import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;

public class SuggestHandler extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        // Проверка, был ли нажат кнопка "suggestButton"
        if (event.getComponentId().equals("suggestButton")) {
            // Создание поля для ввода текста
            TextInput suggestTitle = TextInput.create("suggestTitle", "[📌] Название предложения", TextInputStyle.SHORT)
                    .setPlaceholder("Придумайте название для своего предложения")
                    .setMinLength(1)
                    .setMaxLength(50)
                    .setRequired(true)
                    .build();

            TextInput suggestText = TextInput.create("suggestText", "[💡] Что вы хотите предложить?", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Введите ваше предложение")
                    .setMinLength(1)
                    .setMaxLength(500)
                    .setRequired(true)
                    .build();

            // Создание модального окна для ввода предложения
            Modal suggestModal = Modal.create("suggestModal", "Заявка на сервер MystiVerse")
                    .addComponents(ActionRow.of(suggestTitle), ActionRow.of(suggestText))
                    .build();

            // Отправка модального окна пользователю
            event.replyModal(suggestModal).queue();

            // Вывод информации в лог о начале процесса предложения
            LOGGER.info("Suggest");
        }
    }

    private static TextChannel suggestChannel;
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        try {
            // Проверка, было ли взаимодействие с модальным окном "suggestModal"
            if (event.getModalId().equals("suggestModal")) {
                // Получение идентификатора и имени автора предложения
                String suggestAuthorId = event.getUser().getId();
                String suggestAuthorName = event.getUser().getName();

                // Получение текста предложения
                String suggestText = event.getValue("suggestText").getAsString();
                String suggestTitle = event.getValue("suggestTitle").getAsString();

                // Получение текстового канала для отправки предложения
                suggestChannel = Discord.getJda().getTextChannelById("1190289941817733140");

                if (suggestChannel != null) {
                    // Вывод в лог информации о добавлении предложения
                    LOGGER.info("Suggest added " + suggestAuthorId);

                    // Создание встроенного сообщения с предложением
                    EmbedBuilder suggestEmbed = new EmbedBuilder();
                    suggestEmbed.setTitle("[💡] Предложение от " + suggestAuthorName);
                    suggestEmbed.setDescription("\n**" + suggestTitle + "**\n```" + suggestText + "```");
                    suggestEmbed.setColor(new Color(0x0097F5));
                    suggestEmbed.setFooter("Дата создания: " + format.format(date) + "  \nAuthorID: " + suggestAuthorId);
                    event.reply("Предложение успешно отправлена! 🎄").setEphemeral(true).queue();

                    try {
                        // Отправка сообщения с упоминанием роли, автора предложения и самого предложения
                        suggestChannel.sendMessage("")
                                .setStickers() // Установка стикеров (если нужно)
                                .setEmbeds(suggestEmbed.build())
                                .queue(sentMessage -> {
                                    // Добавление реакции к сообщению
                                    sentMessage.createThreadChannel("обсуждение " + suggestTitle).queue();
                                    sentMessage.addReaction(Emoji.fromUnicode("👍")).queue();
                                    sentMessage.addReaction(Emoji.fromUnicode("👎")).queue();
                                }, throwable -> {
                                    // Обработка ошибки, если что-то пошло не так
                                    throwable.printStackTrace();
                                });
                    } catch (Exception e) {
                        LOGGER.warn("Send suggest error" + e);
                    }
                } else {
                    // В случае ошибки отправки сообщения
                    event.reply("Произошла ошибка! ⛔").setEphemeral(true).queue();
                }
            }
        } catch (Exception e) {
            // Обработка и вывод в лог исключений
            LOGGER.warn("Suggest exception " + e);
        }
    }
}
