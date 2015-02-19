package me.roguemc9000.uhc.listeners;

import me.roguemc9000.uhc.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Nico on 1/27/2015.
 */
public class AntiSpam implements Listener {

    HashMap<UUID, String> messages = new HashMap();
    List<String> curses = Arrays.asList("fuck", "fcuk", "fkuc", "shit", "damn", "dam", "cunt", "cutn", "dick",
            "dcik", "dkic", "pussy", "pssy", "pusy");

    @EventHandler (priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent event) {
        if (messages.containsKey(event.getPlayer().getUniqueId())) {
            for (String curse : curses)
                if (event.getMessage().replaceAll(" ", "").replaceAll(".", "").replace("*", "").replaceAll(",", "")
                        .toLowerCase().contains(curse))
                    event.getMessage().replaceAll(curse, "****");
            if (messages.get(event.getPlayer().getUniqueId()).toLowerCase().contains(event.getMessage().toLowerCase()
            ) || event.getMessage().toLowerCase().contains(messages.get(event.getPlayer().getUniqueId()).toLowerCase
                    ()) ||
                    event.getMessage().equalsIgnoreCase(messages.get(event.getPlayer().getUniqueId()))) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Core.getPrefix() + "Please do not repeat yourself :)");
            }
        }
        messages.put(event.getPlayer().getUniqueId(), event.getMessage());
    }

}
