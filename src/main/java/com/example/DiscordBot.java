package com.example;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

/**
 * Hello world!
 */
public final class DiscordBot {

    private final ShardManager shardManager;

    private DiscordBot() throws LoginException {
        String token = "MTA1MzM2NjE5NDk3NzEzMjYwNQ.GgBk0w.VhsUOIEXvdcuQAEBph-kHGMT2T40UJBtUKjwmk";
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Vines"));
        shardManager = builder.build();
    }


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
