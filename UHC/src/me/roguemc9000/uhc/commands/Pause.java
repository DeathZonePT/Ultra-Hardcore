package me.roguemc9000.uhc.commands;

import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.utilities.GameState;
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
public class Pause implements CommandExecutor {

    public static boolean paused = false;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()) {
            if (Core.getState() == GameState.INGAME || Core.getState() == GameState.GRACE) {
                if (paused) {
                    paused = false;
                    Bukkit.broadcastMessage(Core.getPrefix() + "The game has been resumed by " + sender.getName() +
                            "!");
                } else {
                    paused = true;
                    Bukkit.broadcastMessage(Core.getPrefix() + "The game has been paused by " + sender.getName() + "!");
                }
            } else sender.sendMessage(Core.getPrefix() + "The game isn't running!");
        } else sender.sendMessage(Core.getPrefix() + "Insufficient permission!");
        return true;
    }
}
