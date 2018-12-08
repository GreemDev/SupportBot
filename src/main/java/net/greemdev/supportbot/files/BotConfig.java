package net.greemdev.supportbot.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.greemdev.supportbot.SupportBot;
import net.greemdev.supportbot.files.objects.ConfigColour;
import net.greemdev.supportbot.util.ConfigUtil;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BotConfig {
    private String token;
    private String game;
    private String commandPrefix;
    private String ownerId;
    private ConfigColour embedColour;
    private boolean logCommands;
    private List<String> blacklistedServerOwners;

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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File f = new File("data/config.json");
        Scanner sc;
        try {
            sc = new Scanner(f);
        } catch (FileNotFoundException e) {
            SupportBot.getLogger().error("Couldn't find the bot config!");
            e.printStackTrace();
            return null;
        }
        sc.useDelimiter("\\Z");
        return gson.fromJson(sc.next(), BotConfig.class);
    }
}
