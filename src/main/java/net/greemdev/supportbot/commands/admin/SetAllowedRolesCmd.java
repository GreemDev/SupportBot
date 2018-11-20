package net.greemdev.supportbot.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.greemdev.supportbot.commands.Categories;
import net.greemdev.supportbot.events.Handler;
import net.greemdev.supportbot.files.GuildConfig;
import net.greemdev.supportbot.util.ConfigUtil;
import net.greemdev.supportbot.util.ObjectUtil;
import net.greemdev.supportbot.util.ParserUtil;

import java.util.ArrayList;

@Deprecated // for now. no clue why this just refuses to write to the config file. will fix for V1 release.
public class SetAllowedRolesCmd extends Command {

    public SetAllowedRolesCmd() {
        this.name = "setallowedroles";
        this.help = "Sets the roles allowed to access tickets in the current server.";
        this.aliases = new String[]{"sar", "sr"};
        this.category = Categories.admin;
    }

    @Override
    protected void execute(CommandEvent event) {
        var rolesAllowed = new ArrayList<String>();
        var conf = GuildConfig.get(event.getGuild());
        if (ConfigUtil.catchConfigNull(event)) {
            for (var roleName : event.getArgs().split(",")) {
                var role = ParserUtil.getRoleByName(event.getGuild(), roleName);
                if (ObjectUtil.isNull(role)) {
                    continue;
                }
                rolesAllowed.add(role.getId());
            }
            conf.setRolesAllowed(rolesAllowed.toArray(new String[]{})).write();
            event.reply("Set `" + event.getArgs() + "` as the roles allowed to access support tickets.");
        }
        Handler.onCommand(event);
    }
}
