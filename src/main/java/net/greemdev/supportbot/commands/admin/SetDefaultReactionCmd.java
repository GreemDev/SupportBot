package net.greemdev.supportbot.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.greemdev.supportbot.commands.Categories;
import net.greemdev.supportbot.events.Handler;
import net.greemdev.supportbot.files.GuildConfig;
import net.greemdev.supportbot.util.ConfigUtil;

public class SetDefaultReactionCmd extends Command {

    public SetDefaultReactionCmd() {
        this.name = "setdefaultreaction";
        this.help = "Sets the reaction to close tickets.";
        this.aliases = new String[]{"sdr", "setreaction"};
        this.category = Categories.admin;
    }

    @Override
    protected void execute(CommandEvent event) {
        var conf = GuildConfig.get(event.getGuild());
        if (ConfigUtil.catchConfigNull(event)) {
            if (!event.getArgs().split(" ")[0].startsWith("\\") || !event.getArgs().startsWith("\\")) {
                event.reply("You didn't enter an emoji.");
                Handler.onCommand(event);
                return;
            }
            if (!event.getMessage().getEmotes().get(0).getName().startsWith("\\")) {
                event.reply("You entered a custom server emoji; I don't support those as of yet.");
                Handler.onCommand(event);
                return;
            }
            conf.setDefaultReaction(event.getMessage().getContentRaw().split(" ")[0]);
        }
        Handler.onCommand(event);
    }
}
