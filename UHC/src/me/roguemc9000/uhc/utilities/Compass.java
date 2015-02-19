package me.roguemc9000.uhc.utilities;

import me.roguemc9000.uhc.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * Created by Nico on 10/6/2014.
 */
public class Compass implements Runnable {

    public static boolean set = true;
    Random ran = Core.getRand();

    @Override
    public void run() {
        Bukkit.broadcastMessage(Core.getPrefix() + "Compasses are now live!");
        Bukkit.broadcastMessage(Core.getPrefix() + "They will point to the nearest enemy!");
        set = true;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Core.getWhitelist().contains(player.getUniqueId())) {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, .5F, 1F);
                double distance = Core.getStaticBorder() * 2;
                Location closest = null;
                Player pl = null;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (Core.getWhitelist().contains(p.getUniqueId())) {
                        double d = player.getLocation().distance(p.getLocation());
                        if (p != player) {
                            if (d < distance) {
                                if (Core.getFeature("teaming").isEnabled() && Core.getTeam(p) == Core.getTeam(pl))
                                    continue;
                                distance = d;
                                closest = p.getLocation();
                                pl = p;
                            }
                        }
                    }
                }
                if (Core.getFeature("teaming").isEnabled())
                    player.sendMessage(Core.getPrefix() + "The team closest to you is " + Core.getTeam(pl).getColor() +
                            Core.getTeam(pl).getDisplayName() + ChatColor.GRAY + "!");
                else
                    player.sendMessage(Core.getPrefix() + "The player closest to you is " + ChatColor.GREEN + pl
                            .getName() +
                            ChatColor.GRAY + "!");
                player.setCompassTarget(closest);
            }
        }
        try {
            Thread.sleep((long) ((ran.nextInt(15) + 15) * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Bukkit.broadcastMessage(Core.getPrefix() + "Compasses are no longer live!");
        Bukkit.broadcastMessage(Core.getPrefix() + "Your compass will point to that enemy's last known location!");
        set = false;
        for (Player p : Bukkit.getOnlinePlayers())
            p.playSound(p.getLocation(), Sound.NOTE_PLING, .5F, 1F);
        try {
            Thread.sleep((long) ((ran.nextInt(420) + 480) * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Core.run(this);
    }
}
