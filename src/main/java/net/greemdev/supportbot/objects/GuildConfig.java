package net.greemdev.supportbot.objects;

import com.google.gson.*;
import net.greemdev.supportbot.SupportBot;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public @Nullable class GuildConfig {

    private boolean authorCanClose;
    private String defaultReaction;
    private String initialChannel;
    private List<String> rolesAllowedToClose;
    private String id;
    private int maxOpen;

    public boolean getCanClose() {
        return this.authorCanClose;
    }
    public String getDefaultReaction() {
        return this.defaultReaction;
    }
    public String getInitialChannel() {
        return this.initialChannel;
    }
    public List<String> getRolesAllowedToClose() {
        return this.rolesAllowedToClose;
    }
    public String getId() {
        return this.id;
    }
    public int getMaxOpen() {
        return this.maxOpen;
    }

    public GuildConfig(boolean authorCanClose, String defaultReaction, String initialChannel, String rolesAllowedToClose, String id, int maxOpen) {
        this.authorCanClose = authorCanClose;
        this.defaultReaction = defaultReaction;
        this.id = id;
        this.initialChannel = initialChannel;
        this.maxOpen = maxOpen;
        this.rolesAllowedToClose = Arrays.asList(rolesAllowedToClose.split(","));
    }

    public void writeData() {
        File dataFile = new File("data/guilds/" + this.id + ".json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileUtils.write(dataFile, gson.toJson(this), Charset.forName("UTF8"));
        } catch(IOException e) {
            e.printStackTrace();
            SupportBot.getLogger().error("Couldn't write the data to the file " + dataFile.getAbsolutePath() + "!");
        }

    }

    public static GuildConfig getData(String guildId) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File f = new File("data/guilds/" + guildId + ".json");
        Scanner sc;
        try {
            sc = new Scanner(f);
        } catch (FileNotFoundException e) {
            SupportBot.getLogger().error("Couldn't find the config for guild " + guildId + "!");
            e.printStackTrace();
            return null;
        }
        sc.useDelimiter("\\Z");
        return gson.fromJson(sc.next(), GuildConfig.class);
    }

}
