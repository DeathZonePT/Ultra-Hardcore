package me.roguemc9000.uhc.commands;

import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.features.Ninjanaut;
import me.roguemc9000.uhc.teams.UHCTeam;
import me.roguemc9000.uhc.utilities.GameState;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
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
public class Start implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()) {
            if (Core.getState() == GameState.LOBBY) {
                if (Core.getStaticBorder() > 700) {
                    if (!(Core.getFeature("Shrinking Borders").isEnabled() && Core.getFeature("go to hell").isEnabled
                            ())) {
                        if (Core.getFeature("teaming").isEnabled()) {
                            Bukkit.broadcastMessage(Core.getPrefix() + "Randomizing locations...");
                            HashMap<UHCTeam, Location> locs = new HashMap<UHCTeam, Location>();
                            for (UHCTeam team : Core.getTeams()) {
                                if (!team.getMembers().isEmpty()) {
                                    int x = 0;
                                    int z = 0;
                                    Boolean acceptable = false;
                                    while (!acceptable) {
                                        x = Core.getRand().nextInt(Core.getStaticBorder() * 2) - Core.getStaticBorder();
                                        z = Core.getRand().nextInt(Core.getStaticBorder() * 2) - Core.getStaticBorder();
                                        Location loc = Bukkit.getWorlds().get(0).getHighestBlockAt(x, z).getLocation
                                                ().subtract(0, 1, 0);
                                        acceptable = !(loc.distance(Core.getSpawn(Bukkit.getWorlds().get(0))) < 500
                                                || loc.distance(Core.getSpawn(Bukkit.getWorlds().get(0))) > (Core
                                                .getStaticBorder() - 200)) && !(loc.getBlock().getType() == Material
                                                .CACTUS ||
                                                loc.getBlock().getType() == Material.LAVA ||
                                                loc.getBlock().getType() == Material.STATIONARY_LAVA ||
                                                loc.getBlock().getType() == Material.WATER ||
                                                loc.getBlock().getType() == Material.STATIONARY_WATER);

                                    }
                                    locs.put(team, Bukkit.getWorlds().get(0).getHighestBlockAt(x, z).getLocation());
                                    for (UUID id : team.getMembers()) team.getOriginalMembers().add(id);
                                    Core.getPlaying().add(team);
                                }
                            }
                            Bukkit.broadcastMessage(Core.getPrefix() + "Loading chunks...");
                            for (UHCTeam team : Core.getPlaying()) {
                                locs.get(team).getChunk().load();
                                Boolean loaded = locs.get(team).getChunk().isLoaded();
                                while (!loaded) loaded = locs.get(team).getChunk().isLoaded();
                            }
                            Bukkit.broadcastMessage(Core.getPrefix() + "Teleporting teams...");
                            for (UHCTeam team : Core.getPlaying())
                                for (UUID id : team.getMembers())
                                    Bukkit.getPlayer(id).teleport(new Location(Bukkit.getWorld("world"), locs.get
                                            (team).getX(), 200, locs.get(team).getZ()));
                        } else {
                            Bukkit.broadcastMessage(Core.getPrefix() + "Randomizing locations");
                            HashMap<Player, Location> locs = new HashMap<Player, Location>();
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                int x = 0;
                                int z = 0;
                                Boolean acceptable = false;
                                while (!acceptable) {
                                    x = Core.getRand().nextInt(Core.getStaticBorder() * 2) - Core.getStaticBorder();
                                    z = Core.getRand().nextInt(Core.getStaticBorder() * 2) - Core.getStaticBorder();
                                    Location loc = Bukkit.getWorlds().get(0).getHighestBlockAt(x, z).getLocation()
                                            .subtract(0, 1, 0);
                                    acceptable = !(loc.distance(Core.getSpawn(p.getWorld())) < 500 || loc.distance
                                            (Core.getSpawn(p.getWorld())) > (Core.getStaticBorder() - 200)) && !(loc
                                            .getBlock().getType() == Material.CACTUS ||
                                            loc.getBlock().getType() == Material.LAVA ||
                                            loc.getBlock().getType() == Material.STATIONARY_LAVA ||
                                            loc.getBlock().getType() == Material.WATER ||
                                            loc.getBlock().getType() == Material.STATIONARY_WATER);
                                }
                                locs.put(p, Bukkit.getWorlds().get(0).getHighestBlockAt(x, z).getLocation());
                            }
                            Bukkit.broadcastMessage(Core.getPrefix() + "Loading chunks...");
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.setAllowFlight(true);
                                locs.get(p).getChunk().load();
                                Boolean loaded = locs.get(p).getChunk().isLoaded();
                                while (!loaded) loaded = locs.get(p).getChunk().isLoaded();
                            }
                            Bukkit.broadcastMessage(Core.getPrefix() + "Teleporting players...");
                            for (Player p : Bukkit.getOnlinePlayers())
                                p.teleport(new Location(Bukkit.getWorld("world"), locs.get(p).getX(), 200, locs.get
                                        (p).getZ()));
                        }

                        Core.setState(GameState.WARMUP);
                        Core.setTicks(45);

                        for (World w : Bukkit.getWorlds()) {
                            w.setTime(300);
                            w.setGameRuleValue("doFireTick", "true");
                            w.setPVP(false);
                        }

                        Core.getWhitelist().clear();
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.setGameMode(GameMode.SURVIVAL);
                            p.setFoodLevel(20);
                            p.setFireTicks(0);
                            p.setFlying(false);
                            p.setAllowFlight(false);
                            p.setHealth(20);
                            p.setOp(false);
                            p.getInventory().clear();
                            p.getInventory().setArmorContents(null);
                            p.setExhaustion(0F);
                            p.setSaturation(20F);
                            p.setExp(0F);
                            p.setLevel(0);
                            Core.getWhitelist().add(p.getUniqueId());
                            p.removeAchievement(Achievement.OPEN_INVENTORY);
                            for (PotionEffect e : p.getActivePotionEffects())
                                p.removePotionEffect(e.getType());
                            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 100));
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                if (!(p.canSee(player))) p.showPlayer(player);
                                if (!(player.canSee(p))) player.showPlayer(p);
                            }
                        }
                        Core.getWorld().setDifficulty(Difficulty.NORMAL);
                        Core.getWorld().setMonsterSpawnLimit(70);
                        Core.getWorld().setAnimalSpawnLimit(200);

                        if (Core.getFeature("go to hell").isEnabled()) {
                            Bukkit.broadcastMessage(Core.getPrefix() + "Go to Hell is enabled!");
                            Bukkit.broadcastMessage(Core.getPrefix() + "The Nether is disabled for 60 minutes.");
                            Bukkit.broadcastMessage(Core.getPrefix() + "Then a portal will be spawned at 0,0");
                            Bukkit.broadcastMessage(Core.getPrefix() + "Within 30 minutes from that point, the border" +
                                    " will be at a 10 block radius.");
                            Bukkit.broadcastMessage(Core.getPrefix() + "Everyone must be in the Nether by the " +
                                    "beginning of the 6th episode.");
                            Bukkit.broadcastMessage(Core.getPrefix() + "If you are not, you will be killed.");
                        }

                        if (Core.getFeature("ipvp").isEnabled()) {
                            for (World w : Bukkit.getWorlds()) w.setPVP(false);
                            Bukkit.broadcastMessage(Core.getPrefix() + "iPVP is enabled!");
                            Bukkit.broadcastMessage(Core.getPrefix() + "PVP will be disabled this game! Find a more " +
                                    "creative way to kill people!");
                        }

                        if (Core.getFeature("ninjanaut").isEnabled()) {
                            Player p = ((List<Player>) Bukkit.getOnlinePlayers()).get(Core.getRand().nextInt(Bukkit
                                    .getOnlinePlayers().size()));
                            Bukkit.broadcastMessage(Core.getPrefix() + "Ninjanaut is enabled!");
                            Bukkit.broadcastMessage(Core.getPrefix() + p.getName() + " has been randomly selected as " +
                                    "the ninja!");
                            Bukkit.broadcastMessage(Core.getPrefix() + p.getName() + " has infinite speed II and " +
                                    "strength II!");
                            Bukkit.broadcastMessage(Core.getPrefix() + "Killing " + p.getName() + " will make YOU the" +
                                    " ninja!");
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
                            Ninjanaut.setNinja(p.getUniqueId());
                        }

                        if (Core.getFeature("goldenheads").isEnabled()) {
                            ItemStack head = new ItemStack(Material.GOLDEN_APPLE);
                            ItemMeta meta = head.getItemMeta();
                            meta.setDisplayName(ChatColor.GOLD + "Golden Head");
                            head.setItemMeta(meta);
                            ShapedRecipe r = new ShapedRecipe(head);
                            r.shape("@@@", "@*@", "@@@").setIngredient('@', Material.GOLD_INGOT).setIngredient('*',
                                    Material.SKULL_ITEM);
                            Bukkit.addRecipe(r);
                        }

                        Bukkit.getWorlds().get(0).getHighestBlockAt(0, 0).setType(Material.AIR);
                    } else sender.sendMessage(Core.getPrefix() + "Shrinking Borders and Go To Hell cannot both be " +
                            "enabled. Please disable one...");
                } else sender.sendMessage(Core.getPrefix() + "Invalid border size!");
            } else sender.sendMessage(Core.getPrefix() + "The game is already running!");
        } else sender.sendMessage(Core.getPrefix() + "Insufficient Permission!");
        return true;
    }

}
