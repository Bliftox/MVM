package org.harbingers_of_chaos.mvlib.config;

import net.fabricmc.loader.api.FabricLoader;
import org.harbingers_of_chaos.mvm.MystiVerseModServer;

import com.google.gson.annotations.Expose;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.harbingers_of_chaos.mvm.MystiVerseModServer.LOGGER;

public class Embend {
    public static AccessApplication accessApplication;
    public static ApplicationButton applicationButton;
    public static RejectApplication rejectApplication;
    public static void load() throws Exception {
        Path dirPath = FabricLoader.getInstance().getConfigDir().resolve("mvm");
        if (!Files.exists(dirPath)) {
            LOGGER.info(dirPath.toFile().mkdirs() ? "dir mvm created" : "dir mvm not created");
        }
        Path access_application = dirPath.resolve("access_application.json");
        Path application_button = dirPath.resolve("application_button.json");
        Path reject_application = dirPath.resolve("reject_application.json");

        if (!Files.exists(access_application)) {
            accessApplication = new AccessApplication();
            Files.writeString(access_application, MystiVerseModServer.GSON.toJson(accessApplication), StandardCharsets.UTF_8);
            LOGGER.info("Access application loaded");
        }
        if (!Files.exists(application_button)) {
            applicationButton = new ApplicationButton();
            Files.writeString(application_button, MystiVerseModServer.GSON.toJson(applicationButton), StandardCharsets.UTF_8);
            LOGGER.info("Application button loaded");
        }
        if (!Files.exists(reject_application)) {
            rejectApplication = new RejectApplication();
            Files.writeString(reject_application, MystiVerseModServer.GSON.toJson(rejectApplication), StandardCharsets.UTF_8);
            LOGGER.info("Reject application loaded");
        }
    }
    public static class AccessApplication{
        @Expose public String color = "#00ffcc";
        @Expose public String title = "Заявка принята!";
    }
    public static class ApplicationButton{
        public static class Image{
            @Expose public String url ="https://i.redd.it/zt6mzvpvcvk31.jpg";
        }
        @Expose public Image image = new Image();
        @Expose public int color = 2829617;
        @Expose public String description = "## Заявочки \nОтправить заявку на сервер";

    }
    public static class RejectApplication{
        @Expose public String color = "#ff5050";
        @Expose public String title = "Заявка отклонена!";
    }
}
