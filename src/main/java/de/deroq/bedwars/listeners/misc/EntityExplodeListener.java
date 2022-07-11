package de.deroq.bedwars.listeners.misc;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.utils.Constants;
import de.deroq.bedwars.utils.GameState;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;

/**
 * @author deroq
 * @since 07.07.2022
 */

public class EntityExplodeListener implements Listener {

    private final BedWars bedWars;

    public EntityExplodeListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if(bedWars.getGameManager().getGameState() != GameState.INGAME) {
            event.setCancelled(true);
            return;
        }

        if(!(event.getEntity() instanceof TNTPrimed)) {
            return;
        }

        List<Block> blocks = event.blockList();
        blocks.removeIf(block -> !bedWars.getGameManager().BREAKABLE_BLOCKS.contains(block.getType()));
    }
}
