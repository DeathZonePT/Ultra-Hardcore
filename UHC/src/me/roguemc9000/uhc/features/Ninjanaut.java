package me.roguemc9000.uhc.features;

import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.utilities.Feature;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

/**
 * Created by Nico on 12/1/2014.
 */
public class Ninjanaut implements Feature {

    private static UUID ninja;

    Boolean enabled = false;

    public static UUID getNinja() {
        return ninja;
    }

    public static void setNinja(UUID ninja) {
        Ninjanaut.ninja = ninja;
    }

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
        return "Ninjanaut";
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (isEnabled() && event.getEntity().getKiller() != null && event.getEntity().getUniqueId() == getNinja()) {
            Bukkit.broadcastMessage(Core.getPrefix() + "The ninja has been killed!");
            Bukkit.broadcastMessage(Core.getPrefix() + event.getEntity().getKiller().getName() + " is the new ninja!");
            event.getEntity().getKiller().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE,
                    2));
            event.getEntity().getKiller().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer
                    .MAX_VALUE, 2));
            setNinja(event.getEntity().getKiller().getUniqueId());
        }
    }

}
