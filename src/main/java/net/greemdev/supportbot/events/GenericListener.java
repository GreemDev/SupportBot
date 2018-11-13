package net.greemdev.supportbot.events;

import net.dv8tion.jda.core.events.ReadyEvent;
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

}
