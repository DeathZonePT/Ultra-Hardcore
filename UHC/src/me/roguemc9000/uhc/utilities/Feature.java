package me.roguemc9000.uhc.utilities;


/**
 * Created by Nico on 11/30/2014.
 */
public interface Feature extends org.bukkit.event.Listener {
    public Boolean isEnabled();

    public void setEnabled(Boolean enabled);

    public String getName();
}
