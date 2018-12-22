package net.greemdev.supportbot.events;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.greemdev.supportbot.SupportBot;

class GenericListener {

    static void onReady(ReadyEvent event) {
        if (SupportBot.getBotConfig().getBlacklistedServerOwners().size() == 0) return;
        for (var guild : event.getJDA().getGuilds()) {
            if (SupportBot.getBotConfig().getBlacklistedServerOwners().contains(guild.getOwnerId())) {
                guild.leave().queue();
            }
        }
    }

    static void onGuildJoin(GuildJoinEvent event) {
        var e = new EmbedBuilder()
                .setColor(SupportBot.getBotConfig().getEmbedColour())
                .setTitle("Thanks for inviting me to your server!")
                .setDescription("To get started, send `setupsupport` into the channel you'd like to designate as your support start point.")
                .addField("Need more help?", "[Join the Support Guild!](https://discord.greem.me)", false)
                .setThumbnail(event.getGuild().getOwner().getUser().getEffectiveAvatarUrl());
        event.getGuild().getOwner().getUser().openPrivateChannel().queue(c ->
            c.sendMessage(e.build()).queue(null, err ->
                event.getGuild().getDefaultChannel().sendMessage(e.build())
            )
        );
    }

}
