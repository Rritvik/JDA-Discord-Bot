package com.example.listeners;

import java.util.List;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventListener extends ListenerAdapter{
    
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        User user = event.getUser();
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String channelMention = event.getChannel().getAsMention();
        // String jumpLink = event.getJumpUrl();

        String message = user.getAsTag() + " reacted to a message with " + emoji + " in the " + channelMention + " channel! ";
        ((MessageChannel) event.getGuild().getDefaultChannel()).sendMessage(message).queue();

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if(message.contains("ping")) {
            event.getChannel().sendMessage("pong").queue();
        }
    }


    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String avatar = event.getUser().getEffectiveAvatarUrl();
        System.out.println(avatar);
    }


    /**
     * Event fires when a user updated their online status
     * Requires "Guild Presences" gateway intent AND cache enabled!
     */
    @Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
        List<Member> members = event.getGuild().getMembers();
        int onlineMembers = 0;
        for(Member member: members) {
            if(member.getOnlineStatus() == OnlineStatus.ONLINE) {
                onlineMembers++;
            }
            member.getActivities();
        }

        // WILL NOT WORK WITHOUT USER CACHE
        User user = event.getUser();
        String message = "**" + user.getAsTag() + "** updated their status! There are now " + onlineMembers + " users online in this guild!";
        ((MessageChannel) event.getGuild().getDefaultChannel()).sendMessage(message).queue();
    }
}
