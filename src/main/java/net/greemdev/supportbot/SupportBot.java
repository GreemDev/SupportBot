package net.greemdev.supportbot;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import lombok.Getter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.greemdev.supportbot.commands.CommandRegistry;
import net.greemdev.supportbot.events.Handler;
import net.greemdev.supportbot.objects.BotConfig;
import net.greemdev.supportbot.objects.GuildConfig;
import net.greemdev.supportbot.util.EmojiUtil;
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
    private static BotConfig config = BotConfig.get();
    public static BotConfig getBotConfig() {
        return config;
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
                .setPrefix(config.getCommandPrefix())
                .setStatus(OnlineStatus.ONLINE)
                .setGame(Game.playing(config.getGame()))
                .setOwnerId(config.getOwnerId())
                .setEmojis(EmojiUtil.BALLOT_BOX_WITH_CHECK, EmojiUtil.WARNING, EmojiUtil.X)
                .setHelpWord("help")
                .addCommands(
                        CommandRegistry.evalCommand
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
            getLogger().error("Failed to login. Is your token malformed, or in the config at all?");
            System.exit(1);
        }

    }

}
