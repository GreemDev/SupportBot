package net.greemdev.supportbot.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.greemdev.supportbot.commands.Categories;
import net.greemdev.supportbot.files.GuildConfig;
import net.greemdev.supportbot.util.ConfigUtil;
import net.greemdev.supportbot.util.ObjectUtil;

public class SetAuthorCanCloseCmd extends Command {

    public SetAuthorCanCloseCmd() {
        this.name = "setauthorcanclose";
        this.help = "Sets whether or not the author can close their own ticket in the current server.";
        this.category = Categories.admin;
        this.aliases = new String[]{"sacc", "setcanclose", "scc"};
    }

    @Override
    protected void execute(CommandEvent event) {
        if (ConfigUtil.catchConfigNull(event)) {
            var parsed = Boolean.parseBoolean(ObjectUtil.parseBoolean(event.getArgs()));
            if (ObjectUtil.parseBoolean(event.getArgs()) == null) {
                event.reply("You didn't enter a valid value. Valid values are: " +
                        "`To enable the setting: yes, enabled, yeah, true`" +
                        "\n" +
                        "`To disable the setting: no, disabled, nah, false`" +
                        "\n" +
                        "You must enter one of those values to properly enable/disable the setting.");
                return;
            }

            var conf = GuildConfig.get(event.getGuild());
            conf.setCanClose(parsed);
            event.reply("Set `authorCanClose` to " + parsed);
        }
    }
}
