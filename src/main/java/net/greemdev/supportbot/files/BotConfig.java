package net.greemdev.supportbot.files;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.greemdev.supportbot.SupportBot;
import net.greemdev.supportbot.files.objects.ConfigColour;
import net.greemdev.supportbot.util.ConfigUtil;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BotConfig {
    @Getter private String token;
    @Getter private String game;
    @Getter private String commandPrefix;
    @Getter private String ownerId;
    @Getter private ConfigColour embedColour;
    @Getter private boolean logCommands;
    @Getter private List<String> blacklistedServerOwners;

    private BotConfig() {
        this.token = "your-token-here";
        this.commandPrefix = "your-prefix-here";
        this.game = "your-game-here";
        this.ownerId = "your-user-id-here";
        this.embedColour = new ConfigColour(112, 0, 251); //default, equivalent to 0x7000FB hex.
        this.logCommands = false;
        this.blacklistedServerOwners = Arrays.asList("user id 1", "user id 2", "...");
    }

    public String getToken() {
        return this.token;
    }
    public String getGame() {
        return this.game;
    }
    public String getCommandPrefix() {
        return this.commandPrefix;
    }
    public String getOwnerId() {
        return this.ownerId;
    }
    public Color getEmbedColour() {
        return this.embedColour.getAsColor();
    }
    public boolean getLogCommands() {
        return this.logCommands;
    }
    public List<String> getBlacklistedServerOwners() {
        return this.blacklistedServerOwners;
    }

    public static void write() {
        var dataFile = ConfigUtil.getBotConfigFile();
        var gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileUtils.write(dataFile, gson.toJson(new BotConfig()), Charset.forName("UTF8"));
        } catch(IOException e) {
            e.printStackTrace();
            SupportBot.getLogger().error("Couldn't write the config to the file " + dataFile.getAbsolutePath() + "!");
        }

    }

    public static BotConfig get() {
        var gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            return gson.fromJson(
                    FileUtils.readFileToString(ConfigUtil.getBotConfigFile(), Charset.forName("UTF-8")),
                    BotConfig.class);
        } catch (IOException e) {
            SupportBot.getLogger().error("Couldn't find the bot config, or failed to load it!");
            e.printStackTrace();
            return null;
        }
    }
}
