package me.roguemc9000.uhc.commands;

import me.roguemc9000.uhc.Core;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Code written by Nico Bentley.
 * Raw code not to be sold, redistributed,
 * or edited by anyone else without written consent
 * from Nico Bentley.
 * <p/>
 * (C) All Rights Reserved
 */
public class Heal implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()) {
            Player p = null;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().equalsIgnoreCase(args[0])) {
                    p = player;
                    break;
                }
            }
            if (p == null) {
                sender.sendMessage(Core.getPrefix() + "Cannot find that player!");
                return true;
            } else {
                p.setHealth(20);
                sender.sendMessage(Core.getPrefix() + p.getName() + " has been healed!");
                p.sendMessage(Core.getPrefix() + "You were healed by " + sender.getName());
                return true;
            }
        } else return true;
    }

}
