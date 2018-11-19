package net.greemdev.supportbot.util;

import com.google.gson.GsonBuilder;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.Game;
import net.greemdev.supportbot.SupportBot;
import net.greemdev.supportbot.files.GuildConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ConfigUtil {

    public static File getGuildConfigFile(String id) {
        return new File("data/guilds/" + id + ".json");
    }

    public static File getBotConfigFile() {
        return new File("data/config.json");
    }

    public static ArrayList<String> getSetupGuilds() {
        var gson = new GsonBuilder().setPrettyPrinting().create();
        var guildIds = new ArrayList<String>();
        GuildConfig conf;

        for (File f : new File("data/guilds").listFiles()) {
            try {
                conf = gson.fromJson(FileUtils.readFileToString(f, Charset.forName("UTF8")), GuildConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            guildIds.add(ObjectUtil.isNull(conf) ? conf.getId() : null);
        }
        return guildIds;
    }

    public static void parseGame() {
        var game = SupportBot.getBotConfig().getGame();
        var lowercaseArray = game.toLowerCase().split(" ");
        var activity = game.replace(game.split(" ")[0], "").trim();
        switch (lowercaseArray[0]) {
            case "playing": {
                SupportBot.getJda().getPresence().setGame(Game.playing(activity));
                SupportBot.getLogger().info("Set the game to \"Playing " + activity + "\"");
                break;
            }
            case "listeningto": {
                SupportBot.getJda().getPresence().setGame(Game.listening(activity));
                SupportBot.getLogger().info("Set the game to \"Listening to " + activity + "\"");
                break;
            }
            case "watching": {
                SupportBot.getJda().getPresence().setGame(Game.watching(activity));
                SupportBot.getLogger().info("Set the game to \"Watching " + activity + "\"");
                break;
            }
            default: {
                SupportBot.getJda().getPresence().setGame(Game.playing(activity));
                SupportBot.getLogger().warn("Your game wasn't set properly. " +
                        "You entered the activity as " + lowercaseArray[0] +
                        "instead of a valid activity: Playing, Listeningto, or Watching.");
                SupportBot.getLogger().warn("Your bot's game has been set to \"" + activity + "\"");
                break;
            }
        }
    }

    public static boolean catchConfigNull(CommandEvent event) {
        var exists = ConfigUtil.getGuildConfigFile(event.getGuild().getId()).exists();
        if (!exists) {
            event.reply("You haven't setup this server for support tickets. " +
                    "Send `setupsupport` in your desired start channel and try again after setup.");
            return false;
        }
        return exists;
    }

}
