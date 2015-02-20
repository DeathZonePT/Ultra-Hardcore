package me.roguemc9000.uhc.listeners;

import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.utilities.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Nico Bentley on 7/15/2014.
 */
public class JoinListeners implements Listener {

    HashMap<String, Integer> times = new HashMap<String, Integer>();
    private Core core;

    public JoinListeners(Core instance) {
        core = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Core.getState() == GameState.LOBBY) {
            event.setJoinMessage(Core.getPrefix() + event.getPlayer().getName() + " has joined UHC! " + ChatColor
                    .DARK_GRAY + "[" +
                    ChatColor.RED + Bukkit.getOnlinePlayers().size() + ChatColor.DARK_GRAY + "]");
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!(p.canSee(player))) p.showPlayer(player);
                if (!(player.canSee(p))) player.showPlayer(p);
            }
        } else {
            String team = ChatColor.GREEN + "Player";
            if (!Core.getWhitelist().contains(player.getUniqueId())) {
                team = ChatColor.GRAY + "Spectator";
                ItemStack is = new ItemStack(Material.COMPASS);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(ChatColor.DARK_AQUA + "Teleportation Device");
                im.setLore(Arrays.asList(ChatColor.YELLOW + "Right click me to choose a player to spectate!"));
                is.setItemMeta(im);
                player.getInventory().addItem(is);
                player.setAllowFlight(true);
                player.setGameMode(GameMode.ADVENTURE);
            } else if (Core.getFeature("teaming").isEnabled())
                team = Core.getTeam(player).getColor() + Core.getTeam(player).getName();

            event.setJoinMessage(Core.getPrefix() + player.getName() + " has rejoined UHC! (" + team + ChatColor.GRAY
                    + ")");
        }
        if (times.containsKey(player.getName())) times.remove(player.getName());
        if (Core.getState() != GameState.LOBBY) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (Core.getWhitelist().contains(player.getUniqueId())) {
                    if (Core.getWhitelist().contains(p.getUniqueId())) {
                        p.showPlayer(player);
                        player.showPlayer(p);
                    } else {
                        p.showPlayer(player);
                        player.hidePlayer(p);
                    }
                } else {
                    if (Core.getWhitelist().contains(p.getUniqueId())) {
                        player.showPlayer(p);
                        p.hidePlayer(player);
                    } else {
                        p.hidePlayer(player);
                        player.hidePlayer(p);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        event.setQuitMessage(Core.getPrefix() + event.getPlayer().getName() + " has left UHC!");
        if (Core.getState() != GameState.LOBBY && Core.getWhitelist().contains(event.getPlayer().getUniqueId())) {
            times.put(event.getPlayer().getName(), Core.getSeconds());
            Bukkit.getScheduler().scheduleSyncDelayedTask(core, new Runnable() {
                @Override
                public void run() {
                    if (times.containsKey(event.getPlayer().getName()) && Core.getSeconds() - times.get(event
                            .getPlayer().getName()) >= 600) {
                        Core.getWhitelist().remove(event.getPlayer().getUniqueId());
                        Bukkit.broadcastMessage(Core.getPrefix() + event.getPlayer().getName() +
                                " has been gone for 10 minutes and has been eliminated!");
                    }
                }
            }, 12020L);
        }
    }

}
