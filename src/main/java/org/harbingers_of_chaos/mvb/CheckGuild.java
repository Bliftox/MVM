package org.harbingers_of_chaos.mvb;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.ling.ldbot.main.LDBot;

public class CheckGuild  extends ListenerAdapter {

    private final LDBot plugin;

    public CheckGuild(LDBot plugin) {
        this.plugin = plugin;
    }


    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {

        if (plugin.getConfig().getString("guildId") != null && !plugin.getConfig().getString("guildId").isEmpty()) {
            if (!event.getGuild().getId().equals(plugin.getConfig().getString("guildId"))) {
                Bukkit.getLogger().warning("\nChange the server ID in the config.yml. \nIf it is set correctly and you are still having issues, regenerate the bot token. \nIt's possible that someone is trying to use your bot on a different server! \nIf you think this is an error, Be sure to report this issue to the developer!\n\n Guild:\n" +
                        event.getGuild().getName() + "\n" +
                        event.getGuild().getId() + "\n\n" +
                        "Owner:\n" +
                        event.getGuild().getOwnerId() + "\n" +
                        event.getGuild().getOwner().getUser().getName() + "\n" +
                        event.getGuild().getOwner().getUser().getGlobalName()
                        );

                event.getGuild().leave().queue();


            } else return;
        } else return;
    }
}
