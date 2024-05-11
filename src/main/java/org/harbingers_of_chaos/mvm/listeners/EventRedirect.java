package org.harbingers_of_chaos.mvm.listeners;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import org.harbingers_of_chaos.mvb.Bot;
import org.harbingers_of_chaos.mvlib.config.Config;
import org.harbingers_of_chaos.mvlib.MySQL;
import org.harbingers_of_chaos.mvm.event.PlayerConnectedCallback;


public final class EventRedirect {
    private EventRedirect() {}

    public static void init() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlayerConnectedCallback.EVENT.invoker().onConnected(handler.player, server);
        });
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            Bot.startup();
        });
        ServerLifecycleEvents.SERVER_STARTED.register(server -> Bot.log(Config.instance.game.serverStartMessage));

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            Bot.log(Config.instance.game.serverStopMessage);
            Bot.shutdown();
            MySQL.disconnect();
            try {
                Config.save();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
