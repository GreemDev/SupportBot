package net.greemdev.supportbot.commands;

import net.greemdev.supportbot.commands.admin.SetAllowedRolesCmd;
import net.greemdev.supportbot.commands.admin.SetDefaultReactionCmd;
import net.greemdev.supportbot.commands.admin.SetMaxOpenTicketsCmd;
import net.greemdev.supportbot.commands.owner.EvalCommand;

public class CommandRegistry {

    // Owner commands
    public static EvalCommand eval = new EvalCommand();

    //Admin commands
    public static SetMaxOpenTicketsCmd setMaxOpenTickets = new SetMaxOpenTicketsCmd();
    public static SetDefaultReactionCmd setDefaultReaction = new SetDefaultReactionCmd();
    public static SetAllowedRolesCmd setAllowedRolesCmd = new SetAllowedRolesCmd();

}
