package org.harbingers_of_chaos.mvm.listeners;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
//import org.harbingers_of_chaos.mvb.Discord;
import org.harbingers_of_chaos.mvlib.Config;
import org.harbingers_of_chaos.mvm.event.PlayerConnectedCallback;


public final class EventRedirect {
    private EventRedirect() {}

    public static void init() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlayerConnectedCallback.EVENT.invoker().onConnected(handler.player, server);
        });
//        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
//            Discord.start();
//        });
//        ServerLifecycleEvents.SERVER_STARTED.register(server -> Discord.send(Config.instance.game.serverStartMessage));

    }
}
