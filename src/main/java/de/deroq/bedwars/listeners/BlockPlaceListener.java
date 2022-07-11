package de.deroq.bedwars.listeners;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.utils.Constants;
import de.deroq.bedwars.utils.GameState;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Optional;

/**
 * @author deroq
 * @since 07.07.2022
 */

public class BlockPlaceListener implements Listener {

    private final BedWars bedWars;

    public BlockPlaceListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (bedWars.getGameManager().getGameState() != GameState.INGAME) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
            }
        } else {
            if(!bedWars.getGameManager().PLACEABLE_BLOCKS.contains(block.getType())) {
                if(player.getGameMode() != GameMode.CREATIVE) {
                    event.setCancelled(true);
                }
                return;
            }

            Optional<GamePlayer> optionalGamePlayer = bedWars.getGameManager().getGamePlayer(player.getUniqueId());
            if(!optionalGamePlayer.isPresent()) {
                return;
            }

            GamePlayer gamePlayer = optionalGamePlayer.get();
            if(gamePlayer.isSpectator()) {
                event.setCancelled(true);
                return;
            }

            bedWars.getGameManager().getCurrentGameMap().getPlacedBlocks().add(block.getLocation());
        }
    }
}
