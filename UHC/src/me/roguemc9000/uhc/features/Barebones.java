package me.roguemc9000.uhc.features;

import me.roguemc9000.uhc.utilities.Feature;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Nico on 12/2/2014.
 */
public class Barebones implements Feature {

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
        return "Barebones";
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Material type = event.getBlock().getType();
        if (isEnabled() && (type == Material.DIAMOND_ORE || type == Material.EMERALD_ORE ||
                type == Material.GLOWING_REDSTONE_ORE || type == Material.GOLD_ORE || type == Material.LAPIS_ORE ||
                type == Material.QUARTZ_ORE || type == Material.REDSTONE_ORE)) {
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material
                    .IRON_INGOT));
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (isEnabled() && (event.getRecipe().getResult().getType() == Material.ANVIL || event.getRecipe().getResult
                ().getType() == Material.ENCHANTMENT_TABLE)) {
            event.setResult(Event.Result.DENY);
        }
    }

}
