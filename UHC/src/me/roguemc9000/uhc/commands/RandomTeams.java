package me.roguemc9000.uhc.commands;

import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.teams.UHCTeam;
import me.roguemc9000.uhc.utilities.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Code written by Nico Bentley.
 * Raw code not to be sold, redistributed,
 * or edited by anyone else without written consent
 * from Nico Bentley.
 * <p/>
 * (C) All Rights Reserved
 */
public class RandomTeams implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()) {
            if (Core.getFeature("teaming").isEnabled()) {
                if (Core.getState() == GameState.LOBBY) {
                    if (args.length == 1) {
                        if (Bukkit.getOnlinePlayers().size() % Integer.parseInt(args[0]) == 0) {
                            Bukkit.broadcastMessage(Core.getPrefix() + "RANDOMIZING TEAMS!!!");
                            for (UHCTeam t : Core.getTeams()) t.getMembers().clear();
                            int size = Integer.parseInt(args[0]);
                            List<UHCTeam> teams = new ArrayList<UHCTeam>();
                            List<Player> players = new ArrayList<Player>();
                            List<Player> online = new ArrayList<Player>();
                            for (Player player : Bukkit.getOnlinePlayers()) online.add(player);
                            boolean go;
                            while (players.size() < online.size()) {
                                UHCTeam t = null;
                                go = false;
                                while (!go) {
                                    int ran = new Random().nextInt(16);
                                    UHCTeam team = Core.getTeams().get(ran);
                                    if (teams.contains(team)) go = false;
                                    else {
                                        t = team;
                                        go = true;
                                    }
                                }
                                List<String> playersOnTeam = new ArrayList<String>();
                                go = false;
                                while (!go) {
                                    int ran = new Random().nextInt(online.size());
                                    Player p = online.get(ran);
                                    if (players.contains(p)) go = false;
                                    else if (playersOnTeam.size() < size - 1) {
                                        players.add(p);
                                        Core.setTeam(p, t.getName());
                                        playersOnTeam.add(p.getName());
                                        go = false;
                                    } else {
                                        players.add(p);
                                        Core.setTeam(p, t.getName());
                                        playersOnTeam.add(p.getName());
                                        go = true;
                                    }
                                }
                                for (String s : playersOnTeam) {
                                    Player p = null;
                                    for (Player player : Bukkit.getOnlinePlayers())
                                        if (player.getName().equalsIgnoreCase(s)) p = player;
                                    if (p != null) {
                                        p.sendMessage(Core.getPrefix() + "You have been put on the " + t.getColor() +
                                                t.getName() +
                                                ChatColor.GRAY + " team via randomization!");
                                        p.sendMessage(Core.getPrefix() + "Your teammates are:");
                                        List<String> names = new ArrayList<String>();
                                        for (UUID id : t.getMembers()) names.add(Bukkit.getOfflinePlayer(id).getName());
                                        p.sendMessage(Core.getPrefix() + t.getColor() + names.toString());
                                    }
                                }
                                teams.add(t);
                            }
                            Bukkit.broadcastMessage(Core.getPrefix() + "Everyone is on a team! Let the game begin!");
                        } else sender.sendMessage(Core.getPrefix() + "That would cause uneven teams!");
                    } else sender.sendMessage(Core.getPrefix() + "Set a team size!");
                } else sender.sendMessage(Core.getPrefix() + "The game has already begun!");
            } else sender.sendMessage(Core.getPrefix() + "Teaming is not enabled!");
        }

        return true;
    }

}
