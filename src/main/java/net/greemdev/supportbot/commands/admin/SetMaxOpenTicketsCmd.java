package net.greemdev.supportbot.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.greemdev.supportbot.commands.Categories;
import net.greemdev.supportbot.files.GuildConfig;
import net.greemdev.supportbot.util.ConfigUtil;

public class SetMaxOpenTicketsCmd extends Command {

    public SetMaxOpenTicketsCmd() {
        this.name = "setmaxopentickets";
        this.aliases = new String[]{"smot"};
        this.category = Categories.admin;
    }

    @Override
    protected void execute(CommandEvent event) {
        var conf = GuildConfig.get(event.getGuild());
        if (ConfigUtil.catchConfigNull(event)) {
            try {
                conf.setMaxOpen(Integer.parseInt(event.getArgs()));
                conf.write();
            } catch (NumberFormatException e) {
                event.reply("You must enter a number; nothing else. You entered " + event.getArgs());
                return;
            }
            event.reply("Set the maximum open tickets for this server to " + event.getArgs());
        }


    }
}
