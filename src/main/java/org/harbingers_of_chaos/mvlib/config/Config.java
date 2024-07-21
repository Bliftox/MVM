package org.harbingers_of_chaos.mvlib.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.annotations.Expose;

import net.fabricmc.loader.api.FabricLoader;
import org.harbingers_of_chaos.mvm.MystiVerseModServer;

import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;

public class Config {
    public static Config instance;

    private static Path dirPath = FabricLoader.getInstance().getConfigDir().resolve("mvm");
    //private static Path dirPath = new File("D:\\idea\\MVM-1.20.1\\run").toPath();
    private static Path configPath = dirPath.resolve("mvm.json");


    public static void load(){
        if (!Files.exists(dirPath)) {
            LOGGER.info("[MVM] " + (dirPath.toFile().mkdirs() ? "dir mvm created" : "dir mvm not created"));
        }
        try {
            if (Files.exists(configPath)) {
                instance = MystiVerseModServer.GSON.fromJson(Files.readString(configPath), Config.class);
            } else {
                instance = new Config();
                Files.writeString(configPath, MystiVerseModServer.GSON.toJson(instance), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
        LOGGER.error("[MVM] "+e.getMessage());
        }
    }
    public static void save(){
        if (Files.exists(configPath)) {
            try {
                Files.writeString(configPath, MystiVerseModServer.GSON.toJson(instance), StandardCharsets.UTF_8);
            } catch (IOException e) {
                LOGGER.error("[MVM] "+e.getMessage());
            }
        } else {
            LOGGER.error("[MVM] No config");
        }
    }

    @Expose public MySQLConfig mySQLConfig = new MySQLConfig();
    @Expose public Game game = new Game();

    public static class MySQLConfig{
        @Expose public boolean enabled = true;
        @Expose public String password = "";
        @Expose public String url = "";
        @Expose public String user = "";

    }
    public static class Game {
        @Expose public String serverStartMessage = "Server has started!";
        @Expose public String serverStopMessage = "Server has stopped!";
        @Expose public String serverCrashMessage = "Server has crashed!";
        @Expose public Boolean questionnaire = false;
    }
}
