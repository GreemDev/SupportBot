package net.greemdev.supportbot.events;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ISnowflake;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.greemdev.supportbot.SupportBot;
import net.greemdev.supportbot.files.GuildConfig;
import net.greemdev.supportbot.util.ConfigUtil;
import net.greemdev.supportbot.util.EmojiUtil;
import net.greemdev.supportbot.util.FormatUtil;
import net.greemdev.supportbot.util.ObjectUtil;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

class SupportChannelListener {


    static void onMessage(GuildMessageReceivedEvent event) {
        if (ObjectUtil.isNull(event.getMember()) ||
                //event.getMember().isOwner()
                event.getMember().getUser().isBot()) return;

        if (ConfigUtil.getGuildConfigFile(event.getGuild().getId()).exists() &&
                !ObjectUtil.isNull(GuildConfig.get(event.getGuild().getId()).getInitialChannel())) {
            var conf = GuildConfig.get(event.getGuild().getId());
            if (conf.getMaxOpen() > 0 && conf.getOpenTickets().size() >= conf.getMaxOpen()) {
                event.getChannel().sendMessage("I couldn't create a ticket for you, " +
                        event.getAuthor().getAsMention() + ", because this server's open tickets limit has been reached. " +
                        "Please try again later.").queue(m -> m.delete().queueAfter(2, TimeUnit.MINUTES));
                return;
            }
            if (event.getChannel().getId().equals(conf.getInitialChannel())) {
                var maybeTheirTickets = event.getGuild().getTextChannelsByName(event.getChannel().getName() + "-" + event.getMember().getUser().getId(), true);
                if (maybeTheirTickets.size() > 0) {
                    maybeTheirTickets.get(0).sendMessage(event.getAuthor().getAsMention() + ", please send messages here instead of the main channel. If you need a new ticket, mark this one as solved and send a message in " + event.getChannel().getAsMention() + " again.\nMessage: `" + event.getMessage().getContentRaw() + "`").queue();
                    event.getMessage().delete().queue();
                    return;
                }

                new Thread(() -> handleNew(event, conf)).start();
            }
        }
    }

    static void onReaction(GuildMessageReactionAddEvent event) {
        var conf = GuildConfig.get(event.getGuild());
        if (conf == null) return;
        if (event.getUser().isBot() || !conf.getOpenTickets().contains(event.getChannel())) return;
        if (event.getChannel().getMessageById(event.getMessageId())
                .complete().getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            if (event.getReaction().getReactionEmote().getName().equals(conf.getDefaultReaction())) {
                new Thread(() -> handleDelete(event)).start();
            }
            if (event.getReaction().getReactionEmote().getName().equals(EmojiUtil.MICROPHONE)) {
                new Thread(() -> handleVoiceChat(event)).start();
            }
        }
    }

    private static void handleVoiceChat(GuildMessageReactionAddEvent event) {
        var targetVc = event.getGuild().getVoiceChannelsByName(event.getChannel().getName(), true);
        if (targetVc.size() < 1) {
            event.getChannel().getParent().createVoiceChannel(event.getChannel().getName()).queue();
        }

    }

    private static void handleDelete(GuildMessageReactionAddEvent event) {
        var deleteEmbed = new EmbedBuilder()
                .setAuthor(event.getUser().getName(), event.getUser().getEffectiveAvatarUrl(), event.getUser().getEffectiveAvatarUrl())
                .setColor(event.getMember().getRoles().get(0).getColor())
                .setDescription("This ticket has been marked as solved by " + FormatUtil.getUserString(event.getUser()) + ". Closing the ticket in two minutes.")
                .setTitle("Ticket Marked as Solved");
        try {
            var targetVc = event.getGuild().getVoiceChannelsByName(event.getChannel().getName(), true).get(0);
            if (!ObjectUtil.isNull(targetVc)) {
                targetVc.delete().queue();
            }
        } catch (IndexOutOfBoundsException ignored) {
            //ignore the exception, it just means that the voice channel doesn't exist.
        }

        if (event.getMember().getRoles().stream().map(ISnowflake::getId).anyMatch(s ->
                Arrays.asList(GuildConfig.get(event.getGuild()).getRolesAllowed()).contains(s))) {
            event.getChannel().sendMessage(deleteEmbed.build()).queue();
            event.getChannel().delete().queueAfter(2, TimeUnit.MINUTES);
        }
    }

    private static void handleNew(GuildMessageReceivedEvent event, GuildConfig c) {
        String newChannelName = event.getChannel().getName() + "-" + event.getAuthor().getId();
        if (ObjectUtil.isNull(event.getChannel().getParent())) {
            event.getGuild().getController().createTextChannel(newChannelName).queue(ch -> onTicketCreate(event, c, (TextChannel)ch));
        } else {
            event.getChannel().getParent().createTextChannel(newChannelName).queue(ch -> onTicketCreate(event, c, (TextChannel)ch));
        }

    }

    private static void onTicketCreate(GuildMessageReceivedEvent event, GuildConfig c, TextChannel newTc) {
        event.getGuild().getController().modifyTextChannelPositions().selectPosition(newTc).moveTo(event.getChannel().getPosition() + 1).queue();

        newTc.createPermissionOverride(event.getMember()).setAllow(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).queue();

        for (var roleId : c.getRolesAllowed()) {
            var role = event.getGuild().getRoleById(roleId);
            if (ObjectUtil.isNull(role)) continue;
            newTc.createPermissionOverride(role).setAllow(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).queue();
        }

        var embed = new EmbedBuilder()
                .setColor(SupportBot.getBotConfig().getEmbedColour())
                .setDescription("**Author**: " + event.getAuthor().getAsMention() +
                        "\n**Message**: " + event.getMessage().getContentRaw() +
                        "\n\nClick the " + c.getDefaultReaction() + " reaction below to close this ticket, or click " + EmojiUtil.MICROPHONE + " to create a voice channel.")
                .setThumbnail(event.getAuthor().getEffectiveAvatarUrl());
        newTc.sendMessage(embed.build()).queue(m -> {
            m.addReaction(c.getDefaultReaction()).queue();
            m.addReaction(EmojiUtil.MICROPHONE).queue();
        });

        event.getMessage().delete().queue();
    }

}
