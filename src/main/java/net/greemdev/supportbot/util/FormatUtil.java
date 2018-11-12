package net.greemdev.supportbot.util;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.SelfUser;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class FormatUtil {

    public static String getUserString(User u) {
        return u.getName() + "#" + u.getDiscriminator();
    }

    public static String getUserString(SelfUser u) {
        return u.getName() + "#" + u.getDiscriminator();
    }

        public static String getUserStringFromEvent(MessageReceivedEvent event) {
            return event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator();
        }
        public static String getChannelStringWithLink(TextChannel tc) {
            return "[" +
                    FormatUtil.getChannelString(tc) +
                    "](https://cdn.discordapp.com/channels/" +
                    tc.getGuild().getId() + "/" + tc.getId() +
                    ")";
        }
        public static String getUserStringFromEvent(CommandEvent event) {
            return event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator();
        }
        public static String getChannelStringFromEvent(CommandEvent event) {
            return "#" + event.getChannel().getName();
        }
        public static String getChannelStringFromEvent(MessageReceivedEvent event) {
            return "#" + event.getChannel().getName();
        }
        public static String getChannelString(TextChannel channel) {
            return "#" + channel.getName();
        }
        public static String getChannelString(VoiceChannel channel) {
            return channel.getName();
        }
        public static String getReactionLogString(String reactionName, MessageReceivedEvent event) {
            return "Reaction \"" + reactionName + "\" triggered by [" + FormatUtil.getUserStringFromEvent(event) +
                    "] in channel " + FormatUtil.getChannelStringFromEvent(event);
        }

}
