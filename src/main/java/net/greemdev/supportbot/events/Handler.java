package net.greemdev.supportbot.events;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.greemdev.supportbot.SupportBot;
import net.greemdev.supportbot.util.ConfigUtil;
import net.greemdev.supportbot.util.FormatUtil;
import org.slf4j.Logger;

import java.util.Date;

public class Handler extends ListenerAdapter {

    private static Logger logger = SupportBot.getLogger();

    public static void onCommand(CommandEvent event) {
        if (!SupportBot.getBotConfig().getLogCommands()) return;
        logger.info("--| Command from User: " + FormatUtil.getUserString(event.getAuthor()));
        logger.info("--|  Command Executed: " +
                event.getMessage().getContentRaw().split(" ")[0].replace(SupportBot.getClient().getPrefix(), ""));
        logger.info("--|             Guild: " + event.getGuild().getName());
        logger.info("--|           Channel: " + FormatUtil.getChannelStringFromEvent(event));
        logger.info("--|              Time: " + new Date().toString());
        logger.info("-------------------------------------------------");
    }

    @Override
    public void onReady(ReadyEvent event) {
        var pluralGuilds = (event.getJDA().getGuilds().size() != 1) ? " guilds." : " guild.";
        var pluralUsers = (event.getJDA().getUsers().size() != 1) ? " users." : " user.";
        logger.info("");
        logger.info("      #####                                           ######               ");
        logger.info("     #     # #    # #####  #####   ####  #####  ##### #     #  ####  ##### ");
        logger.info("     #       #    # #    # #    # #    # #    #   #   #     # #    #   #   ");
        logger.info("      #####  #    # #    # #    # #    # #    #   #   ######  #    #   #   ");
        logger.info("           # #    # #####  #####  #    # #####    #   #     # #    #   #   ");
        logger.info("     #     # #    # #      #      #    # #   #    #   #     # #    #   #   ");
        logger.info("      #####   ####  #      #       ####  #    #   #   ######   ####    #   ");
        logger.info("");
        logger.info("       Logged in as " + FormatUtil.getUserString(event.getJDA().getSelfUser()) + ".");
        logger.info("       Connected to " + event.getJDA().getGuilds().size() + pluralGuilds);
        logger.info("       Connected to " + event.getJDA().getUsers().size() + pluralUsers);
        this.printSupportInfo();
        logger.info("| Now accepting commands.");
        ConfigUtil.parseGame(event.getJDA());

        GenericListener.onReady(event);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        SetupListener.onMessage(event);
        SupportChannelListener.onMessage(event);
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        SupportChannelListener.onReaction(event);
    }

    private void printSupportInfo() {
        int guilds = ConfigUtil.getSetupGuilds().size();
        SupportBot.getLogger().info("Currently, " + guilds +
                ((guilds != 1) ? " guilds are" : " guild is") + " setup.");
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        GenericListener.onGuildJoin(event);
    }
}
