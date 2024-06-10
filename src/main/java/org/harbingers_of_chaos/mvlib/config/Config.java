package org.harbingers_of_chaos.mvlib.config;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.annotations.Expose;

import net.fabricmc.loader.api.FabricLoader;
import org.harbingers_of_chaos.mvm.MystiVerseModServer;

import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;

public class Config {
    public static Config instance;

    public static void load() throws Exception {
        Path dirPath = FabricLoader.getInstance().getConfigDir().resolve("mvm");
        if (!Files.exists(dirPath)) {
            LOGGER.info(dirPath.toFile().mkdirs() ? "dir mvm created" : "dir mvm not created");
        }

        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("mvm").resolve("mvm.json");

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
        @Expose public String applicationsLogChannelId = "";
        @Expose public String guildId = "";
        @Expose public String logChannelId = "";
        @Expose public String applicationsChannelId = "";
        @Expose public String token = "";

        @Expose public boolean applicationsEnable = true;
        @Expose public boolean changeNickname = true;

        @Expose public String[] accessRoleIds = {"1160295664668913816"};

        @Expose public String[] rejectRoleIds = {"1160295664668913816"};

        @Expose public String[] inProgressRoleIds = {"1160295664668913816"};

        @Expose public String[] mentionRoleIds = {"1160295664668913816"};
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
