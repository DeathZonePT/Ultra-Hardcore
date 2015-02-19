package me.roguemc9000.uhc.commands;

import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.utilities.GameState;
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
public class Donate implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Core.getFeature("health donor").isEnabled()) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 2) {
                    Player pl = null;
                    for (Player player : Bukkit.getOnlinePlayers())
                        if (player.getName().equalsIgnoreCase(args[0])) pl = player;
                    if (pl != null) {
                        Double d = Double.parseDouble(args[1]);
                        if (d != null) {
                            if (Core.getFeature("teaming").isEnabled()) {
                                if (Core.getState() == GameState.GRACE || Core.getState() == GameState.INGAME) {
                                    if (Core.getTeam(p) == Core.getTeam(pl)) {
                                        if ((p.getHealth() / 2) - d > 0) {
                                            if ((pl.getHealth() / 2) + d > 10) {
                                                p.sendMessage(Core.getPrefix() + "You have donated " + d + " hearts " +
                                                        "to " +
                                                        pl.getName() + "!");
                                                pl.sendMessage(Core.getPrefix() + p.getName() + " has donated " + d +
                                                        " hearts to you!");
                                                p.setHealth(p.getHealth() - (d * 2));
                                                pl.setHealthScale(pl.getHealth() + (d * 2));
                                            } else p.sendMessage(Core.getPrefix() + "That gives " + pl.getName() +
                                                    " more than 10 hearts!");
                                        } else p.sendMessage(Core.getPrefix() + "You don't have that much health!");
                                    } else p.sendMessage(Core.getPrefix() + "That player isn\'t on your team!");
                                } else
                                    p.sendMessage(Core.getPrefix() + "You can only donate health while the game is " +
                                            "running!");
                            } else
                                p.sendMessage(Core.getPrefix() + "You can only donate health if teaming is enabled!");
                        } else p.sendMessage(Core.getPrefix() + "Invalid number!");
                    } else p.sendMessage(Core.getPrefix() + "Cannot find that player!");
                } else p.sendMessage(Core.getPrefix() + "Correct Usage: /donate [player] [hearts]");
            } else sender.sendMessage(Core.getPrefix() + "Only players can donate health!");
        } else sender.sendMessage(Core.getPrefix() + "The Health Donor feature is not enabled!");
        return true;
    }

}
