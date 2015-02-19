package me.roguemc9000.uhc.features;

import me.roguemc9000.uhc.utilities.Feature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

/**
 * Created by Nico on 12/1/2014.
 */
public class GoToHell implements Feature, Listener {


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
        return "Go to Hell";
    }

    @EventHandler
    public void onPortal(PortalCreateEvent event) {
        if (isEnabled()) {
            event.setCancelled(true);
        }
    }

}
