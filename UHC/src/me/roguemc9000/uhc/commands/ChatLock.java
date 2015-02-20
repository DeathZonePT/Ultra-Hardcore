package me.roguemc9000.uhc.commands;

import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.listeners.UtilityListeners;
import org.bukkit.Bukkit;
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
public class ChatLock implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()) {
            if (UtilityListeners.locked) {
                UtilityListeners.locked = false;
                Bukkit.broadcastMessage(Core.getPrefix() + "Chat has been unlocked!");
            } else {
                UtilityListeners.locked = true;
                Bukkit.broadcastMessage(Core.getPrefix() + "Chat has been locked!");
            }
        } else sender.sendMessage(Core.getPrefix() + "Insufficient permissions!");
        return true;
    }

}
