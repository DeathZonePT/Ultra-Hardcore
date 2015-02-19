package me.roguemc9000.uhc.listeners;

import me.roguemc9000.uhc.Core;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Nico on 9/4/2014.
 */
public class PotionListeners implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if (inv.getType() == InventoryType.BREWING) {
            if (event.getAction() == InventoryAction.PLACE_ALL || event.getAction() == InventoryAction.PLACE_ONE ||
                    event.getAction() == InventoryAction.PLACE_SOME) {
                Inventory tinv = event.getView().getTopInventory();
                if (tinv.getItem(3).getType() == Material.GLOWSTONE_DUST) {
                    for (int i = 0; i <= 2; i++) {
                        ItemStack stack = tinv.getItem(i);
                        if (stack.getType() == Material.POTION) {
                            PotionMeta meta = (PotionMeta) stack.getItemMeta();
                            if (meta.getCustomEffects().get(0).getType() == PotionEffectType.HEAL || meta
                                    .getCustomEffects().get(0).getType() == PotionEffectType.INCREASE_DAMAGE) {
                                event.setCancelled(true);
                                tinv.setItem(i, null);
                                event.getWhoClicked().getInventory().addItem(new ItemStack(Material.GLOWSTONE_DUST));
                                event.getWhoClicked().closeInventory();
                                ((Player) event.getWhoClicked()).sendMessage(Core.getPrefix() + ChatColor.RED + "You " +
                                        "cannot brew that type " +
                                        "of potion!");
                            }
                        }
                    }
                } else if (tinv.getItem(3).getType() == Material.FERMENTED_SPIDER_EYE) {
                    for (int i = 0; i <= 2; i++) {
                        ItemStack stack = tinv.getItem(i);
                        if (stack.getType() == Material.POTION) {
                            PotionMeta meta = (PotionMeta) stack.getItemMeta();
                            if (meta.getCustomEffects().get(0).getType() == PotionEffectType.HEAL || meta
                                    .getCustomEffects().get(0).getType() == PotionEffectType.WATER_BREATHING || meta
                                    .getCustomEffects().get(0).getType() == PotionEffectType.NIGHT_VISION) {
                                event.setCancelled(true);
                                tinv.setItem(i, null);
                                event.getWhoClicked().getInventory().addItem(new ItemStack(Material
                                        .FERMENTED_SPIDER_EYE));
                                event.getWhoClicked().closeInventory();
                                ((Player) event.getWhoClicked()).sendMessage(Core.getPrefix() + ChatColor.RED + "You " +
                                        "cannot brew that type " +
                                        "of potion!");
                            }
                        }
                    }
                } else if (tinv.getItem(3).getType() == Material.SPIDER_EYE) {
                    for (int i = 0; i <= 2; i++) {
                        ItemStack stack = tinv.getItem(i);
                        if (stack.getType() == Material.POTION) {
                            event.setCancelled(true);
                            tinv.setItem(i, null);
                            event.getWhoClicked().getInventory().addItem(new ItemStack(Material.SPIDER_EYE));
                            event.getWhoClicked().closeInventory();
                            ((Player) event.getWhoClicked()).sendMessage(Core.getPrefix() + ChatColor.RED + "You " +
                                    "cannot brew that type " +
                                    "of potion!");
                        }
                    }
                }
            }
        }

    }

}
