package org.harbingers_of_chaos.mvb;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.harbingers_of_chaos.mvlib.Config;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Bot {
        private static JDA jda;

        public  static void main(String[] args) throws Exception {
                startup();

        }


        public static void startup() {
                jda = JDABuilder.createDefault("MTIzMzAzMDI0NDM0OTI0NzUzMA.GKHeQX.2PtrJ9PF-M7yR8X5NvKF6ISry4pFwHDhycJjpY")
                        .setMemberCachePolicy(MemberCachePolicy.ALL)
                        .setChunkingFilter(ChunkingFilter.ALL)
                        .enableIntents(GatewayIntent.GUILD_MESSAGES,
                                GatewayIntent.MESSAGE_CONTENT,
                                GatewayIntent.DIRECT_MESSAGES,
                                GatewayIntent.GUILD_MEMBERS,
                                GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                                GatewayIntent.GUILD_WEBHOOKS)
                        .addEventListeners(new Command(), new Blank(), new Form(), new Application())
                        .build();
        }

        public static void shutdown() {
                jda.shutdown();
        }

        public static void log(@NotNull String s) {
                jda.getGuildById(Config.instance.discord.guildId).getTextChannelById(Config.instance.discord.logChannelId).sendMessage(s).queue();
        }

        public static JDA getJda() {
                return jda;
        }

        public void setJda(JDA jda) {
                this.jda = jda;
        }
}
