package org.harbingers_of_chaos.mvb.CityHandler;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Objects;

import static org.harbingers_of_chaos.mvb.Main.*;

public class cityHandler extends ListenerAdapter {
    public static String cityName;
    public static String cityType;
    public static String cityDescription;
    public static String cityFormОfGovernment;
    public static String cityCoordinates;
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        guild = event.getGuild();

        cityName = Objects.requireNonNull(event.getValue("cityName")).getAsString();
        cityType = Objects.requireNonNull(event.getValue("cityType")).getAsString();
        cityDescription = Objects.requireNonNull(event.getValue("cityDescription")).getAsString();
        cityFormОfGovernment = Objects.requireNonNull(event.getValue("cityFormОfGovernment")).getAsString();
        if (cityCoordinates != null) {cityCoordinates = Objects.requireNonNull(event.getValue("cityCoordinates")).getAsString();}

        String rulerId = event.getUser().getId();
        String rulerName = event.getUser().getName();



        if (event.getModalId().equals("cityModal")) {
            Role rulerRole = guild.createRole().setName("[🏛️] Правитель " + cityName).setColor(Color.BLACK).complete();
            guild.addRoleToMember(UserSnowflake.fromId(rulerId), Objects.requireNonNull(guild.getRoleById(rulerRole.getId()))).queue();

            guild.createCategory( "[" + "]" + cityFormОfGovernment + cityName).queue();

            event.reply("Город " + cityName + " успешно создан! 🎄").setEphemeral(true).queue();
        }

    }
}
