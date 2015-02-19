package me.roguemc9000.uhc.commands;

import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.utilities.Feature;
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
public class Rules implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage(Core.getPrefix() + "Now displaying the current rules:");
        for (Feature f : Core.getFeatures())
            sender.sendMessage("    " + ChatColor.DARK_GRAY + f.getName() + ": " + ChatColor.RED + "" + ChatColor.BOLD +
                    f.isEnabled().toString().toUpperCase());
        return true;
    }

}
