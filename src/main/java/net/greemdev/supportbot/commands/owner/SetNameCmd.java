package net.greemdev.supportbot.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.greemdev.supportbot.SupportBot;
import net.greemdev.supportbot.commands.Categories;

public class SetNameCmd extends Command {

    public SetNameCmd() {
        this.name = "setname";
        this.help = "Sets the bot's name. Owner only.";
        this.category = Categories.owner;
    }

    @Override
    protected void execute(CommandEvent event) {
        SupportBot.getJda().getSelfUser().getManager().setName(event.getArgs()).queue();
    }
}
