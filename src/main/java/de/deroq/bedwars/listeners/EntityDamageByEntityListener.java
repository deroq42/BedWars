package de.deroq.bedwars.listeners;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.utils.GameState;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Optional;

/**
 * @author deroq
 * @since 07.07.2022
 */

public class EntityDamageByEntityListener implements Listener {

    private final BedWars bedWars;

    public EntityDamageByEntityListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (bedWars.getGameManager().getGameState() != GameState.INGAME) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player damaged = (Player) event.getEntity();
        Player damager = null;

        Optional<GamePlayer> optionalDamagedGamePlayer = bedWars.getGameManager().getGamePlayer(damaged.getUniqueId());
        if (!optionalDamagedGamePlayer.isPresent()) {
            return;
        }

        GamePlayer damagedGamePlayer = optionalDamagedGamePlayer.get();
        if (damagedGamePlayer.isSpectator()) {
            event.setCancelled(true);
            return;
        }

        if (event.getCause() == EntityDamageByEntityEvent.DamageCause.ENTITY_ATTACK) {
            if (!(event.getDamager() instanceof Player)) {
                return;
            }

            damager = (Player) event.getDamager();

        } else if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            if (!(event.getDamager() instanceof Arrow)) {
                return;
            }

            Arrow arrow = (Arrow) event.getDamager();
            damager = (Player) arrow.getShooter();
        }

        if (damager == null) {
            return;
        }

        Optional<GamePlayer> optionalDamagerGamePlayer = bedWars.getGameManager().getGamePlayer(damager.getUniqueId());
        if (!optionalDamagerGamePlayer.isPresent()) {
            return;
        }

        GamePlayer damagerGamePlayer = optionalDamagerGamePlayer.get();
        if (damagerGamePlayer.isSpectator()) {
            event.setCancelled(true);
            return;
        }

        if (damagerGamePlayer.getGameTeam().equals(damagedGamePlayer.getGameTeam())) {
            event.setCancelled(true);
            return;
        }

        damagedGamePlayer.setLastDamager(damager.getUniqueId());
    }
}
