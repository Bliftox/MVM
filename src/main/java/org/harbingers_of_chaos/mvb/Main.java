package org.harbingers_of_chaos.mvb;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.harbingers_of_chaos.mvb.CityHandler.cityHandler;
import org.harbingers_of_chaos.mvb.CityHandler.cityModalHandler;
import org.harbingers_of_chaos.mvb.application.ApplicationHandler;
import org.harbingers_of_chaos.mvb.application.RejectWithReasonButton;
import org.harbingers_of_chaos.mvb.commands.CommandHandler;
import org.harbingers_of_chaos.mvb.commands.NewsEmbedHandler;
import org.harbingers_of_chaos.mvb.suggestion.SuggestHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class Main {
    public static final Logger log = Logger.getGlobal();
    public static Guild guild;
    public static TextChannel applicationsLogChat;
    public static TextChannel applicationsChat;

    public static final Preferences prefs = Preferences.userRoot().node(Main.class.getName());

    public static Integer appInt = prefs.getInt("appInt", 0);

    public static void main(String[] args) {
        log.info("Bot started");



        // Получаем значение счетчика из настроек, если оно там сохранено


//        applicationsLogChat = jda.getTextChannelById("1189900614226944110");
//        applicationsChat = jda.getTextChannelById("1189996402164629575");

        log.info("Number of applications sent: " + appInt);


    }
}