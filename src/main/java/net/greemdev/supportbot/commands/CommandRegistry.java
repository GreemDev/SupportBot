package net.greemdev.supportbot.commands;

import com.jagrosh.jdautilities.command.Command;
import net.greemdev.supportbot.commands.admin.*;
import net.greemdev.supportbot.commands.owner.*;

public class CommandRegistry {

    public static Command[] getAllCommands() {
        return new Command[] {
                eval,

                setMaxOpenTicketsCmd, setDefaultReactionCmd, setAllowedRolesCmd, setAuthorCanCloseCmd
        };
    }

    // Owner commands
    public static EvalCmd eval = new EvalCmd();

    //Admin commands
    public static SetMaxOpenTicketsCmd setMaxOpenTicketsCmd = new SetMaxOpenTicketsCmd();
    public static SetDefaultReactionCmd setDefaultReactionCmd = new SetDefaultReactionCmd();
    public static SetAllowedRolesCmd setAllowedRolesCmd = new SetAllowedRolesCmd();
    public static SetAuthorCanCloseCmd setAuthorCanCloseCmd = new SetAuthorCanCloseCmd();

    //General commands

}
