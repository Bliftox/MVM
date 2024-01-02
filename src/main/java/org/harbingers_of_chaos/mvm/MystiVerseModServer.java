package org.harbingers_of_chaos.mvm;

import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.WorldSavePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.harbingers_of_chaos.mvb.Discord;
import org.harbingers_of_chaos.mvm.command.Start;
import org.harbingers_of_chaos.mvm.utils.ModRegistries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

public class MystiVerseModServer implements ModInitializer {
    public static final String MOD_ID = "mws";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setLenient().create();

    private static MinecraftServer minecraftServer;
    public static boolean isLuckPerms = false;
    @Override
    public void onInitialize() {
        isLuckPerms = FabricLoader.getInstance().isModLoaded("luckperms");

        try {
            Config.load();
        } catch (Exception e) {
            LOGGER.warn("Failed to load config using defaults : ", e);
        }

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            Discord.start();
        });
        ServerLifecycleEvents.SERVER_STARTED.register(server -> Discord.send(Config.INSTANCE.game.serverStartMessage));

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            Discord.stop();
            Discord.send(Config.INSTANCE.game.serverStopMessage);
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            LiteralCommandNode<ServerCommandSource> startNode = CommandManager
                    .literal("start")
                    .requires(Permissions.require("mvs.command.root", 2))
                    .then((CommandManager.argument("target", GameProfileArgumentType.gameProfile())
                            .then((CommandManager.argument("target1", GameProfileArgumentType.gameProfile())
                                    .then((CommandManager.argument("target2", GameProfileArgumentType.gameProfile())
                                            .then((CommandManager.argument("target3", GameProfileArgumentType.gameProfile())
                                                    .executes(Start::start)))))))))
                    .build();

            LiteralCommandNode<ServerCommandSource> sendNode = CommandManager
                    .literal("send")
                    .requires(Permissions.require("mvs.command.inv", 2))
                    .then(CommandManager.argument("target", GameProfileArgumentType.gameProfile())
                            .executes(Start::send))
                    .build();

            LiteralCommandNode<ServerCommandSource> invNode = CommandManager
                    .literal("inv")
                    .requires(Permissions.require("mvs.command.inv", 2))
                    .then(CommandManager.argument("target", GameProfileArgumentType.gameProfile())
                            .executes(Start::inv))
                    .build();

            LiteralCommandNode<ServerCommandSource> echestNode = CommandManager
                    .literal("echest")
                    .requires(Permissions.require("mvs.command.echest", 2))
                    .then(CommandManager.argument("target", GameProfileArgumentType.gameProfile())
                            .executes(Start::eChest))
                    .build();




            dispatcher.getRoot().addChild(startNode);
            startNode.addChild(invNode);
            startNode.addChild(echestNode);
            startNode.addChild(sendNode);

        });
        ServerLifecycleEvents.SERVER_STARTING.register(this::onLogicalServerStarting);
    }

    private void onLogicalServerStarting(MinecraftServer server) {
        minecraftServer = server;
    }

    public static MinecraftServer getMinecraftServer() {
        return minecraftServer;
    }

    public static void savePlayerData(ServerPlayerEntity player) {
        File playerDataDir = minecraftServer.getSavePath(WorldSavePath.PLAYERDATA).toFile();
        try {
            NbtCompound compoundTag = player.writeNbt(new NbtCompound());
            File file = File.createTempFile(player.getUuidAsString() + "-", ".dat", playerDataDir);
            NbtIo.writeCompressed(compoundTag, file);
            File file2 = new File(playerDataDir, player.getUuidAsString() + ".dat");
            File file3 = new File(playerDataDir, player.getUuidAsString() + ".dat_old");
            Util.backupAndReplace(file2, file, file3);
        } catch (Exception var6) {
            LogManager.getLogger().warn("Failed to save player data for {}", player.getName().getString());
        }
    }
}
