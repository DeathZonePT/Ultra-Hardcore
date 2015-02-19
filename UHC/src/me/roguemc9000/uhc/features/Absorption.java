package me.roguemc9000.uhc.features;

import me.roguemc9000.uhc.utilities.Feature;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Nico on 12/1/2014.
 */
public class Absorption implements Feature, Runnable {

    Boolean enabled = false;

    @Override
    public Boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getName() {
        return "Absorption";
    }

    @Override
    public void run() {
        if (!isEnabled()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPotionEffect(PotionEffectType.ABSORPTION)) p.removePotionEffect(PotionEffectType.ABSORPTION);
            }
        }
    }
}
