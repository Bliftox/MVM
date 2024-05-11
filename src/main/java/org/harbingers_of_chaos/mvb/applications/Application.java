package org.harbingers_of_chaos.mvb.applications;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.harbingers_of_chaos.mvb.Bot;
import org.harbingers_of_chaos.mvlib.MySQL;
import org.harbingers_of_chaos.mvlib.config.Config;
import org.harbingers_of_chaos.mvm.MystiVerseModServer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;

public class Application extends ListenerAdapter {

    public enum Fields {
        APPLICATION_FIELD_ZERO,
        APPLICATION_FIELD_ONE,
        APPLICATION_FIELD_TWO,
        APPLICATION_FIELD_THREE,
        APPLICATION_FIELD_FOUR
    }

    // Constants
    private static final String RESUME_BUTTON_ID = "resume";

    private static final String APPLICATION_MODAL_ID = "applicationModal";

    private static final String BUTTON_ACCEPT_LABEL = "Принять";
    private static final String BUTTON_ACCEPT_ID = "ACCEPT";

    private static final String BUTTON_REJECT_LABEL = "Отказать";
    private static final String BUTTON_REJECT_ID = "REJECT";

    public static String getButtonAcceptId() {
        return BUTTON_ACCEPT_ID;
    }

    public static String getButtonRejectId() {
        return BUTTON_REJECT_ID;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("заявка") && (event.getMember().getId().equals("753149425118871622") || event.getMember().getId().equals("748852911760736319"))) {
            EmbedBuilder resumeBuilder = new EmbedBuilder()
                    .setTitle("📄 Заявки на сервер")
                    .setDescription("- Чтобы подать заявку на сервер, нажмите\n на кнопку ниже и заполните заявку по форме.")
                    .setColor(Color.decode("#ffcc00"))
                    .setImage("https://cdn.discordapp.com/attachments/1197957820562296895/1211049362566684772/f5f8879f0c89bfef5317d29abc54b41c.png?ex=65ecc89c&is=65da539c&hm=07310e9c16495a612f63e68836a8bd9242f111806a9af26fecb12419b775aa39&");

            TextChannel textChannel = event.getChannel().asTextChannel();

            textChannel.sendMessage("").setEmbeds(resumeBuilder.build()).addActionRow(Button.of(
                            ButtonStyle.DANGER, RESUME_BUTTON_ID, "Подать", Emoji.fromUnicode("🍄")))
                    .queue();

            event.reply("✅ Успешное создание резюме").setEphemeral(true).queue();
        }

    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().equals(RESUME_BUTTON_ID)) {

            if (new MySQL().isUserInDatabase(event.getUser().getId())) {
                event.reply("⚠️ Вы уже подали заявку!").setEphemeral(true).queue();
                return;
            }

            if (Config.instance.discord.applicationsEnable) {

                TextInput fieldZero = TextInput.create(Fields.APPLICATION_FIELD_ZERO.name(), "[🎗️] Ваш ник в игре", TextInputStyle.SHORT)
                        .setRequired(true)
                        .setMaxLength(16)
                        .build();

                TextInput fieldOne = TextInput.create(Fields.APPLICATION_FIELD_ONE.name(), "[🎨] Сколько вам лет?", TextInputStyle.SHORT)
                        .setRequired(true)
                        .setMaxLength(2)
                        .build();

                TextInput fieldTwo = TextInput.create(Fields.APPLICATION_FIELD_TWO.name(), "[📜] Немного о себе", TextInputStyle.PARAGRAPH)
                        .setRequired(true)
                        .setMaxLength(500)
                        .build();

                TextInput fieldThree = TextInput.create(Fields.APPLICATION_FIELD_THREE.name(), "[🍄] Почему именно на сервер?", TextInputStyle.PARAGRAPH)
                        .setRequired(true)
                        .setMaxLength(150)
                        .build();

                TextInput fieldFour = TextInput.create(Fields.APPLICATION_FIELD_FOUR.name(), "[⚠️] Любите играть с читами?", TextInputStyle.SHORT)
                        .setRequired(true)
                        .setMaxLength(50)
                        .build();

                Modal applicationModal = Modal.create(APPLICATION_MODAL_ID, "[🐸] Заявка на сервер")
                        .addActionRows(
                                ActionRow.of(fieldZero),
                                ActionRow.of(fieldOne),
                                ActionRow.of(fieldTwo),
                                ActionRow.of(fieldThree),
                                ActionRow.of(fieldFour)
                        ).build();

                event.replyModal(applicationModal).queue();
            } else event.reply("⚠️ На данный момент заявки на данном сервере прекращены").setEphemeral(true).queue();
        }
    }


    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals(APPLICATION_MODAL_ID)) {

            if ((Config.instance.discord.applicationsChannelId == null) || Config.instance.discord.applicationsChannelId.isEmpty()) {
                MystiVerseModServer.LOGGER.warn("The value for [applications.channelId] is incorrect.");
                return;
            }

            String fieldOneValue = event.getValue(Fields.APPLICATION_FIELD_ZERO.name()).getAsString();
            String fieldTwoValue = event.getValue(Fields.APPLICATION_FIELD_ONE.name()).getAsString();
            String fieldThreeValue = event.getValue(Fields.APPLICATION_FIELD_TWO.name()).getAsString();
            String fieldFourValue = event.getValue(Fields.APPLICATION_FIELD_THREE.name()).getAsString();
            String fieldFiveValue = event.getValue(Fields.APPLICATION_FIELD_FOUR.name()).getAsString();

            EmbedBuilder applicationEmbed = new EmbedBuilder()
                    .setDescription(
                            "## [📋] Заявка от " + event.getUser().getName() + "\n" +
                                    "### 🎗️ Ваш ник в игре" + "\n```text\n" + fieldOneValue + "\n```" +
                                    "\n\n### 🎨 Сколько вам лет?" + "\n```text\n" + fieldTwoValue + "\n```" +
                                    "\n\n### 📜 Немного о себе" + "\n```text\n" + fieldThreeValue + "\n```" +
                                    "\n\n### 🍄 Почему именно на сервер?" + "\n```text\n" + fieldFourValue + "\n```" +
                                    "\n\n### ⚠️ Любите играть с читами?" + "\n```text\n" + fieldFiveValue + "\n```"
                    )

                    .setColor(Color.decode("#ff9933"))
                    .setImage("https://cdn.discordapp.com/attachments/890237163151695892/1210709512693223465/-27-12-2023.png?ex=65eb8c19&is=65d91719&hm=86049861ffecbe6ed389859e3faf5d37e9d2a103c5eafd824255c7f8fe457586&")
                    .setTimestamp(Instant.now());



            try {
                TextChannel textChannel = Bot.getJda().getTextChannelById(Config.instance.discord.applicationsChannelId);

                String[] mentionRoleIds = Config.instance.discord.mentionRoleIds;

                MessageCreateAction message =  textChannel.sendMessageEmbeds(applicationEmbed.build())
                        .addActionRow(
                                Button.of(ButtonStyle.SUCCESS, BUTTON_ACCEPT_ID, BUTTON_ACCEPT_LABEL, Emoji.fromUnicode("✅")),
                                Button.of(ButtonStyle.DANGER, BUTTON_REJECT_ID, BUTTON_REJECT_LABEL, Emoji.fromUnicode("⛔")));

                for (String roleId : mentionRoleIds)
                    message.setContent(message.getContent() + String.format("%s, ", event.getGuild().getRoleById(roleId).getAsMention()));

                message.queue();

                event.reply("✅ Успешно создано!").setEphemeral(true).queue();
            } catch (NullPointerException e) {
                MystiVerseModServer.LOGGER.warn("It's impossible to send the application because the channel doesn't exist.");
            }
        }
    }
}
