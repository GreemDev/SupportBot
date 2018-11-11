package net.greemdev.supportbot;

import net.greemdev.supportbot.objects.BotConfig;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        if (args.length > 0) {
            SupportBot.getLogger().info(
                    "Your command-line arguments were ignored as this program doesn't accept/need any."
            );
        }

        if (!(new File("data/config.json").exists())) {
            BotConfig.write();
            SupportBot.getLogger().error("Config didn't exist, so I created it for you. Fill in the file and restart the bot to continue.");
            System.exit(1);
            return;
        }
        new SupportBot();
    }

}
