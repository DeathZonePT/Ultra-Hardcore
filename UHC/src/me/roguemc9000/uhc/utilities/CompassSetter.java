package me.roguemc9000.uhc.utilities;

import me.roguemc9000.uhc.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Nico Bentley on 10/17/2014.
 */
public class CompassSetter implements Runnable {
    HashMap<String, String> players = new HashMap<String, String>();

    @Override
    public void run() {
        if (Compass.set) {
            for (String pl : players.keySet()) {
                Player p = Bukkit.getPlayer(pl);
                Player p2 = Bukkit.getPlayer(players.get(pl));
                if (p != null && p2 != null) p.setCompassTarget(p2.getLocation());
            }
        }
        Core.run(this);
    }
}
