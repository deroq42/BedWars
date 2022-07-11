package de.deroq.bedwars.listeners.misc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

/**
 * @author deroq
 * @since 07.07.2022
 */

public class CraftItemListener implements Listener {

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        event.setCancelled(true);
    }
}
