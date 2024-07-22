package org.harbingers_of_chaos.mvm;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.harbingers_of_chaos.mvlib.AccountLinking;
import org.harbingers_of_chaos.mvlib.config.Config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.harbingers_of_chaos.mvlib.SQL;
import org.harbingers_of_chaos.mvm.command.RegCommand;

import java.util.function.Consumer;

public class MystiVerseModServer implements ModInitializer {
    public static final String MOD_ID = "mws";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final AccountLinking ACCOUNT_LINKING = new AccountLinking();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setLenient().create();

    @Override
    public void onInitialize() {
        try {Config.load();} catch (Exception e) {LOGGER.warn("[MVM] Failed to load config using defaults : ", e);}
        SQL.connection();
        SQL.createDB();
        registerEvents();
        registerCommands();
    }
    private void registerEvents() {
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            SQL.disconnect();
            Config.save();
        });
    }
    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
            RegCommand.register(dispatcher);
        });
    }
}