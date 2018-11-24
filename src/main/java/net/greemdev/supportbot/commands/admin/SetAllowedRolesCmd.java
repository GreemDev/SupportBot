package net.greemdev.supportbot.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.ISnowflake;
import net.greemdev.supportbot.commands.Categories;
import net.greemdev.supportbot.events.Handler;
import net.greemdev.supportbot.files.GuildConfig;
import net.greemdev.supportbot.util.ConfigUtil;
import net.greemdev.supportbot.util.ParserUtil;

import java.util.Arrays;
import java.util.Objects;

public class SetAllowedRolesCmd extends Command {

    public SetAllowedRolesCmd() {
        this.name = "setallowedroles";
        this.help = "Sets the roles allowed to access tickets in the current server.";
        this.aliases = new String[]{"sar", "sr"};
        this.category = Categories.admin;
    }

    @Override
    protected void execute(CommandEvent event) {
        var rolesAllowed = new String[0];
        var conf = GuildConfig.get(event.getGuild());
        if (ConfigUtil.catchConfigNull(event)) {
            rolesAllowed = Arrays.stream(event.getArgs().split(","))
                    .map(s -> ParserUtil.getRoleByName(event.getGuild(), s.trim()))
                    .filter(Objects::nonNull)
                    .map(ISnowflake::getId)
                    .toArray(String[]::new);
            event.reply("Set `rolesAllowed` to " + event.getArgs());
            conf.setRolesAllowed(rolesAllowed).write();
        }
        Handler.onCommand(event);
    }
}

