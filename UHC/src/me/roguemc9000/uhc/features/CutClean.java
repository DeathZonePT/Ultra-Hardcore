package me.roguemc9000.uhc.features;

import me.roguemc9000.uhc.utilities.Feature;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Nico on 12/1/2014.
 */
public class CutClean implements Feature {

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
        return "CutClean";
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isEnabled()) {
            if (event.getBlock().getType() == Material.IRON_ORE) {
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
                event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material
                        .IRON_INGOT));
                event.getPlayer().giveExp(7);
            } else if (event.getBlock().getType() == Material.GOLD_ORE) {
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
                event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material
                        .GOLD_INGOT));
                event.getPlayer().giveExp(10);
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (isEnabled()) {
            for (ItemStack item : event.getDrops()) {
                if (item.getType() == Material.RAW_BEEF) {
                    event.getDrops().remove(item);
                    event.getDrops().add(new ItemStack(Material.COOKED_BEEF));
                } else if (item.getType() == Material.RAW_CHICKEN) {
                    event.getDrops().remove(item);
                    event.getDrops().add(new ItemStack(Material.COOKED_CHICKEN));
                } else if (item.getType() == Material.RAW_FISH) {
                    event.getDrops().remove(item);
                    event.getDrops().add(new ItemStack(Material.COOKED_FISH));
                } else if (item.getType() == Material.PORK) {
                    event.getDrops().remove(item);
                    event.getDrops().add(new ItemStack(Material.GRILLED_PORK));
                }
            }
        }
    }

}
