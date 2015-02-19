package me.roguemc9000.uhc.teams;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.UUID;

/**
 * Created by Nico Bentley on 7/14/2014.
 */
public interface UHCTeam {

    public String getName();

    public ChatColor getColor();

    public String getDisplayName();

    public void setDisplayName(String name);

    public List<UUID> getMembers();

    public List<UUID> getOriginalMembers();

}
