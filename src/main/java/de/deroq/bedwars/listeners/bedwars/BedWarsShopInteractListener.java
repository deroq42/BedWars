package de.deroq.bedwars.listeners.bedwars;

import de.deroq.bedwars.events.BedWarsShopInteractEvent;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BedWarsShopInteractListener implements Listener {

    @EventHandler
    public void onBedWarsShopInteract(BedWarsShopInteractEvent event) {
        GamePlayer gamePlayer = event.getGamePlayer();
        Player player = gamePlayer.getPlayer();
        NPC npc = event.getNpc();

        player.openInventory(Bukkit.createInventory(null, 9, "TEST"));
    }
}
