package de.deroq.bedwars.listeners;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.utils.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Optional;

/**
 * @author deroq
 * @since 07.07.2022
 */

public class EntityDamageListener implements Listener {

    private final BedWars bedWars;

    public EntityDamageListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(bedWars.getGameManager().getGameState() != GameState.INGAME) {
            event.setCancelled(true);
            return;
        }

        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Optional<GamePlayer> optionalGamePlayer = bedWars.getGameManager().getGamePlayer(player.getUniqueId());
        if(!optionalGamePlayer.isPresent()) {
            return;
        }

        GamePlayer gamePlayer = optionalGamePlayer.get();
        if(gamePlayer.isSpectator()) {
            event.setCancelled(true);
        }
    }
}
