package com.example.commands;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CommandManager extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if(command.equals("welcome")) {  // command /welcome
            // run the '/welcome' command
            String userTag = event.getUser().getAsTag();
            String reply = "Welcome to the server, **" + userTag + "**!";
            event.reply(reply).setEphemeral(true).queue();;
        } else if(command.equals("roles")) {
            // run the '/roles' command
            event.deferReply().queue();
            String response = "";
            for(Role role : event.getGuild().getRoles()) {
                response += role.getAsMention() + "\n";
            }
            event.getHook().sendMessage(response).queue();

        } else if(command.equals("say")) {
            // run the '/say' command

            // Get message option
            OptionMapping messageOption = event.getOption("message");
            String message = messageOption.getAsString();

            MessageChannel channel;
            OptionMapping channelOption = event.getOption("channel");
            if(channelOption != null) {
                channel = (MessageChannel) channelOption.getAsChannel();
            } else {
                channel = event.getChannel();
            }

            // send message in chat
            channel.sendMessage(message).queue();
            event.reply("Your message was sent.").setEphemeral(true).queue();
        } else if(command.equals("emote")) {
            OptionMapping option = event.getOption("type");
            String type = option.getAsString();

            String replyMessage = "";
            switch(type.toLowerCase()) {
                case "hug": {
                    replyMessage = "You hug the closest person to you";
                    break;
                }
                case "laugh": {
                    replyMessage = "You laugh histerically at everyone around you.";
                    break;
                }
                case "cry": {
                    replyMessage = "You can't stop crying.";
                    break;
                }
            }
            event.reply(replyMessage).queue();
        } else if(command.equals("giverole")) {
            Member member = event.getOption("user").getAsMember();
            Member Bot = event.getGuild().getSelfMember();
            Role role = event.getOption("role").getAsRole();

            event.getGuild().addRoleToMember(member, role).queue();
            event.reply(member.getAsMention() + " has been given the " + role.getAsMention() + " role!").queue();
        }
    }

    // Guild commands -- instantly updated (max 100 commands)
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("welcome", "Get welcomes by the bot."));
        commandData.add(Commands.slash("roles", "Display all roles on the server"));

        // Command: /say <message> [channel]
        OptionData option1 = new OptionData(OptionType.STRING, "message", "The message you want the bot to say", true);
        OptionData option2 = new OptionData(OptionType.CHANNEL, "channel", "The channel you want to send this message in.").setChannelTypes(ChannelType.TEXT, ChannelType.NEWS, ChannelType.GUILD_PUBLIC_THREAD);
        commandData.add(Commands.slash("say", "Make the bot say a message.").addOptions(option1, option2));

        // Command: /emote [type]
        OptionData option3 = new OptionData(OptionType.STRING, "type", "The type of emotion to express", true).addChoice("Hug", "hug").addChoice("Laugh", "laugh").addChoice("Cry", "cry");
        commandData.add(Commands.slash("emote", "Express your emotions through text.").addOptions(option3));

        // Command: /giverole <user> <role>
        OptionData option4 = new OptionData(OptionType.USER, "user", "The user to give the role to.", true);
        OptionData option5 = new OptionData(OptionType.ROLE, "role", "The role to be given", true);
        commandData.add(Commands.slash("giverole", "Give a user a role.").addOptions(option4, option5));

        // update Commands
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    // Global commands -- up to an hour to update (unlimited commands)
    // @Override
    // public void onReady(ReadyEvent event) {
    //     List<CommandData> commandData = new ArrayList<>();
    //     commandData.add(Commands.slash("welcome", "Get welcomes by the bot."));
    //     event.getJDA().updateCommands().addCommands(commandData).queue();
    // }

    
}
