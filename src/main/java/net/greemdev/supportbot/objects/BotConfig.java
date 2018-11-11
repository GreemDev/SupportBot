package net.greemdev.supportbot.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.greemdev.supportbot.SupportBot;
import org.apache.commons.io.FileUtils;

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
    private List<String> blacklistedServerOwners;

    private BotConfig() {
        this.token = "your-token-here";
        this.commandPrefix = "your-prefix-here";
        this.game = "your-game-here";
        this.ownerId = "your-user-id-here";
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
    public List<String> getBlacklistedServerOwners() {
        return this.blacklistedServerOwners;
    }

    public static void write() {
        File dataFile = new File("data/config.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
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
