package net.greemdev.supportbot.util;

import com.google.gson.GsonBuilder;
import net.greemdev.supportbot.objects.GuildConfig;
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
            guildIds.add(conf.equals(null) ? conf.getId() : null);
        }
        return guildIds;
    }

}
