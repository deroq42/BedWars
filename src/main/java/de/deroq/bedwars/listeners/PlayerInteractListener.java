package de.deroq.bedwars.listeners;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.utils.Constants;
import de.deroq.bedwars.utils.GameState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    private final BedWars bedWars;

    public PlayerInteractListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();

            if(block.getType() == Material.BED_BLOCK) {
                event.setCancelled(true);
                return;
            }

            if(bedWars.getGameManager().getGameState() != GameState.INGAME) {
                if(block instanceof InventoryHolder) {
                    event.setCancelled(true);
                }
            }
            return;
        }

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemStack = event.getItem();

            if(itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
                return;
            }

            if(itemStack.isSimilar(Constants.TEAM_SELECTION_ITEM)) {
                bedWars.getGameTeamManager().openTeamSelectionInventory(player);
                return;
            }

            if(itemStack.isSimilar(Constants.LOBBY_ITEM)) {
                player.kickPlayer("");
                return;
            }
        }
    }
}
