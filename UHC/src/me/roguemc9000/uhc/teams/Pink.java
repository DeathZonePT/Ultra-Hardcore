package me.roguemc9000.uhc.teams;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Nico Bentley on 7/14/2014.
 */
public class Pink implements UHCTeam {

    private List<UUID> members = new ArrayList<UUID>();
    private String displayName = null;
    private List<UUID> originalMembers = new ArrayList<UUID>();

    @Override
    public String getName() {
        return "Pink";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.LIGHT_PURPLE;
    }

    @Override
    public String getDisplayName() {
        if (displayName != null) return displayName;
        else return getName();
    }

    @Override
    public void setDisplayName(String name) {
        displayName = name;
    }

    @Override
    public List<UUID> getMembers() {
        return members;
    }

    @Override
    public List<UUID> getOriginalMembers() { return originalMembers; }
}
