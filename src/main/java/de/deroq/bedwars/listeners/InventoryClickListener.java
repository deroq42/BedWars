package de.deroq.bedwars.listeners;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.utils.GameState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * @author deroq
 * @since 06.07.2022
 */

public class InventoryClickListener implements Listener {

    private final BedWars bedWars;

    public InventoryClickListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();

        if(itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return;
        }

        if(bedWars.getGameManager().getGameState() != GameState.INGAME) {
            if(player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
            }

            if(event.getClickedInventory().getTitle().equals("§8Team auswählen")) {
                bedWars.getGameTeamManager().onTeamSelection(player, itemStack);
            }
            return;
        }

        Optional<GamePlayer> optionalGamePlayer = bedWars.getGameManager().getGamePlayer(player.getUniqueId());
        if (!optionalGamePlayer.isPresent()) {
            return;
        }

        GamePlayer gamePlayer = optionalGamePlayer.get();
        if (gamePlayer.isSpectator()) {
            event.setCancelled(true);
            return;
        }

        if(event.getClickedInventory().getTitle().equals("§8Shop")) {
            event.setCancelled(true);
            bedWars.getGameShopManager().handleInventoryClick(player, itemStack, event.getClick(), event.getHotbarButton());
        }
    }
}
