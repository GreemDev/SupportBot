package net.greemdev.supportbot.commands.admin;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.greemdev.supportbot.commands.Categories;
import net.greemdev.supportbot.events.Handler;
import net.greemdev.supportbot.files.GuildConfig;
import net.greemdev.supportbot.util.ConfigUtil;
import com.vdurmont.emoji.EmojiManager;

public class SetDefaultReactionCmd extends Command {

    public SetDefaultReactionCmd() {
        this.name = "setdefaultreaction";
        this.help = "Sets the reaction to close tickets.";
        this.aliases = new String[]{"sdr", "setreaction"};
        this.category = Categories.admin;
    }

    @Override
    protected void execute(CommandEvent event) {
        Handler.onCommand(event);
        if (ConfigUtil.catchConfigNull(event)) {
            if (event.getArgs().split(" ").length > 0) {
                if (!EmojiManager.isEmoji(event.getArgs().split(" ")[0])) {
                    event.reply("You didn't enter an emoji.");
                    return;
                }
            }
            if (!EmojiManager.isEmoji(event.getArgs().split(" ")[0]) || !EmojiManager.isEmoji(event.getArgs())) {
                event.reply("You didn't enter an emoji.");
                return;
            }
            if (event.getMessage().getEmotes().size() > 0) {
                event.reply("You entered a custom server emoji; I don't support those as of yet.");
                return;
            }
            GuildConfig.get(event.getGuild()).setDefaultReaction(event.getArgs()).write();
            event.reply("Set `defaultReaction` to " + event.getArgs());
        }
    }
}
