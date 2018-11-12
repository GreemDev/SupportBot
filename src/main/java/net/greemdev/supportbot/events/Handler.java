package net.greemdev.supportbot.events;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.greemdev.supportbot.SupportBot;
import net.greemdev.supportbot.util.FormatUtil;
import org.slf4j.Logger;

public class Handler extends ListenerAdapter {

    private Logger logger = SupportBot.getLogger();

    @Override
    public void onReady(ReadyEvent event) {
        this.logger.info("");
        this.logger.info("      #####                                           ######               ");
        this.logger.info("     #     # #    # #####  #####   ####  #####  ##### #     #  ####  ##### ");
        this.logger.info("     #       #    # #    # #    # #    # #    #   #   #     # #    #   #   ");
        this.logger.info("      #####  #    # #    # #    # #    # #    #   #   ######  #    #   #   ");
        this.logger.info("           # #    # #####  #####  #    # #####    #   #     # #    #   #   ");
        this.logger.info("     #     # #    # #      #      #    # #   #    #   #     # #    #   #   ");
        this.logger.info("      #####   ####  #      #       ####  #    #   #   ######   ####    #   ");
        this.logger.info("");
        this.logger.info("       Logged in as " + FormatUtil.getUserString(event.getJDA().getSelfUser()) + ".");
        this.logger.info("       Connected to " + event.getJDA().getGuilds().size() + " guilds.");
        this.logger.info("       Connected to " + event.getJDA().getUsers().size() + " users.");
        this.logger.info("| Now accepting commands.");

        GenericListener.onReady(event);
    }
}
