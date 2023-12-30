package org.harbingers_of_chaos.mvb.application;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class HelloButton {
    public static boolean onButton(ButtonInteractionEvent event) {

        TextInput nickname = TextInput.create("nickname", "[🎀] Ник в игре:", TextInputStyle.SHORT)
                .setPlaceholder("Введите ваш ник в игре")
                .setMinLength(3)
                .setMaxLength(15)
                .build();

        TextInput years = TextInput.create("years", "[🧭] Сколько вам лет?", TextInputStyle.SHORT)
                .setPlaceholder("Введите свой настоящий возраст")
                .setMinLength(1)
                .setMaxLength(2)
                .setRequired(true)
                .build();

        TextInput sex = TextInput.create("sex", "[🎨] Ваш пол:", TextInputStyle.SHORT)
                .setPlaceholder("Введите ваш пол")
                .setMinLength(1)
                .setMaxLength(10)
                .setRequired(true)
                .build();

        TextInput bio = TextInput.create("bio", "[📕] Роскажите о себе:", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Поделитесь немного о себе: чем занимаетесь, какие у вас увлечения и интересы?")
                .setMinLength(1)
                .setMaxLength(200)
                .setRequired(true)
                .build();

        TextInput whyWe = TextInput.create("whyWe", "[💚] Почему именно наш сервер?", TextInputStyle.SHORT)
                .setPlaceholder("Чем вам приглянулся наш сервер?")
                .setMinLength(1)
                .setMaxLength(100)
                .setRequired(true)
                .build();

        Modal modal = Modal.create("application", "Заявка на сервер MystiVerse")
                .addComponents(ActionRow.of(nickname), ActionRow.of(years), ActionRow.of(sex), ActionRow.of(bio), ActionRow.of(whyWe))
                .build();

        event.replyModal(modal).queue();
        return true;
    }
}