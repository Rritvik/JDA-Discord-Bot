package com.example;

import javax.security.auth.login.LoginException;

import com.example.commands.CommandManager;
import com.example.listeners.EventListener;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

/**
 * Hello world!
 */
public final class DiscordBot {

    private final Dotenv config;

    private final ShardManager shardManager;

    
    /**
     * Loads environment variables and builds the bot shard manager.
     * @throws LoginException occurs when bot token is invalid
     */
    private DiscordBot() throws LoginException {
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");

        // Build shard manager
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Vines"));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY);
        shardManager = builder.build();

        // Register listeners
        shardManager.addEventListener(new EventListener(), new CommandManager());
    }

    /**
     * Retrieves the bot config to access environment variables
     * @return the Dotenv instance for the bot
     */
    public Dotenv getConfig() {
       return config; 
    }

    /**
     * Retrieves the bot shard manager
     * @return the ShardManager instance for the bot
     */
    public ShardManager getShardManager() {
        return shardManager;
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        try {
            DiscordBot bot = new DiscordBot();
        } catch(LoginException e) {
            System.out.println("ERROR: Provided bot token is invalid!");
        }
    }
}
