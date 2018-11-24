package net.greemdev.supportbot.commands;

import com.jagrosh.jdautilities.command.Command.Category;
import net.dv8tion.jda.core.Permission;
import net.greemdev.supportbot.SupportBot;

public class Categories {

    public static Category owner = new Category("Owner", event -> {
        if (!event.getAuthor().getId().equals(SupportBot.getClient().getOwnerId())) {
            event.getMessage().addReaction(event.getClient().getError()).queue();
            return false;
        }
        return true;
    });

    public static Category admin = new Category("Admin", event -> {
        if (event.getAuthor().getId().equals(event.getClient().getOwnerId())) return true;
        if (event.getGuild().equals(null)) return false;
        return event.getMember().hasPermission(Permission.MANAGE_SERVER)
                || event.getMember().hasPermission(Permission.ADMINISTRATOR);
    });

    public static Category general = new Category("General", event ->
            !event.getAuthor().isFake() || !event.getAuthor().isBot());

}
