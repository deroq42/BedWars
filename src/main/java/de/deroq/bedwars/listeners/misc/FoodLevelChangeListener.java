package de.deroq.bedwars.listeners.misc;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.utils.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.Optional;

public class FoodLevelChangeListener implements Listener {

    private final BedWars bedWars;

    public FoodLevelChangeListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();

        if(bedWars.getGameManager().getGameState() != GameState.INGAME) {
            event.setCancelled(true);
            return;
        }

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
