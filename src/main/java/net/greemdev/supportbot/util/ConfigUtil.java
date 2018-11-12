package net.greemdev.supportbot.util;

import java.io.File;

public class ConfigUtil {

    public static File getGuildConfigFile(String id) {
        return new File("data/guilds/" + id + ".json");
    }

    public static File getBotConfigFile() {
        return new File("data/config.json");
    }

}
