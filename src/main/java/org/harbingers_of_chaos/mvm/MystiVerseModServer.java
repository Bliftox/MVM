package org.harbingers_of_chaos.mvm;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.harbingers_of_chaos.mvlib.AccountLinking;
import org.harbingers_of_chaos.mvlib.config.Config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.harbingers_of_chaos.mvlib.MySQL;
import org.harbingers_of_chaos.mvlib.config.Embend;
import org.harbingers_of_chaos.mvm.listeners.EventRedirect;
import org.harbingers_of_chaos.mvm.listeners.MinecraftEventListeners;

public class MystiVerseModServer implements ModInitializer {
    public static final String MOD_ID = "mws";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final AccountLinking ACCOUNT_LINKING = new AccountLinking();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setLenient().create();

    @Override
    public void onInitialize() {
        try {
            Embend.load();
            Config.load();
        } catch (Exception e) {
            LOGGER.warn("Failed to load config using defaults : ", e);
        }
        EventRedirect.init();
        MinecraftEventListeners.init();

        MySQL.connection();
        MySQL.createDB();
    }
}
