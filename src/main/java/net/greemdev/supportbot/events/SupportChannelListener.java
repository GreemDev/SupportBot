package net.greemdev.supportbot.events;

import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.greemdev.supportbot.SupportBot;
import net.greemdev.supportbot.objects.GuildConfig;
import net.greemdev.supportbot.util.*;

import java.awt.*;

public class SupportChannelListener {

    public static void onMessage(GuildMessageReceivedEvent event) {
        if (ObjectUtil.isNull(event.getMember()) ||
                event.getMember().isOwner()
                /*event.getMember().getUser().isBot()*/) return;

        if (ConfigUtil.getGuildConfigFile(event.getGuild().getId()).exists() &&
                !ObjectUtil.isNull(GuildConfig.get(event.getGuild().getId()).getInitialChannel())) {
            var conf = GuildConfig.get(event.getGuild().getId());
            if (conf.getMaxOpen() > 0 && conf.getOpenTickets().size() >= conf.getMaxOpen()) return;
            if (event.getChannel().getId().equals(conf.getInitialChannel())) {
                var maybeTheirTickets = event.getGuild().getTextChannelsByName(event.getChannel().getName() + "-" + event.getMember().getUser().getId(), true);
                if (maybeTheirTickets.size() > 0) {
                    maybeTheirTickets.get(0).sendMessage(event.getAuthor().getAsMention() + ", please send messages here instead of the main channel. If you need a new ticket, mark this one as solved and send a message in " + event.getChannel().getAsMention() + " again.").queue();
                    event.getMessage().delete().queue();
                    return;
                }

                new Thread(() -> handleNew(event)).start();
            }
        }
    }

    private static void handleNew(GuildMessageReceivedEvent event) {
        String newChannelName = event.getChannel().getName() + "-" + event.getAuthor().getId();
        TextChannel newTc;
        if (ObjectUtil.isNull(event.getChannel().getParent())) {
            newTc = (TextChannel)event.getGuild().getController().createTextChannel(newChannelName).complete();
        } else {
            newTc = (TextChannel)event.getChannel().getParent().createTextChannel(newChannelName).complete();
        }
        event.getGuild().getController().modifyTextChannelPositions().selectPosition(newTc).moveTo(event.getChannel().getPosition()+1).queue();

        newTc.createPermissionOverride(event.getMember()).setAllow(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).queue();

        var rgb = SupportBot.getBotConfig().getEmbedColour();
        var embed = new EmbedBuilder()
                .setColor(new Color(rgb[0], rgb[1], rgb[2]))
                .setDescription("**Author**: " + event.getAuthor().getAsMention() +
                              "\n**Message**: " + event.getMessage().getContentRaw() +
                        "\n\nClick the " + SupportBot.getClient().getSuccess() + " reaction below to close this ticket.")
                .setThumbnail(event.getAuthor().getEffectiveAvatarUrl());
        newTc.sendMessage(embed.build()).queue(m -> m.addReaction(SupportBot.getClient().getSuccess()).queue());

        event.getMessage().delete().queue();
    }

}
