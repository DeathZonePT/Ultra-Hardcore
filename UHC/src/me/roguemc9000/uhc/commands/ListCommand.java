package me.roguemc9000.uhc.commands;

import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.teams.UHCTeam;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Code written by Nico Bentley.
 * Raw code not to be sold, redistributed,
 * or edited by anyone else without written consent
 * from Nico Bentley.
 * <p/>
 * (C) All Rights Reserved
 */
public class ListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Core.getFeature("teaming").isEnabled()) {
            sender.sendMessage(Core.getPrefix() + "Here are all the teams that contain players:");
            for (UHCTeam t : Core.getTeams())
                if (!(t.getMembers().isEmpty())) {
                    List<String> names = new ArrayList<String>();
                    for (UUID id : t.getMembers()) names.add(Bukkit.getOfflinePlayer(id).getName());
                    sender.sendMessage(t.getColor() + t.getName() + "§8: " + names.toString());
                }
            List<String> players = new ArrayList<String>();
            for (Player p : Bukkit.getOnlinePlayers())
                if (Core.getTeam(p) == null) players.add(p.getName());
            sender.sendMessage(Core.getPrefix() + "Here are all the players that aren't on a team:");
            sender.sendMessage("§8" + players.toString());
        } else sender.sendMessage(Core.getPrefix() + "Teaming is not enabled!");
        return true;
    }
}
