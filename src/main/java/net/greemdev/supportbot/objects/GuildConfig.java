package net.greemdev.supportbot.objects;

import com.google.gson.*;
import net.greemdev.supportbot.SupportBot;
import net.greemdev.supportbot.util.ConfigUtil;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public @Nullable class GuildConfig {

    private boolean authorCanClose;
    private String defaultReaction;
    private String initialChannel;
    private String[] rolesAllowedToClose;
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
    public String[] getRolesAllowedToClose() {
        return this.rolesAllowedToClose;
    }
    public String getId() {
        return this.id;
    }
    public int getMaxOpen() {
        return this.maxOpen;
    }

    public GuildConfig(boolean authorCanClose, String defaultReaction, String initialChannel, String[] rolesAllowedToClose, String id, int maxOpen) {
        this.authorCanClose = authorCanClose;
        this.defaultReaction = defaultReaction;
        this.id = id;
        this.initialChannel = initialChannel;
        this.maxOpen = maxOpen;
        this.rolesAllowedToClose = rolesAllowedToClose;
    }

    public void write() {
        var file = ConfigUtil.getGuildConfigFile(this.id);
        var gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileUtils.write(file, gson.toJson(this), Charset.forName("UTF8"));
        } catch(IOException e) {
            e.printStackTrace();
            SupportBot.getLogger().error("Couldn't write the data to the file " + file.getAbsolutePath() + "!");
        }

    }

    public static GuildConfig get(String guildId) {
        var gson = new GsonBuilder().setPrettyPrinting().create();
        var f = ConfigUtil.getGuildConfigFile(guildId);
        try {
            return gson.fromJson(FileUtils.readFileToString(f, Charset.forName("UTF-8")), GuildConfig.class);
        } catch (IOException e) {
            SupportBot.getLogger().error("Couldn't find the config for guild " + guildId + "!");
            e.printStackTrace();
            return null;
        }
    }

}
