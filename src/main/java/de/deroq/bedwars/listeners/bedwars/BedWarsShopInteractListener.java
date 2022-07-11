package de.deroq.bedwars.listeners.bedwars;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.events.BedWarsShopInteractEvent;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.npc.NPC;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author deroq
 * @since 09.07.2022
 */

public class BedWarsShopInteractListener implements Listener {

    private final BedWars bedWars;

    public BedWarsShopInteractListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onBedWarsShopInteract(BedWarsShopInteractEvent event) {
        GamePlayer gamePlayer = event.getGamePlayer();
        Player player = gamePlayer.getPlayer();
        NPC npc = event.getNpc();

        if(npc.getName().equals("§6§lShop")) {
            player.openInventory(bedWars.getGameShopManager().getMainInventory());
        }
    }
}
