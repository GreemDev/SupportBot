package net.greemdev.supportbot.events;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ISnowflake;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.greemdev.supportbot.SupportBot;
import net.greemdev.supportbot.files.GuildConfig;
import net.greemdev.supportbot.util.*;
import org.apache.commons.io.FileExistsException;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class SetupListener {

    static void onMessage(GuildMessageReceivedEvent event) {
        if (!ConfigUtil.getGuildConfigFile(event.getGuild().getId()).exists()) {
            if (event.getMessage().getContentRaw().equalsIgnoreCase("setupSupport")) {
                var t = new Thread(() -> handle(event));
                t.setName("Setup | Setting up Guild " +
                        event.getGuild().getId() + ", started by user " +
                        FormatUtil.getUserString(event.getAuthor()));
                t.start();
            }
        }

    }

    private static void handle(GuildMessageReceivedEvent event) {
        var c = event.getChannel();
        if (event.getMember() == null || event.getMember().getUser().isBot() ||
                (!event.getMember().isOwner() && !event.getMember().hasPermission(Permission.ADMINISTRATOR))) return;
        SupportBot.getLogger().info("Setting up " + event.getGuild());
        var authorCanClose = ParserUtil.getYesNo(c.sendMessage("Let users close their own ticket? | " +
                "Recommended: " + SupportBot.getClient().getSuccess()).complete(), event.getAuthor());
        var defaultReaction = ParserUtil.getGuildMessageReactionAddEvent(c.sendMessage("Emoji used to mark tickets as complete. React to this message with an emoji. **Custom emojis do not work yet.** " +
                        "Recommended: " + SupportBot.getClient().getSuccess()).complete(), event.getAuthor()).getReactionEmote().getName();
        var initialChannel = event.getChannel().getId();
        var maxOpen = ParserUtil.getInt(c.sendMessage("How many tickets can be open at any time?").complete().getTextChannel(), event.getAuthor());
        String[] rolesAllowed = new String[0];
        while (rolesAllowed.length == 0) {
            event.getChannel().sendMessage("Send a comma-separated list of role names in this server that you want to be able to help with and close tickets. \n\nAvailable Roles: `" + event.getGuild().getRoles().stream().map(Role::getName).filter(r -> !r.equals("@everyone")).collect(Collectors.joining(", ")) + "`").queue();
            rolesAllowed = Arrays.stream(ParserUtil.getGuildMessageReceived(event.getAuthor()).getMessage().getContentRaw().split(","))
                    .map(s -> ParserUtil.getRoleByName(event.getGuild(), s))
                    .filter(Objects::nonNull)
                    .map(ISnowflake::getId)
                    .toArray(String[]::new);
        }
        try {
            GuildConfig.create(authorCanClose, event.getChannel().getName() ,defaultReaction, initialChannel, rolesAllowed, event.getGuild().getId(), maxOpen).write();
        } catch (FileExistsException e) {
            e.printStackTrace();
            event.getChannel().sendMessage("Failed to create your config file: " + e.getMessage()).queue();
            return;
        }

        event.getChannel().deleteMessages(event.getChannel().getHistory().retrievePast(100).complete()).complete();
        event.getChannel().sendMessage("This server and channel has been setup for the support system! " +
                "Take the time to send a message here instructing your members how to create a support ticket. " +
                "Do not worry about sending a message here, as you are the server owner, so you don't get tickets made for you. " +
                "This message will be automatically deleted in 2 minutes.").queue(m -> m.delete().queueAfter(2, TimeUnit.MINUTES));

    }

}
