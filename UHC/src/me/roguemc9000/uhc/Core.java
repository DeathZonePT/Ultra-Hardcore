package me.roguemc9000.uhc;

import me.roguemc9000.uhc.commands.*;
import me.roguemc9000.uhc.features.*;
import me.roguemc9000.uhc.features.Teams;
import me.roguemc9000.uhc.listeners.*;
import me.roguemc9000.uhc.teams.*;
import me.roguemc9000.uhc.utilities.Feature;
import me.roguemc9000.uhc.utilities.GameState;
import me.roguemc9000.uhc.utilities.TimeManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

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
public class Core extends JavaPlugin {

    private static String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "UHC" + ChatColor.DARK_GRAY + "] " +
            ChatColor.GRAY;
    private static World world = Bukkit.getWorlds().get(0);
    private static GameState state;
    private static Random rand = new Random();
    private static Integer ticks = 0;
    private static Integer seconds = 0;
    private static Integer border = 0;
    private static Integer shrinkSize = 100;
    private static List<UHCTeam> teams = new ArrayList<UHCTeam>();
    private static List<Feature> features = new ArrayList<Feature>();
    private static List<UUID> whitelist = new ArrayList<UUID>();
    private static List<Scoreboard> boards = new ArrayList<Scoreboard>();
    private static List<UHCTeam> playing = new ArrayList<UHCTeam>();

    public static Random getRand() {
        return rand;
    }

    public static List<UHCTeam> getPlaying() {
        return playing;
    }

    public static List<Feature> getFeatures() {
        return features;
    }

    public static GameState getState() {
        return state;
    }

    public static void setState(GameState state) {
        Core.state = state;
    }

    public static Integer getTicks() {
        return ticks;
    }

    public static void setTicks(Integer ticks) {
        Core.ticks = ticks;
    }

    public static Integer getSeconds() {
        return seconds;
    }

    public static void setSeconds(Integer seconds) {
        Core.seconds = seconds;
    }

    public static Integer getBorder(Player p) {
        if (!Core.getFeature("gotohell").isEnabled() || Core.getSeconds() >= 7200 ||
                (Core.getSeconds() <= 6000 && p.getWorld().getName().equalsIgnoreCase("world"))) {
            return border;
        } else return 1000;
    }

    public static Integer getStaticBorder() {
        return border;
    }

    public static void setBorder(Integer border) {
        Core.border = border;
    }

    public static Integer getShrinkSize() {
        return shrinkSize;
    }

    public static void setShrinkSize(Integer shrinkSize) {
        Core.shrinkSize = shrinkSize;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static World getWorld() {
        return world;
    }

    public static List<Scoreboard> getBoards() {
        return boards;
    }

    public static List<UUID> getWhitelist() {
        return whitelist;
    }

    public static List<UHCTeam> getTeams() {
        return teams;
    }

    public static Location getSpawn(World p) {
        return p.getHighestBlockAt(0, 0).getLocation();
    }

    public static Feature getFeature(String name) {
        for (Feature f : getFeatures())
            if (f.getName().replaceAll(" ", "").equalsIgnoreCase(name.replaceAll(" ", ""))) return f;
        return null;
    }

    public static UHCTeam getTeam(String name) {
        for (UHCTeam t : getTeams()) if (t.getName().equalsIgnoreCase(name)) return t;
        return null;
    }

    public static UHCTeam getTeam(OfflinePlayer p) {
        for (UHCTeam t : getTeams()) if (t.getMembers().contains(p.getUniqueId())) return t;
        return null;
    }

    public static void setTeam(OfflinePlayer player, String team) {
        for (UHCTeam t : getTeams()) {
            if (t.getMembers().contains(player.getUniqueId())) {
                for (Scoreboard board : getBoards()) {
                    board.getTeam(t.getName()).removePlayer(player);
                }
                t.getMembers().remove(player.getUniqueId());
            }
            if (t.getName().equalsIgnoreCase(team)) {
                for (Scoreboard board : getBoards()) {
                    board.getTeam(t.getName()).addPlayer(player);
                }
                t.getMembers().add(player.getUniqueId());
            }
        }
    }

    public static Scoreboard getBoard(Player p) {
        if (getFeature("teaming").isEnabled()) {
            for (Scoreboard board : getBoards()) {
                if (board.getObjective(DisplaySlot.SIDEBAR).getName().equalsIgnoreCase(getTeam(p).getName()))
                    return board;
            }
        } else {
            for (Scoreboard board : getBoards())
                if (board.getObjective(DisplaySlot.SIDEBAR).getName().equalsIgnoreCase(p.getName())) return board;
        }
        return null;
    }

    public static Scoreboard getBoard(UHCTeam t) {
        for (Scoreboard board : getBoards())
            if (board.getObjective(DisplaySlot.SIDEBAR).getName().equalsIgnoreCase(t.getName())) return board;
        return null;
    }

    public static void run(Runnable r) {
        Thread thread = new Thread(r);
        thread.start();
    }

    @Override
    public void onEnable() {
        features.add(new Absorption());
        features.add(new CutClean());
        features.add(new EternalDay());
        features.add(new Heads());
        features.add(new ShrinkingBorders());
        features.add(new Teams());
        features.add(new DamageDodgers());
        features.add(new HealthDonor());
        features.add(new iPVP());
        features.add(new Ninjanaut());
        features.add(new GoToHell());

        for (Feature f : features) {
            f.setEnabled(false);
            Bukkit.getPluginManager().registerEvents(f, this);
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Absorption(), 20L, 20L);

        getCommand("rules").setExecutor(new Rules());
        getCommand("heal").setExecutor(new Heal());
        getCommand("border").setExecutor(new SetBorder());
        getCommand("donate").setExecutor(new Donate());
        getCommand("start").setExecutor(new Start());
        getCommand("randomize").setExecutor(new RandomTeams());
        getCommand("list").setExecutor(new ListCommand());
        getCommand("setteam").setExecutor(new me.roguemc9000.uhc.commands.Teams());
        getCommand("setname").setExecutor(new me.roguemc9000.uhc.commands.Teams());
        getCommand("removeplayer").setExecutor(new me.roguemc9000.uhc.commands.Teams());
        getCommand("setrule").setExecutor(new SetRule());
        getCommand("pause").setExecutor(new Pause());
        getCommand("settime").setExecutor(new SetTime());
        getCommand("chatlock").setExecutor(new ChatLock());

        Bukkit.getPluginManager().registerEvents(new DeathListener(this), this);
        Bukkit.getPluginManager().registerEvents(new JoinListeners(this), this);
        Bukkit.getPluginManager().registerEvents(new MoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PotionListeners(), this);
        Bukkit.getPluginManager().registerEvents(new UtilityListeners(), this);
        Bukkit.getPluginManager().registerEvents(new AntiSpam(), this);

        setState(GameState.LOBBY);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TimeManager(this), 20L, 20L);

        teams.add(new Aqua());
        teams.add(new Black());
        teams.add(new Blue());
        teams.add(new DarkAqua());
        teams.add(new DarkBlue());
        teams.add(new DarkGray());
        teams.add(new DarkGreen());
        teams.add(new DarkRed());
        teams.add(new Gold());
        teams.add(new Gray());
        teams.add(new Green());
        teams.add(new Pink());
        teams.add(new Purple());
        teams.add(new Red());
        teams.add(new White());
        teams.add(new Yellow());

    }

}
