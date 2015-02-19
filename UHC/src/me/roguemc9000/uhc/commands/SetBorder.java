package me.roguemc9000.uhc.commands;

import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.utilities.GameState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Code written by Nico Bentley.
 * Raw code not to be sold, redistributed,
 * or edited by anyone else without written consent
 * from Nico Bentley.
 * <p/>
 * (C) All Rights Reserved
 */
public class SetBorder implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()) {
            if ((Core.getState() == GameState.LOBBY)) {
                if (args.length == 1) {
                    Integer size = 0;
                    try {
                        size = Integer.parseInt(args[0]);
                    } catch (Exception e) {
                        sender.sendMessage(Core.getPrefix() + ChatColor.RED + "Not a valid number!");
                    }
                    if (size == 0) sender.sendMessage(Core.getPrefix() + ChatColor.RED + "Not a valid number!");
                    else {
                        Core.setBorder(size);
                        sender.sendMessage(Core.getPrefix() + "Border has been set to " + size + " blocks!");
                    }
                } else sender.sendMessage(Core.getPrefix() + ChatColor.RED + "You need to set a border size!");
            } else sender.sendMessage(Core.getPrefix() + ChatColor.RED + "The game is already running!");
        } else sender.sendMessage(Core.getPrefix() + "Insufficient permission!");
        return true;
    }
}
