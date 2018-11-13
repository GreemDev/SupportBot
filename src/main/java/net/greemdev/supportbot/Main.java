package net.greemdev.supportbot;

import net.greemdev.supportbot.objects.BotConfig;
import net.greemdev.supportbot.util.ConfigUtil;

public class Main {

    public static void main(String[] args) {

        if (args.length > 0) {
            SupportBot.getLogger().info(
                    "Your command-line arguments were ignored as this program doesn't accept/need any."
            );
        }

        if (!ConfigUtil.getBotConfigFile().exists()) {
            BotConfig.write();
            SupportBot.getLogger().error("Config didn't exist, so I created it for you. Fill in the file and restart to continue.");
            System.exit(1);
        }
        new SupportBot();
    }

}
