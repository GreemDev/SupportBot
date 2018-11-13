package net.greemdev.supportbot.events;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.greemdev.supportbot.objects.GuildConfig;
import net.greemdev.supportbot.util.ConfigUtil;

public class SupportChannelListener {

    public static void onMessage(GuildMessageReceivedEvent event) {
        if (ConfigUtil.getGuildConfigFile(event.getGuild().getId()).exists() &&
                GuildConfig.get(event.getGuild().getId()).getInitialChannel() != null) {

        }
    }

}
