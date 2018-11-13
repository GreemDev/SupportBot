package net.greemdev.supportbot.util;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.greemdev.supportbot.SupportBot;
import org.apache.commons.lang3.StringUtils;

public class ParserUtil {

    public static Event getEvent(Class<? extends Event> eventClass) {
        final var events = new Event[]{null};
        var adapter = new ListenerAdapter() {
            @Override
            public void onGenericEvent(Event event) {
                if (event.getClass().getSimpleName().equals(eventClass.getSimpleName())) {
                    events[0] = event;
                }
            }
        };
        SupportBot.getJda().addEventListener(adapter);

        while (events[0] == null) try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SupportBot.getJda().removeEventListener(adapter);
        return events[0];

    }

    public static GuildMessageReceivedEvent getGuildMessageReceived(User u) {
        while (true) {
            var event = (GuildMessageReceivedEvent)getEvent(GuildMessageReceivedEvent.class);
            if (event.getMember().equals(null) || event.getMember().getUser().isBot()) continue;
            if (event.getMember().getUser().equals(u)) {
                return event;
            }
        }
    }

    public static GuildMessageReactionAddEvent getGuildMessageReactionAddEvent(User u) {
        return getReactionEvent(u);
    }

    public static GuildMessageReactionAddEvent getGuildMessageReactionAddEvent(Message m, User u) {
        m.addReaction(SupportBot.getClient().getSuccess()).queue();
        return getReactionEvent(u);
    }

    private static GuildMessageReactionAddEvent getReactionEvent(User u) {
        while (true) {
            var event = (GuildMessageReactionAddEvent) getEvent(GuildMessageReactionAddEvent.class);
            if (event.getMember().equals(null) || event.getUser().isBot()) continue;
            if (event.getUser().equals(u)) {
                return event;
            }
        }
    }

    public static int getInt(TextChannel tc, User u) {
        while (true) {
            var event = getGuildMessageReceived(u);
            if (StringUtils.isNumeric(event.getMessage().getContentRaw())) {
                return Integer.parseInt(event.getMessage().getContentRaw());
            } else {
                tc.sendMessage("Your message must be a number.").queue();
            }
        }
    }

    public static Role getRoleByName(Guild g, String r) {
        for (var role : g.getRoles()) {
            if (role.getName().equalsIgnoreCase(r)) {
                return role;
            }
        }
        return null;
    }

    public static boolean getYesNo(Message m, User u) {
        m.addReaction(SupportBot.getClient().getSuccess()).queue();
        m.addReaction(SupportBot.getClient().getError()).queue();

        while (true) {
            var event = getGuildMessageReactionAddEvent(u);
            var emoji = event.getReactionEmote().getName();
            if (!event.getMessageId().equals(m.getId()) || (
                    !emoji.equals(SupportBot.getClient().getSuccess())
                            && !emoji.equals(SupportBot.getClient().getError())
            )) continue;
            return emoji.equals(SupportBot.getClient().getSuccess());

        }

    }
}
