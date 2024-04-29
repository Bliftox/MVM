package org.harbingers_of_chaos.mvb.applications;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class Form extends ListenerAdapter {
        public static final String APPLICATION_MODAL_ID = "applicationForm";

        @Override
        public void onButtonInteraction(ButtonInteractionEvent event) {
                if (!event.getButton().getId().equals(Application.SEND_BUTTON_ID)) return;

                ItemComponent nickname = TextInput.create("0", "Ваш никнейм:", TextInputStyle.SHORT)
                        .setRequired(true)
                        .setMaxLength(16)
                        .setPlaceholder("Ввести")
                        .build();

                ItemComponent years = TextInput.create("1", "Сколько вам лет?", TextInputStyle.SHORT)
                        .setRequired(true)
                        .setMaxLength(2)
                        .setPlaceholder("Ввести")
                        .build();

                ItemComponent sex = TextInput.create("2", "Ваш пол? ", TextInputStyle.SHORT)
                        .setRequired(true)
                        .setMaxLength(1)
                        .setPlaceholder("(М/Ж)")
                        .build();

                ItemComponent bio = TextInput.create("3", "Роскажи о себе:", TextInputStyle.PARAGRAPH)
                        .setRequired(true)
                        .setMaxLength(16)
                        .setPlaceholder("Ввести")
                        .build();

                ItemComponent chupapi = TextInput.create("4", "Чупапи?", TextInputStyle.SHORT)
                        .setRequired(true)
                        .setMaxLength(69)
                        .setPlaceholder("Ввести")
                        .build();

                Modal.Builder modal = Modal.create(APPLICATION_MODAL_ID, "Заявка на сервер").addActionRows(ActionRow.of(nickname), ActionRow.of(years), ActionRow.of(sex), ActionRow.of(bio), ActionRow.of(chupapi));
                event.replyModal(modal.build()).queue();
        }
}
