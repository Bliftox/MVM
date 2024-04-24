package org.harbingers_of_chaos.mvb;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.harbingers_of_chaos.mvlib.Config;
import org.jetbrains.annotations.NotNull;

public class Initialize implements Bot {
        private JDA jda;

        @Override
        public void start() {
                jda = JDABuilder.createDefault(Config.INSTANCE.discord.token)
                        .setMemberCachePolicy(MemberCachePolicy.ALL)
                        .setChunkingFilter(ChunkingFilter.ALL)
                        .enableIntents(GatewayIntent.GUILD_MESSAGES,
                                GatewayIntent.MESSAGE_CONTENT,
                                GatewayIntent.DIRECT_MESSAGES,
                                GatewayIntent.GUILD_MEMBERS,
                                GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                                GatewayIntent.GUILD_WEBHOOKS)
                        .build();
        }

        @Override
        public void stop() {
                jda.shutdown();
        }

        @Override
        public void log(@NotNull String s) {

        }
}
