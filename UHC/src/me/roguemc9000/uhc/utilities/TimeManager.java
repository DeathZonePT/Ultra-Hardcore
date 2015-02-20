package me.roguemc9000.uhc.utilities;

import me.confuser.barapi.BarAPI;
import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.commands.Pause;
import me.roguemc9000.uhc.teams.UHCTeam;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.util.Arrays;
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
public class TimeManager implements Runnable {

    private Core core;

    public TimeManager(Core instance) {
        core = instance;
    }

    @Override
    public void run() {
        if (Core.getState() != GameState.LOBBY && !Pause.paused) {
            Integer ticks = Core.getTicks();
            GameState state = Core.getState();
            if (state == GameState.WARMUP) {
                if (ticks == 44) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 880, 100));
                        p.setAllowFlight(false);
                        p.setFlying(false);
                    }
                } else if (ticks == 40) {
                    if (Core.getFeature("teaming").isEnabled()) {
                        for (UHCTeam team : Core.getTeams()) {
                            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
                            Objective side = board.registerNewObjective(team.getName(), "dummy");
                            Objective tab = board.registerNewObjective("Tab", "dummy");
                            side.setDisplaySlot(DisplaySlot.SIDEBAR);
                            tab.setDisplaySlot(DisplaySlot.PLAYER_LIST);
                            for (UHCTeam t : Core.getTeams()) {
                                board.registerNewTeam(t.getName()).setPrefix(t.getColor() + "");
                                for (UUID id : t.getMembers())
                                    board.getTeam(t.getName()).addPlayer(Bukkit.getOfflinePlayer(id));
                            }
                            Core.getBoards().add(board);
                        }
                    } else {
                        for (UUID id : Core.getWhitelist()) {
                            Player p = Bukkit.getPlayer(id);
                            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
                            Objective side = board.registerNewObjective(p.getName(), "dummy");
                            Objective tab = board.registerNewObjective("Tab", "dummy");
                            side.setDisplaySlot(DisplaySlot.SIDEBAR);
                            tab.setDisplaySlot(DisplaySlot.PLAYER_LIST);
                            board.registerNewTeam("High").setPrefix(ChatColor.GREEN + "");
                            board.registerNewTeam("Medium").setPrefix(ChatColor.YELLOW + "");
                            board.registerNewTeam("Low").setPrefix(ChatColor.RED + "");
                            board.registerNewTeam("Dead").setPrefix(ChatColor.GRAY + "");
                            Core.getBoards().add(board);
                        }
                    }
                } else if (ticks == 0) {
                    Core.setState(GameState.GRACE);
                    Core.setTicks(600);
                    Core.setSeconds(1);
                    Bukkit.broadcastMessage(Core.getPrefix() + "The game has begun!");
                    Bukkit.broadcastMessage(Core.getPrefix() + "PVP will be disabled for 10 minutes!");
                    Bukkit.broadcastMessage(Core.getPrefix() + "Do /rules to view this game's rules.");
                    if (Core.getFeature("Damage Dodgers").isEnabled())
                        Bukkit.broadcastMessage(Core.getPrefix() + "The first 5 people to take damage will be " +
                                "eliminated!");
                    for (World world : Bukkit.getWorlds()) world.setDifficulty(Difficulty.NORMAL);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.playSound(player.getLocation(), Sound.FIREWORK_TWINKLE, .5F, 1F);
                        player.playSound(player.getLocation(), Sound.FIZZ, .5F, 1F);
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, .5F, 1F);
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(core, new Runnable() {
                        @Override
                        public void run() {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendMessage(Core.getPrefix() + "I'm feeling generous.");
                                p.sendMessage(Core.getPrefix() + "Here is one final heal!");
                                p.setHealth(20);
                            }
                        }
                    }, 400L);
                } else if (ticks > 0 && ticks <= 5) {
                    Bukkit.broadcastMessage(Core.getPrefix() + "The game will begin in " + ChatColor.RED + ticks +
                            ChatColor.GRAY +
                            " seconds!");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, .5F, 1F);
                    }
                } else if ((ticks > 5 && ticks <= 10) || ticks == 15 || ticks == 30) {
                    Bukkit.broadcastMessage(Core.getPrefix() + "The game will begin in " + ChatColor.RED + ticks +
                            ChatColor.GRAY +
                            " seconds!");
                    for (Player p : Bukkit.getOnlinePlayers())
                        p.playSound(p.getLocation(), Sound.CLICK, .5F, 1F);
                }
            } else if (state == GameState.GRACE) {
                if (ticks == 0) {
                    if (!Core.getFeature("ipvp").isEnabled()) {
                        Bukkit.broadcastMessage(Core.getPrefix() + "PVP is now enabled!");
                        for (World w : Bukkit.getWorlds()) w.setPVP(true);
                    }
                    Core.setState(GameState.INGAME);
                }
            }

            //Time and Episode
            int hours = Core.getSeconds() / 3600;
            int remainder = Core.getSeconds() % 3600;
            int mins = remainder / 60;
            int secs = remainder % 60;
            int episode = Core.getSeconds() / 1200 + 1;

            //Border
            if (state != GameState.WARMUP) {
                if (Core.getSeconds() % 1200 == 0) {
                    if (Core.getFeature("shrinking borders").isEnabled()) {
                        Core.run(new Border());
                        int shrink = Core.getStaticBorder() - Core.getShrinkSize();
                        if (shrink < 100) shrink = 100;
                        Bukkit.broadcastMessage(Core.getPrefix() + "The border is now shrinking to " + ChatColor
                                .DARK_RED + "" +
                                ChatColor.BOLD + shrink + " " + ChatColor.GRAY + "blocks!");
                    }
                    Bukkit.broadcastMessage(Core.getPrefix() + "Episode " + (episode - 1) + " has ended!");
                    Bukkit.broadcastMessage(Core.getPrefix() + "Episode " + episode + " begins now!");
                    for (Player player : Bukkit.getOnlinePlayers())
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, .5F, 1F);
                }
                if (Core.getSeconds() % 1800 == 0)
                    Bukkit.broadcastMessage(Core.getPrefix() + "Mark: " + Core.getSeconds() / 60 + " minutes in!");
                else if ((mins == 15 || mins == 35 || mins == 55) && secs % 20 == 0) {
                    if (Core.getFeature("shrinking borders").isEnabled())
                        Bukkit.broadcastMessage(Core.getPrefix() + ChatColor.RED + "WARNING! THE BORDER WILL SHRINK " +
                                "IN " + ChatColor.DARK_RED +
                                "" + ChatColor.BOLD + "5 " + ChatColor.RED + "MINUTES!!!");
                }

                //Scoreboard and BarAPI
                if (Core.getFeature("teaming").isEnabled()) {
                    for (UHCTeam team : Core.getPlaying()) {
                        Scoreboard board = Core.getBoard(team);
                        Objective obj = board.getObjective(DisplaySlot.SIDEBAR);
                        obj.setDisplayName(ChatColor.GRAY + "Border: " + ChatColor.RED + Core.getStaticBorder() + "  " +
                                ChatColor.GRAY + "Ep " + ChatColor.RED + episode);
                        List<String> colors = Arrays.asList(ChatColor.GREEN + "", ChatColor.YELLOW + "", ChatColor
                                .RED + "", ChatColor.GRAY + "");
                        for (UUID id : team.getOriginalMembers()) {
                            OfflinePlayer p = Bukkit.getOfflinePlayer(id);
                            for (String color : colors)
                                if (p.getName().length() > 14) board.resetScores((color + p.getName()).substring(16));
                                else board.resetScores(color + p.getName());
                            if (team.getMembers().contains(id) && p.isOnline()) {
                                if (p.getPlayer().getHealth() > 14) {
                                    if (p.getPlayer().getName().length() > 14)
                                        obj.getScore((ChatColor.GREEN + p.getName()).substring(0, 15)).setScore((int)
                                                p.getPlayer().getHealth() * 5);
                                    else
                                        obj.getScore(ChatColor.GREEN + p.getName()).setScore((int) p.getPlayer()
                                                .getHealth() * 5);
                                } else if (p.getPlayer().getHealth() > 7) {
                                    if (p.getPlayer().getName().length() > 14)
                                        obj.getScore((ChatColor.YELLOW + p.getName()).substring(0, 15)).setScore(
                                                (int) p.getPlayer().getHealth() * 5);
                                    else
                                        obj.getScore(ChatColor.YELLOW + p.getName()).setScore((int) p.getPlayer()
                                                .getHealth() * 5);
                                } else if (p.getPlayer().getName().length() > 14)
                                    obj.getScore((ChatColor.RED + p.getName()).substring(0, 15)).setScore((int) p
                                            .getPlayer().getHealth() * 5);
                                else
                                    obj.getScore(ChatColor.RED + p.getName()).setScore((int) p.getPlayer().getHealth
                                            () * 5);
                            } else if (p.getPlayer().getName().length() > 14)
                                obj.getScore((ChatColor.GRAY + p.getName()).substring(0, 15)).setScore(0);
                            else obj.getScore(ChatColor.GRAY + p.getName()).setScore(0);
                            DecimalFormat df = new DecimalFormat("##00");
                            if (p.isOnline())
                                BarAPI.setMessage(p.getPlayer(), ChatColor.GRAY + df.format(hours) + ":" + df.format
                                        (mins) + ":" + df.format(secs) +
                                        "   Distance from border: " + ChatColor.GREEN +
                                        new DecimalFormat("#,##0.00").format(Core.getBorder(p.getPlayer()) - Math
                                                .sqrt(Math.pow(p.getPlayer().getLocation().getX(), 2) + Math.pow(p
                                                        .getPlayer().getLocation().getZ(), 2))));
                            p.getPlayer().setScoreboard(board);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                board.getObjective("Tab").getScore(player.getName()).setScore((int) (player.getHealth
                                        () * 5));
                            }
                        }
                    }
                } else {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        Objective obj = Core.getBoard(p).getObjective(DisplaySlot.SIDEBAR);
                        obj.setDisplayName(ChatColor.GRAY + "Border: " + ChatColor.RED + Core.getBorder(p) + "  " +
                                ChatColor.GRAY +
                                "Ep " + ChatColor.RED + episode);
                        obj.getScore(p.getName()).setScore((int) (p.getHealth() * 5));
                        DecimalFormat df = new DecimalFormat("##00");
                        BarAPI.setMessage(p, ChatColor.GRAY + df.format(hours) + ":" + df.format(mins) + ":" + df
                                .format(secs) +
                                "   " +
                                "Distance from border: " + ChatColor.GREEN + new DecimalFormat("#,##0.00").format
                                (Core.getBorder(p) - Math.sqrt(Math.pow(p.getLocation().getX(), 2) + Math.pow(p
                                        .getLocation().getZ(), 2))));
                        p.setScoreboard(Core.getBoard(p));
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            Core.getBoard(p).getObjective("Tab").getScore(player.getName()).setScore((int) (player
                                    .getHealth() * 5));
                        }
                    }
                    for (Scoreboard board : Core.getBoards()) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (board.getPlayerTeam(p) != null) board.getPlayerTeam(p).removePlayer(p);
                            if (Core.getWhitelist().contains(p.getUniqueId())) {
                                if (p.getHealth() > 14) board.getTeam("High").addPlayer(p);
                                else if (p.getHealth() > 7) board.getTeam("Medium").addPlayer(p);
                                else board.getTeam("Low").addPlayer(p);
                            } else board.getTeam("Dead").addPlayer(p);
                        }
                    }
                }
            }

            //Features
            if (hours == 1 && mins == 0 && secs == 0) {
                Core.run(new Compass());
                Core.run(new CompassSetter());

                if (Core.getFeature("eternalday").isEnabled())
                    Bukkit.broadcastMessage(Core.getPrefix() + "It has been 60 minutes and is now eternally day!");
                if (Core.getFeature("gotohell").isEnabled()) {
                    Bukkit.broadcastMessage(Core.getPrefix() + "There is now a portal at 0,0!");
                    Bukkit.broadcastMessage(Core.getPrefix() + "The border will be at a 10 block radius in 30 minutes" +
                            ".");
                    int y = Core.getWorld().getHighestBlockYAt(0, 0);
                    Core.getWorld().getHighestBlockAt(0, 0).setType(Material.STATIONARY_WATER);
                    Core.getWorld().getBlockAt(1, y, 1).setType(Material.STATIONARY_WATER);
                    Core.getWorld().getBlockAt(1, y, -1).setType(Material.STATIONARY_WATER);
                    Core.getWorld().getBlockAt(1, y, 0).setType(Material.STATIONARY_WATER);
                    Core.getWorld().getBlockAt(-1, y, 1).setType(Material.STATIONARY_WATER);
                    Core.getWorld().getBlockAt(-1, y, -1).setType(Material.STATIONARY_WATER);
                    Core.getWorld().getBlockAt(-1, y, 0).setType(Material.STATIONARY_WATER);
                    Core.getWorld().getBlockAt(0, y, 1).setType(Material.STATIONARY_WATER);
                    Core.getWorld().getBlockAt(0, y, -1).setType(Material.STATIONARY_WATER);
                    Core.getWorld().getBlockAt(1, y - 1, 1).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(1, y - 1, -1).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(1, y - 1, 0).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(-1, y - 1, 1).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(-1, y - 1, -1).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(-1, y - 1, 0).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(0, y - 1, 1).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(0, y - 1, -1).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(0, y - 1, 0).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(2, y, 0).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(2, y, 1).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(2, y, 2).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(1, y, 2).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(0, y, 2).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(-1, y, 2).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(-2, y, 2).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(-2, y, 1).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(-2, y, 0).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(-2, y, -1).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(-2, y, -2).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(-1, y, -2).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(0, y, -2).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(1, y, -2).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(2, y, -2).setType(Material.BEDROCK);
                    Core.getWorld().getBlockAt(2, y, -1).setType(Material.BEDROCK);

                    Bukkit.getScheduler().scheduleSyncRepeatingTask(core, new Runnable() {
                        @Override
                        public void run() {
                            if (Core.getStaticBorder() > 10) Core.setBorder(Core.getStaticBorder() - 1);
                        }
                    }, 20L, (36000 / (Core.getStaticBorder() - 10)));
                }
            }

            if (hours == 1 & mins == 40 && secs == 0) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getWorld().getName().equalsIgnoreCase("world")) {
                        if (Core.getWhitelist().contains(p.getUniqueId())) {
                            p.setHealth(0);
                            Bukkit.broadcastMessage(Core.getPrefix() + p.getName() + " was not in the Nether on time!");
                        } else p.teleport(Bukkit.getWorld("world_nether").getHighestBlockAt(0, 0).getLocation());
                    }
                }
            }

            if (hours >= 1) {
                if (Core.getFeature("eternalday").isEnabled()) for (World w : Bukkit.getWorlds()) w.setTime(6000L);
            }
            //Move Listener
            for (UUID id : Core.getWhitelist()) {
                Player p = Bukkit.getPlayer(id);
                if (Core.getState() == GameState.INGAME || Core.getState() == GameState.GRACE) {
                    if (Core.getBorder(p) - Math.sqrt(Math.pow(p.getLocation().getX(), 2) + Math.pow(p.getLocation()
                            .getZ(), 2)) <= 75)
                        p.sendMessage(Core.getPrefix() + "You are " + ChatColor.RED +
                                new DecimalFormat("#,##0.00").format(Core.getBorder(p) - Math.sqrt(Math.pow(p
                                        .getLocation().getX(), 2) + Math.pow(p.getLocation().getZ(), 2))) +
                                ChatColor.GRAY + " blocks from the border!");
                    if (Core.getBorder(p) - Math.sqrt(Math.pow(p.getLocation().getX(), 2) + Math.pow(p.getLocation()
                            .getZ(), 2)) <= 5)
                        p.playSound(p.getLocation(), Sound.CAT_HISS, 1F, 1F);
                    if (Core.getBorder(p) - Math.sqrt(Math.pow(p.getLocation().getX(), 2) + Math.pow(p.getLocation()
                            .getZ(), 2)) <= 0) {
                        p.sendMessage(Core.getPrefix() + "There's the border!");
                        Integer x = 0;
                        Integer z = 0;
                        if (p.getLocation().getX() > 0) x = -5;
                        else if (p.getLocation().getX() < 0) x = 5;
                        if (p.getLocation().getZ() > 0) z = -5;
                        else if (p.getLocation().getZ() < 0) z = 5;
                        p.teleport(Core.getWorld().getHighestBlockAt((int) p.getLocation().getX() + x, (int) p
                                .getLocation().getZ() + z).getLocation());
                    }
                }
            }
            if (Core.getTicks() != 0) Core.setTicks(Core.getTicks() - 1);
            if (Core.getSeconds() != 0) Core.setSeconds(Core.getSeconds() + 1);

        }
    }
}
