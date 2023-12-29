package org.harbingers_of_chaos.mvb;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.harbingers_of_chaos.mvb.application.ApplicationHandler;
import org.harbingers_of_chaos.mvb.commands.CommandHandler;
import org.harbingers_of_chaos.mvb.suggestion.SuggestHandler;

import java.util.logging.Logger;
import java.util.prefs.Preferences;

import static org.harbingers_of_chaos.mvb.config.cfg.TOKEN;

public class Main {
    public static final Logger log = Logger.getGlobal();
    public static final JDA jda = JDABuilder.createDefault(TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
            .setActivity(Activity.playing("Лучший в мире сервер MystiVerse"))
            .addEventListeners(new CommandHandler())
            .addEventListeners(new ApplicationHandler())
            .addEventListeners(new SuggestHandler())
            .build();

    private static final String CHECK_KEY = "check";
    private static final Preferences prefs = Preferences.userRoot().node(Main.class.getName());
    public static int check = prefs.getInt(CHECK_KEY, 0);

    public static void main(String[] args) {
        log.info("Bot started");
        Guild guild = jda.getGuildById("1143266536958722240");

        log.info("Количество отправленных заявок: " + check);

        prefs.putInt(CHECK_KEY, check);
    }
}