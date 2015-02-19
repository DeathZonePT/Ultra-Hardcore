package me.roguemc9000.uhc.listeners;

import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.teams.UHCTeam;
import me.roguemc9000.uhc.utilities.GameState;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Nico on 10/6/2014.
 */
public class DeathListener implements Listener {

    Player p = null;

    private Core core;

    public DeathListener(Core instance) {
        core = instance;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getKiller() != null && p == null) {
            p = player.getKiller();
            Bukkit.broadcastMessage(Core.getPrefix() + p.getName() + " has drawn first blood!");
        }
        Location deathLoc = player.getLocation();
        if (Core.getFeature("goldenheads").isEnabled()) {
            ItemStack head = new ItemStack(Material.SKULL_ITEM);
            player.getWorld().dropItemNaturally(deathLoc, head);
        }
        Bukkit.broadcastMessage(Core.getPrefix() + ChatColor.RED + event.getDeathMessage());
        event.setDeathMessage(null);
        if (Core.getFeature("shrinking borders").isEnabled()) {
            Core.setShrinkSize(Core.getShrinkSize() + 25);
            Bukkit.broadcastMessage(Core.getPrefix() + "The border will now shrink by " + ChatColor.RED + "" +
                    ChatColor.BOLD +
                    Core.getShrinkSize() + ChatColor.GRAY + " blocks every episode!");
        }
        Core.getWhitelist().remove(player.getUniqueId());
        if (Core.getFeature("teaming").isEnabled()) {
            UHCTeam team = Core.getTeam(player);
            team.getMembers().remove(player.getUniqueId());
            if (team.getMembers().isEmpty()) {
                Bukkit.broadcastMessage(Core.getPrefix() + team.getColor() + team.getName() + ChatColor.GRAY + " has " +
                        "been ELIMINATED");
                Core.getPlaying().remove(team);
                if (Core.getPlaying().size() == 1) {
                    Bukkit.broadcastMessage(Core.getPrefix() + "Congratulations to " + Core.getPlaying().get(0)
                            .getColor() +
                            Core.getPlaying().get(0).getName() + ChatColor.GRAY + " for winning UHC!");
                } else
                    Bukkit.broadcastMessage(Core.getPrefix() + "There are now " + ChatColor.RED + Core.getPlaying()
                            .size() +
                            ChatColor.GRAY + " teams remaining!");
            } else {
                Bukkit.broadcastMessage(Core.getPrefix() + "There are " + ChatColor.RED + team.getMembers().size() +
                        ChatColor.GRAY +
                        " players left on " + team.getColor() + team.getName() + ChatColor.GRAY + "!");
            }
        } else {
            if (Core.getWhitelist().size() == 1) {
                Bukkit.broadcastMessage(Core.getPrefix() + "Congratulation to " + ChatColor.GREEN +
                        Core.getWhitelist().get(0) + ChatColor.GRAY + " for winning UHC!");
                Core.setState(GameState.ENDGAME);
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.EXPLODE, .5F, 1F);
            p.playSound(p.getLocation(), Sound.AMBIENCE_THUNDER, 1F, 1F);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.SHEEP)
            event.getDrops().add(new ItemStack(Material.PORK, new Random().nextInt(3) + 1));
        else if (event.getEntityType() == EntityType.SQUID) {
            event.getDrops().add(new ItemStack(Material.RAW_FISH, new Random().nextInt(3) + 1));
        } else if (event.getEntityType() == EntityType.GHAST) {
            for (ItemStack item : event.getDrops()) {
                if (item.getType() == Material.GHAST_TEAR) item.setType(Material.GOLD_INGOT);
            }
        } else if (event.getEntityType() == EntityType.ENDERMAN) {
            for (ItemStack item : event.getDrops()) {
                if (item.getType() == Material.ENDER_PEARL) event.getDrops().remove(item);
                event.getDrops().add(new ItemStack(Material.GOLD_INGOT, new Random().nextInt(2) + 1));
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        final Player p = event.getPlayer();
        p.teleport(new Location(Core.getWorld(), 0, Core.getWorld().getHighestBlockYAt(0, 0), 0));
        p.setGameMode(GameMode.ADVENTURE);
        final ItemStack is = new ItemStack(Material.COMPASS);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.DARK_AQUA + "Teleportation Device");
        im.setLore(Arrays.asList(ChatColor.YELLOW + "Right click me to choose a player to spectate!"));
        is.setItemMeta(im);
        p.setAllowFlight(true);
        p.setGameMode(GameMode.ADVENTURE);
        Bukkit.getScheduler().scheduleSyncDelayedTask(core, new Runnable() {
            @Override
            public void run() {
                p.getInventory().addItem(is);
            }
        }, 10L);
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (Core.getWhitelist().contains(pl.getUniqueId())) {
                if (Core.getWhitelist().contains(p.getUniqueId())) {
                    p.showPlayer(pl);
                    pl.showPlayer(p);
                } else {
                    p.showPlayer(pl);
                    pl.hidePlayer(p);
                }
            } else {
                if (Core.getWhitelist().contains(p.getUniqueId())) {
                    pl.showPlayer(p);
                    p.hidePlayer(pl);
                } else {
                    p.hidePlayer(pl);
                    pl.hidePlayer(p);
                }
            }
        }
    }

}
