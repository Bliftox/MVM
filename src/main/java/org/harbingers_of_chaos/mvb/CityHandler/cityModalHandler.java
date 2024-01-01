package org.harbingers_of_chaos.mvb.CityHandler;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class cityModalHandler extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("cityButton")) {
            TextInput cityName = TextInput.create("cityName", "[🌆] Название", TextInputStyle.SHORT)
                    .setPlaceholder("Название жилого пункта")
                    .setMaxLength(15)
                    .setRequired(true)
                    .build();

            TextInput cityType = TextInput.create("cityType", "[🏙️] Тип", TextInputStyle.SHORT)
                    .setPlaceholder("Тип жилого пункта (Город, лагерь, деревня тд)")
                    .setMaxLength(15)
                    .setRequired(true)
                    .build();

            TextInput cityDescription = TextInput.create("cityDescription", "[🌁] Описание", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Название жилого пункта")
                    .setMaxLength(500)
                    .setRequired(true)
                    .build();

            TextInput cityFormОfGovernment = TextInput.create("cityFormОfGovernment", "[🏛️] Форма правления", TextInputStyle.SHORT)
                    .setPlaceholder("Введите форму правления вашего населенного пункта")
                    .setMaxLength(25)
                    .setRequired(true)
                    .build();

            TextInput cityCoordinates = TextInput.create("cityCoordinates", "[🌃] Координаты (Не обязательно)", TextInputStyle.SHORT)
                    .setPlaceholder("Введите точные координаты населенного пункта")
                    .setMaxLength(20)
                    .setRequired(false)
                    .build();

            Modal cityModal = Modal.create("cityModal", "Заявка на сервер MystiVerse")
                    .addComponents(ActionRow.of(cityName), ActionRow.of(cityType), ActionRow.of(cityDescription), ActionRow.of(cityFormОfGovernment), ActionRow.of(cityCoordinates))
                    .build();

            event.replyModal(cityModal).queue();
        }
    }



}
