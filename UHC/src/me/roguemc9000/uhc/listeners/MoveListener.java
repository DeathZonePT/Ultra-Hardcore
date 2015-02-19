package me.roguemc9000.uhc.listeners;

import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.utilities.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;

/**
 * Created by Nico on 10/6/2014.
 */
public class MoveListener implements Listener {
    HashMap<String, Location> locs = new HashMap<String, Location>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (Core.getState() == GameState.LOBBY) {
            if (p.getLocation().distance(Bukkit.getWorlds().get(0).getHighestBlockAt(0, 0).getLocation()) >= 35) {
                p.teleport(Bukkit.getWorlds().get(0).getHighestBlockAt(0, 0).getLocation(), PlayerTeleportEvent
                        .TeleportCause.PLUGIN);
                p.sendMessage(Core.getPrefix() + "Don't leave spawn!");
            }
        } else if (Core.getState() == GameState.WARMUP) {
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ())
                p.teleport(event.getFrom());
        }
        if (Core.getFeature("gotohell").isEnabled() && Core.getState() == GameState.INGAME) {
            if (Core.getWhitelist().contains(p.getUniqueId())) {
                if ((((int) event.getPlayer().getLocation().getX()) == -1 || ((int) event.getPlayer().getLocation()
                        .getX()) == 0 || ((int) event.getPlayer().getLocation().getX()) == 1) && (((int) event
                        .getPlayer().getLocation().getZ()) == -1 || ((int) event.getPlayer().getLocation().getZ()) ==
                        0 || ((int) event.getPlayer().getLocation().getZ()) == 1) && event.getPlayer().getLocation()
                        .getY() != 150) {

                    event.getPlayer().sendMessage(Core.getPrefix() + "Teleporting...");
                    event.getPlayer().setAllowFlight(true);
                    event.getPlayer().setFlying(true);
                    event.getPlayer().teleport(Core.getWorld().getBlockAt(0, 150, 0).getLocation());
                    if (Core.getFeature("teaming").isEnabled()) {
                        if (locs.containsKey(Core.getTeam(p).getName()))
                            p.teleport(locs.get(Core.getTeam(p).getName()));
                        else {
                            int x = 0;
                            int z = 0;
                            Boolean acceptable = false;
                            while (!acceptable) {
                                x = Core.getRand().nextInt(Core.getStaticBorder() * 2) - Core.getStaticBorder();
                                z = Core.getRand().nextInt(Core.getStaticBorder() * 2) - Core.getStaticBorder();
                                Location loc = Bukkit.getWorld("world_nether").getHighestBlockAt(x, z).getLocation()
                                        .subtract(0, 1, 0);
                                acceptable = !(loc.distance(Core.getSpawn(Bukkit.getWorld("world_nether"))) < 500 ||
                                        loc.distance(Core.getSpawn(Bukkit.getWorld("world_nether"))) > (Core
                                                .getStaticBorder() - 200)) && (loc.getBlock().getType() == Material
                                        .NETHERRACK ||
                                        loc.getBlock().getType() == Material.BRICK ||
                                        loc.getBlock().getType() == Material.SOUL_SAND ||
                                        loc.add(0, 1, 0).getBlock().getType() != Material.STATIONARY_LAVA ||
                                        loc.add(0, 1, 0).getBlock().getType() == Material.LAVA);
                            }
                            Location location = new Location(Bukkit.getWorld("world_nether"), x, Bukkit.getWorld
                                    ("world_nether").getHighestBlockYAt(x, z), z);
                            location.getChunk().load();
                            Boolean loaded = location.getChunk().isLoaded();
                            while (!loaded) loaded = location.getChunk().isLoaded();
                            p.teleport(location);
                            locs.put(Core.getTeam(p).getName(), location);
                        }
                    } else {
                        int x = 0;
                        int z = 0;
                        Boolean acceptable = false;
                        while (!acceptable) {
                            x = Core.getRand().nextInt(Core.getStaticBorder() * 2) - Core.getStaticBorder();
                            z = Core.getRand().nextInt(Core.getStaticBorder() * 2) - Core.getStaticBorder();
                            Location loc = Bukkit.getWorld("world_nether").getHighestBlockAt(x, z).getLocation()
                                    .subtract(0, 1, 0);
                            acceptable = !(loc.distance(Core.getSpawn(p.getWorld())) < 500 || loc.distance(Core
                                    .getSpawn(p.getWorld())) > (Core.getStaticBorder() - 200)) && (loc.getBlock()
                                    .getType() == Material.NETHERRACK ||
                                    loc.getBlock().getType() == Material.BRICK ||
                                    loc.getBlock().getType() == Material.SOUL_SAND ||
                                    loc.add(0, 1, 0).getBlock().getType() != Material.STATIONARY_LAVA ||
                                    loc.add(0, 1, 0).getBlock().getType() == Material.LAVA);
                        }
                        Location location = new Location(Bukkit.getWorld("world_nether"), x, Bukkit.getWorld
                                ("world_nether").getHighestBlockYAt(x, z), z);
                        location.getChunk().load();
                        Boolean loaded = location.getChunk().isLoaded();
                        while (!loaded) loaded = location.getChunk().isLoaded();
                        p.teleport(location);
                    }
                    p.setAllowFlight(false);
                    p.setFlying(false);
                }
            }
        }
    }

}
