package me.roguemc9000.uhc.features;

import me.roguemc9000.uhc.utilities.Feature;

/**
 * Created by Nico on 12/1/2014.
 */
public class iPVP implements Feature {

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
        return "iPVP";
    }

}
