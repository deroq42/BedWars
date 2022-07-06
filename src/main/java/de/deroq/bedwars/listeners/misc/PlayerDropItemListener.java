package de.deroq.bedwars.listeners.misc;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.utils.GameState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.Optional;

public class PlayerDropItemListener implements Listener {

    private final BedWars bedWars;

    public PlayerDropItemListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (bedWars.getGameManager().getGameState() != GameState.INGAME) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
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
        }
    }
}
