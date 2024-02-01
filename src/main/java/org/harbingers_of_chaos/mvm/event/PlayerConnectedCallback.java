package org.harbingers_of_chaos.mvm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.sql.SQLException;

public interface PlayerConnectedCallback {
    Event<PlayerConnectedCallback> EVENT = EventFactory.createArrayBacked(PlayerConnectedCallback.class, (listeners) -> (player, server) -> {
        for (PlayerConnectedCallback listener : listeners) {
            listener.onConnected(player, server);
        }
    });

    void onConnected(ServerPlayerEntity player, MinecraftServer server) ;
}
