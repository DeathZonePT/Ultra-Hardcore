package me.roguemc9000.uhc.listeners;

import me.roguemc9000.uhc.Core;
import me.roguemc9000.uhc.utilities.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Nico Bentley on 7/14/2014.
 */
public class UtilityListeners implements Listener {

    public static boolean locked = false;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if ((Core.getState() != GameState.INGAME && Core.getState() != GameState.GRACE) || !Core.getWhitelist()
                .contains(event.getPlayer().getUniqueId()))
            event.setCancelled(true);
        if (!event.isCancelled()) {
            if (event.getBlock().getType() == Material.LAPIS_ORE) {
                int i = new Random().nextInt(2);
                if (i == 1)
                    event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack
                            (Material.GOLD_INGOT));
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if ((Core.getState() != GameState.INGAME && Core.getState() != GameState.GRACE) || !Core.getWhitelist()
                .contains(event.getPlayer().getUniqueId()))
            event.setCancelled(true);
        if (!event.isCancelled()) {
            if (event.getBlockPlaced().getY() > 230) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Core.getPrefix() + "Thou shalt not build that high!");
            }
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (Core.getState() != GameState.INGAME && Core.getState() != GameState.GRACE) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (Core.getState() != GameState.INGAME && Core.getState() != GameState.GRACE) event.setCancelled(true);
        if (event.getEntity() instanceof Player) {
            if (!Core.getWhitelist().contains(event.getEntity().getUniqueId())) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityShoot(EntityShootBowEvent event) {
        if (Core.getState() != GameState.INGAME && Core.getState() != GameState.GRACE) event.setCancelled(true);
    }

    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent event) {
        if (Core.getState() != GameState.INGAME && Core.getState() != GameState.GRACE) event.setCancelled(true);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if ((Core.getState() != GameState.INGAME && Core.getState() != GameState.GRACE) || !Core.getWhitelist()
                .contains(event.getEntity().getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (Core.getState() != GameState.INGAME && Core.getState() != GameState.GRACE) event.setCancelled(true);
        if (!Core.getWhitelist().contains(event.getPlayer().getUniqueId()) && event.getPlayer().getItemInHand()
                .getType() == Material.COMPASS) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                Inventory inv = Bukkit.createInventory(null, 27, "Player Teleporter");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (Core.getWhitelist().contains(p.getUniqueId())) {
                        ItemStack stack = new ItemStack(Material.SKULL_ITEM);
                        stack.setDurability((short) 3);
                        SkullMeta meta = ((SkullMeta) stack.getItemMeta());
                        meta.setOwner(p.getName());
                        ChatColor color = ChatColor.GREEN;
                        String team = color + "Player";
                        if (Core.getFeature("teaming").isEnabled()) {
                            team = Core.getTeam(p).getColor() + Core.getTeam(p).getDisplayName();
                            color = Core.getTeam(p).getColor();
                        }
                        meta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Team: " + team, ChatColor.DARK_GRAY +
                                "Health: " + color + (int) p.getHealth(), ChatColor.DARK_GRAY + "Hunger: " + color +
                                (int) p.getFoodLevel()));
                        stack.setItemMeta(meta);
                        inv.addItem(stack);
                    }
                }
                event.getPlayer().openInventory(inv);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase("player teleporter")) {
            if (event.getCurrentItem() != null) {
                event.getWhoClicked().teleport(Bukkit.getPlayer(((SkullMeta) event.getCurrentItem().getItemMeta())
                        .getOwner()));
            }
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if ((Core.getState() != GameState.INGAME && Core.getState() != GameState.GRACE) || !Core.getWhitelist()
                .contains(event.getPlayer().getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if ((Core.getState() != GameState.INGAME && Core.getState() != GameState.GRACE) || !Core.getWhitelist()
                .contains(event.getPlayer().getUniqueId()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (Core.getState() == GameState.LOBBY) {
            if (locked && !event.getPlayer().isOp()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Core.getPrefix() + "Chat is currently locked!");
            } else if (!event.getPlayer().isOp())
                event.setFormat(ChatColor.DARK_GRAY + event.getPlayer().getName() + ": " + ChatColor.GRAY + event
                        .getMessage());
            else event.setFormat(ChatColor.GREEN + "OP HOST " + event.getPlayer().getName() + ": " + ChatColor.RED +
                        event.getMessage());

        } else if (locked && !event.getPlayer().isOp()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Core.getPrefix() + "Chat is currently locked!");
        } else if (!Core.getWhitelist().contains(event.getPlayer().getUniqueId())) {
            event.setFormat(ChatColor.GRAY + event.getPlayer().getName() + ": " + ChatColor.WHITE + event.getMessage());
            for (Player player : Bukkit.getOnlinePlayers())
                if (Core.getWhitelist().contains(player.getUniqueId())) event.getRecipients().remove(player);
        } else if (Core.getFeature("teaming").isEnabled()) event.setFormat(Core.getTeam(event.getPlayer()).getColor() +
                Core.getTeam(event.getPlayer()).getDisplayName().toUpperCase() + " " + event.getPlayer().getName() +
                ": " + ChatColor.GRAY + event.getMessage());
        else if (Core.getWhitelist().contains(event.getPlayer().getUniqueId()))
            event.setFormat(ChatColor.GREEN + event.getPlayer().getName() + ChatColor.DARK_GRAY + ": " + ChatColor
                    .GRAY +
                    event.getMessage());
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("Golden Head")) {
            event.getPlayer().setHealth(event.getPlayer().getHealth() + 4);
            if (event.getPlayer().getHealth() > 20) event.getPlayer().setHealth(20);
        }
    }

    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        if (Core.getState() != GameState.GRACE && Core.getState() != GameState.INGAME) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent event) {
        if (Core.getState() != GameState.GRACE && Core.getState() != GameState.INGAME) event.setCancelled(true);
        if (!Core.getWhitelist().contains(event.getPlayer().getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = ((Player) event.getEntity());
            Player damage = ((Player) event.getDamager());
            if (Core.getFeature("teaming").isEnabled()) {
                if (Core.getTeam(player).getMembers().contains(damage.getUniqueId())) {

                    event.setCancelled(true);
                    damage.sendMessage(Core.getPrefix() + "Friendly fire is disabled!");
                }
            }
        } else if (event.getDamager() instanceof Player) {
            if (!Core.getWhitelist().contains(event.getDamager().getUniqueId())) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (Core.getState() != GameState.GRACE && Core.getState() != GameState.INGAME) event.setCancelled(true);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().split(" ");
        if (args[0].equalsIgnoreCase("/me") || args[0].equalsIgnoreCase("/say") ||
                args[0].equalsIgnoreCase("/bukkit:me") || args[0].equalsIgnoreCase("/bukkit:say") ||
                args[0].equalsIgnoreCase("/minecraft:me") || args[0].equalsIgnoreCase("/minecraft:say"))
            event.setCancelled(true);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1);
        if (event.getRecipe().getResult().isSimilar(apple)) event.setCancelled(true);
    }

}
