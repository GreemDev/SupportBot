package net.greemdev.supportbot;

import com.jagrosh.jdautilities.command.*;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import net.greemdev.supportbot.commands.CommandRegistry;
import net.greemdev.supportbot.events.Handler;
import net.greemdev.supportbot.files.BotConfig;
import net.greemdev.supportbot.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class SupportBot {
    /**
     *  Gets a logger for a specific class.
     * @param clazz Class to get the {@link org.slf4j.Logger} for.
     * @return {@link org.slf4j.Logger} logger
     */
    public static Logger getLogger(Class clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static Logger getLogger() {
        return LoggerFactory.getLogger(SupportBot.class);
    }
    public static JDA getJda() {
        return jda;
    }
    private static JDA jda;
    private static CommandClient c;
    public static BotConfig getBotConfig() {
        return BotConfig.get();
    }
    public static CommandClient getClient() {
        return c;
    }

    public SupportBot() {
        this.start();
    }

    private void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> jda.shutdown()));
        c = new CommandClientBuilder()
                .setPrefix(getBotConfig().getCommandPrefix())
                .setStatus(OnlineStatus.ONLINE)
                .setGame(Game.playing("initializing..."))
                .setOwnerId(getBotConfig().getOwnerId())
                .setEmojis(EmojiUtil.BALLOT_BOX_WITH_CHECK, EmojiUtil.WARNING, EmojiUtil.X)
                .setHelpWord("help")
                .addCommands(
                        CommandRegistry.eval,
                        CommandRegistry.setMaxOpenTickets,
                        CommandRegistry.setDefaultReaction
                )
                .setLinkedCacheSize(200)
                .build();

        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(BotConfig.get().getToken())
                    .setAudioEnabled(false)
                    .addEventListener(new Handler(), c, new EventWaiter())
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
            getLogger().error("Failed to login.");
            System.exit(1);
        }
        ConfigUtil.parseGame();
    }

}
