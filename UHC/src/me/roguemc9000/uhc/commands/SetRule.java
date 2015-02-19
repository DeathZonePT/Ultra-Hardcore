package me.roguemc9000.uhc.commands;

import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.utilities.Feature;
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
public class SetRule implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()) {
            if (Core.getState() == GameState.LOBBY) {
                if (args.length == 2) {
                    Feature feature = Core.getFeature(args[0]);
                    if (feature != null) {
                        if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")) {
                            Boolean set = Boolean.parseBoolean(args[1]);
                            feature.setEnabled(set);
                            sender.sendMessage(Core.getPrefix() + feature.getName() + " is now: " + ChatColor.RED +
                                    ChatColor.BOLD + set.toString().toUpperCase());
                        } else sender.sendMessage(Core.getPrefix() + "Correct Usage: /setrule [rule] [true/false]");
                    } else sender.sendMessage(Core.getPrefix() + "Cannot find that feature!");
                } else sender.sendMessage(Core.getPrefix() + "Correct Usage: /setrule [rule] [true/false]");
            } else sender.sendMessage(Core.getPrefix() + "Cannot change game rules midgame!");
        } else sender.sendMessage(Core.getPrefix() + "Insufficient permission!");
        return true;
    }

}
