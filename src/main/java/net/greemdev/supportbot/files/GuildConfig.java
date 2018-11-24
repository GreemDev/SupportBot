package net.greemdev.supportbot.files;

import com.google.gson.GsonBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.greemdev.supportbot.SupportBot;
import net.greemdev.supportbot.util.ConfigUtil;
import net.greemdev.supportbot.util.ObjectUtil;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class GuildConfig {

    private boolean authorCanClose;
    private String supportChannelName;
    private String defaultReaction;
    private String initialChannel;
    private String[] rolesAllowed;
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
    public String[] getRolesAllowed() {
        return this.rolesAllowed;
    }
    public String getId() {
        return this.id;
    }
    public int getMaxOpen() {
        return this.maxOpen;
    }
    public GuildConfig setCanClose(boolean canClose) {
        this.authorCanClose = canClose;
        return this;
    }
    public GuildConfig setDefaultReaction(String emote) {
        this.defaultReaction = emote;
        return this;
    }
    public GuildConfig setInitialChannel(String id) {
        this.initialChannel = id;
        return this;
    }
    public GuildConfig setRolesAllowed(String[] roles) {
        this.rolesAllowed = roles;
        return this;
    }
    public GuildConfig setMaxOpen(int maxOpen) {
        this.maxOpen = maxOpen;
        return this;
    }

    public GuildConfig(boolean authorCanClose, String supportChannelName, String defaultReaction, String initialChannel, String[] rolesAllowed, String id, int maxOpen) {
            this.setCanClose(authorCanClose)
                .setRolesAllowed(rolesAllowed)
                .setMaxOpen(maxOpen)
                .setDefaultReaction(defaultReaction);
            this.supportChannelName = supportChannelName;
            this.initialChannel = initialChannel;
            this.id = id;
    }

    public void write() {
        var file = ConfigUtil.getGuildConfigFile(this.id);
        var gson = new GsonBuilder().setPrettyPrinting().create();
        if (file.exists()) file.delete();
        try {
            FileUtils.writeStringToFile(file, gson.toJson(this), Charset.forName("UTF8"));
        } catch (IOException e) {
            e.printStackTrace();
            SupportBot.getLogger().error("Couldn't write the data to the file " + file.getAbsolutePath() + "!");
        }

    }

    public static GuildConfig create(boolean canClose, String supportChannelName, String defaultReaction, String initialChannel, String[] rolesAllowed, String id, int maxOpen) throws FileExistsException {
        if (ConfigUtil.getSetupGuilds().contains(id))
            throw new FileExistsException("The file " + ConfigUtil.getGuildConfigFile(id).getAbsolutePath() + " already exists.");
        return new GuildConfig(canClose, supportChannelName, defaultReaction, initialChannel, rolesAllowed, id, maxOpen);
    }

    public static GuildConfig get(Guild g) {
        return get(g.getId());
    }

    public static GuildConfig get(String guildId) {
        var gson = new GsonBuilder().setPrettyPrinting().create();
        var f = ConfigUtil.getGuildConfigFile(guildId);
        try {
            if (!ConfigUtil.getGuildConfigFile(guildId).exists()) { return null; }
            return gson.fromJson(FileUtils.readFileToString(f, Charset.forName("UTF-8")), GuildConfig.class);
        } catch (IOException e) {
            SupportBot.getLogger().error("Couldn't find the config for guild " + guildId + "!");
            e.printStackTrace();
            return null;
        }
    }

    public List<TextChannel> getOpenTickets() {
        ArrayList<TextChannel> tcList = new ArrayList<>();
        if (ObjectUtil.isNull(get(this.id))) return new ArrayList<>();
        for (var tc : SupportBot.getJda().getGuildById(this.id).getTextChannels()) {
            if (tc.getName().startsWith(this.supportChannelName) &&
                    StringUtils.isNumeric(StringUtils.removeStart(tc.getName(), this.supportChannelName + "-"))) {
                tcList.add(tc);
            }
        }
        return tcList;
    }

}
