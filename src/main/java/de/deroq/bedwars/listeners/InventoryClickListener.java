package de.deroq.bedwars.listeners;

import de.deroq.bedwars.BedWars;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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

        if(event.getClickedInventory().getTitle().equals("§8Team auswählen")) {
            bedWars.getGameTeamManager().onTeamSelection(player, itemStack);
            return;
        }
    }
}
