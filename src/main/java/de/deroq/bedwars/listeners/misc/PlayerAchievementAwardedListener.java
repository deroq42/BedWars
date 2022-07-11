package de.deroq.bedwars.listeners.misc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

/**
 * @author deroq
 * @since 07.07.2022
 */

public class PlayerAchievementAwardedListener implements Listener {

    @EventHandler
    public void onPlayerAchievementAwarded(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }
}
