package net.greemdev.supportbot.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.greemdev.supportbot.commands.Categories;
import net.greemdev.supportbot.files.GuildConfig;
import net.greemdev.supportbot.events.Handler;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class EvalCmd extends Command {

    public EvalCmd() {
        this.name = "eval";
        this.help = "Executes JavaScript code.";
        this.category = Categories.owner;
    }

    @Override
    @SuppressWarnings("SpellCheckingInspection")
    protected void execute(CommandEvent event) {
        ScriptEngine se = new ScriptEngineManager().getEngineByName("js");
        se.put("jda", event.getJDA());
        se.put("self", event.getSelfUser());
        se.put("event", event);
        se.put("logger", LoggerFactory.getLogger(this.getClass()));
        se.put("config", GuildConfig.get(event.getGuild()));

        if (event.getArgs().equals("")) {
            var m = new EmbedBuilder()
                    .setColor(0x7000FB)
                    .setTitle("Available Eval Variables")
                    .addField("jda", "[interface net.dv8tion.jda.core.JDA](http://home.dv8tion.net:8080/job/JDA/javadoc/net/dv8tion/jda/core/JDA.html)", false)
                    .addField("self", "[interface net.dv8tion.jda.core.entities.SelfUser](http://home.dv8tion.net:8080/job/JDA/javadoc/net/dv8tion/jda/core/entities/SelfUser.html)", false)
                    .addField("event", "class com.jagrosh.jdautilities.command.CommandEvent", false)
                    .addField("logger", "interface org.slf4j.Logger", false)
                    .addField("config", "class net.greemdev.supportbot.files.GuildConfig", false);
            event.reply(m.build());
            return;
        }

        try {
            Object evalOutput = se.eval(event.getArgs());
            if (evalOutput != null) {
                event.reply(event.getClient().getSuccess() +
                        " Evaluated Successfully:\n```java\n" + evalOutput + " ```");
            } else {
                event.getMessage().addReaction(event.getClient().getSuccess()).queue();
            }
        } catch (Exception e) {
            event.reply(event.getClient().getError() + " An exception was thrown:\n```java\n" + e + "```");
        }

        Handler.onCommand(event);
    }
}
