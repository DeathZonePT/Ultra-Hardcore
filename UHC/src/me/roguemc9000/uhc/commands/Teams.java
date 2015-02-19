package me.roguemc9000.uhc.commands;

import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.teams.UHCTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Code written by Nico Bentley.
 * Raw code not to be sold, redistributed,
 * or edited by anyone else without written consent
 * from Nico Bentley.
 * <p/>
 * (C) All Rights Reserved
 */
public class Teams implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()) {
            if (Core.getFeature("teaming").isEnabled()) {
                if (label.equalsIgnoreCase("setteam") || label.equals("st")) {
                    if (args.length != 2)
                        sender.sendMessage(Core.getPrefix() + ChatColor.RED + "That command takes two arguments: " +
                                "player, and team!");
                    else {
                        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
                        if (player.hasPlayedBefore() || player.isOnline()) {
                            String team = args[1];
                            List<UHCTeam> teams = new ArrayList<UHCTeam>();
                            for (UHCTeam t : Core.getTeams())
                                if (t.getName().equalsIgnoreCase(team)) teams.add(t);
                            if (!teams.isEmpty()) {
                                Core.setTeam(player, team);
                                sender.sendMessage(Core.getPrefix() + player.getName() + " has been put on the " +
                                        Core.getTeam(team).getColor() + Core.getTeam(team).getName() + ChatColor.GRAY +
                                        " team!");
                                if (player.isOnline())
                                    player.getPlayer().sendMessage(Core.getPrefix() + sender.getName() + " has put " +
                                            "you on the " +
                                            Core.getTeam(team).getColor() + Core.getTeam(team).getName() +
                                            ChatColor.GRAY + " team!");
                            } else sender.sendMessage(Core.getPrefix() + ChatColor.RED + "That team cannot be found!");
                        } else sender.sendMessage(Core.getPrefix() + "Cannot find that player!");
                    }
                } else if (label.equalsIgnoreCase("setname") || label.equals("sn")) {
                    if (args.length != 2)
                        sender.sendMessage(Core.getPrefix() + ChatColor.RED + "That command takes two arguments: " +
                                "team, and name!");
                    else {
                        UHCTeam team = Core.getTeam(args[0]);
                        if (team == null) {
                            sender.sendMessage(Core.getPrefix() + ChatColor.RED + "That team cannot be found!");
                            return true;
                        }
                        String teamName = args[1].replaceAll("&", "§");
                        team.setDisplayName(teamName);
                        Bukkit.broadcastMessage(Core.getPrefix() + sender.getName() + " has changed the name of the " +
                                team.getColor() + team.getName() + ChatColor.GRAY + " team to '" + team.getColor() +
                                teamName + ChatColor.GRAY + "'!");
                    }
                } else if (label.equalsIgnoreCase("removeplayer") || label.equalsIgnoreCase("rp")) {
                    if (args.length != 1)
                        sender.sendMessage(Core.getPrefix() + ChatColor.RED + "That command takes one argument: " +
                                "player!");
                    else {
                        for (UHCTeam t : Core.getTeams())
                            if (t.getMembers().contains(Bukkit.getOfflinePlayer(args[0]).getUniqueId()))
                                t.getMembers().remove(Bukkit.getOfflinePlayer(args[0]).getUniqueId());
                        sender.sendMessage(Core.getPrefix() + args[0] + " has been removed from their team!");
                    }
                }
            } else sender.sendMessage(Core.getPrefix() + "Teaming is not enabled!");
        }
        return true;
    }
}