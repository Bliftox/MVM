package org.harbingers_of_chaos.mvlib;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.annotations.Expose;

import net.fabricmc.loader.api.FabricLoader;
import org.harbingers_of_chaos.mvm.MystiVerseModServer;

public class Config {
    public static Config instance;

    public static void load() throws Exception {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("mvm.json");

        if (Files.exists(configPath)) {
            instance = MystiVerseModServer.GSON.fromJson(Files.readString(configPath), Config.class);
        } else {
            instance = new Config();
            Files.writeString(configPath, MystiVerseModServer.GSON.toJson(instance), StandardCharsets.UTF_8);
        }
    }
    public static void save() throws Exception {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("mvm.json");

        if (Files.exists(configPath)) {
            Files.writeString(configPath, MystiVerseModServer.GSON.toJson(instance), StandardCharsets.UTF_8);
        } else {
            MystiVerseModServer.LOGGER.error("MVM:No config");
        }
    }

    @Expose public Discord discord = new Discord();
    @Expose public MySQLConfig mySQLConfig = new MySQLConfig();
    @Expose public Game game = new Game();
    @Expose public Crashes crashes = new Crashes();

    public static class Discord {
        @Expose public String webhook = "";
        @Expose public String applicationsLogChannelId = "1189900614226944110";
        @Expose public String guildId = "1143266536958722240";
        @Expose public String logChannelId = "1189900614226944110";
        @Expose public String applicationsChannelId = "1189996402164629575";
        @Expose public String token = "";

        @Expose public boolean accessRoleEnable = true;
        @Expose public String[] accessRoleIds = {"1160295664668913816"};

        @Expose public boolean rejectRoleEnable = true;
        @Expose public String[] rejectRoleIds = {"1160295664668913816"};

        @Expose public boolean inProgressRoleEnable = true;
        @Expose public String[] inProgressRoleIds = {"1160295664668913816"};
    }
    public static class MySQLConfig{
        @Expose public String password = "";
        @Expose public String url = "";
        @Expose public String user = "";

    }
    public static class Game {
        @Expose public String serverStartMessage = "Server has started!";
        @Expose public String serverStopMessage = "Server has stopped!";
        @Expose public String serverCrashMessage = "Server has crashed!";
        @Expose public int players = 0;
        @Expose public boolean mirrorDeath = true;
        @Expose public boolean mirrorAdvancements = true;
    }

    public static class Crashes {
        @Expose public boolean uploadToMclogs = true;
    }
}
