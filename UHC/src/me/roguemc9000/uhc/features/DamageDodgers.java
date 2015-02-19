package me.roguemc9000.uhc.features;

import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.utilities.Feature;
import me.roguemc9000.uhc.utilities.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Nico on 12/1/2014.
 */
public class DamageDodgers implements Feature {

    List<UUID> eliminated = new ArrayList();

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
        return "Damage Dodgers";
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (Core.getState() != GameState.LOBBY) {
            if (event.getEntity() instanceof Player && isEnabled() && eliminated.size() < 5) {
                ((Player) event.getEntity()).setHealth(0);
                Bukkit.broadcastMessage(Core.getPrefix() + ((Player) event.getEntity()).getName() +
                        " was one of the first five to take damage and has been eliminated thanks to Damage Dodgers!");
                eliminated.add(event.getEntity().getUniqueId());
            }
        }
    }
}
