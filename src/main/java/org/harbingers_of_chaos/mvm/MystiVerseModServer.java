package org.harbingers_of_chaos.mvm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.WorldSavePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.harbingers_of_chaos.mvb.Bot;
import org.harbingers_of_chaos.mvlib.AccountLinking;
import org.harbingers_of_chaos.mvlib.Config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.harbingers_of_chaos.mvlib.MySQL;
import org.harbingers_of_chaos.mvm.listeners.EventRedirect;
import org.harbingers_of_chaos.mvm.listeners.MinecraftEventListeners;

import java.io.File;

public class MystiVerseModServer implements ModInitializer {
    public static final String MOD_ID = "mws";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final AccountLinking ACCOUNT_LINKING = new AccountLinking();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setLenient().create();

    @Override
    public void onInitialize() {
        try {
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
