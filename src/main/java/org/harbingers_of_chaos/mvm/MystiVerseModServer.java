package org.harbingers_of_chaos.mvm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.harbingers_of_chaos.mvb.Bot;
import org.harbingers_of_chaos.mvlib.AccountLinking;
import org.harbingers_of_chaos.mvlib.config.Config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.harbingers_of_chaos.mvlib.MySQL;
import org.harbingers_of_chaos.mvweb.Website;

public class MystiVerseModServer implements ModInitializer {
    public static final String MOD_ID = "mws";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final AccountLinking ACCOUNT_LINKING = new AccountLinking();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setLenient().create();

    @Override
    public void onInitialize() {
        try {Config.load();} catch (Exception e) {LOGGER.warn("[MVM] Failed to load config using defaults : ", e);}
        MySQL.connection("database.db");
        MySQL.createDB();
        loadEvents();
    }
    private void loadEvents() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            Bot.startup();
            Website.startup();

        });
        ServerLifecycleEvents.SERVER_STARTED.register(server -> Bot.log(Config.instance.game.serverStartMessage));

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            Bot.log(Config.instance.game.serverStopMessage);
            Bot.shutdown();
            MySQL.disconnect();
            Config.save();
        });
    }
}
