package de.deroq.bedwars.listeners;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.events.BedWarsDropOutEvent;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.game.team.models.GameTeamType;
import de.deroq.bedwars.stats.models.StatsUser;
import de.deroq.bedwars.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Optional;

/**
 * @author deroq
 * @since 07.07.2022
 */

public class PlayerDeathListener implements Listener {

    private final BedWars bedWars;

    public PlayerDeathListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player killed = event.getEntity();
        event.setDeathMessage(null);
        event.getDrops().clear();

        Optional<GamePlayer> optionalKilledGamePlayer = bedWars.getGameManager().getGamePlayer(killed.getUniqueId());
        if (!optionalKilledGamePlayer.isPresent()) {
            return;
        }

        GamePlayer killedGamePlayer = optionalKilledGamePlayer.get();
        GameTeamType killedGameTeamType = killedGamePlayer.getGameTeam().getGameTeamType();
        Player killer = Bukkit.getPlayer(killedGamePlayer.getLastDamager());
        GamePlayer killerGamePlayer = null;

        if (killer == null) {
            BukkitUtils.sendBroadcastMessage(killedGameTeamType.getColorCode() + killed.getName() + " §7ist gestorben", true);
        } else {
            Optional<GamePlayer> optionalKillerGamePlayer = bedWars.getGameManager().getGamePlayer(killer.getUniqueId());
            if (!optionalKillerGamePlayer.isPresent()) {
                return;
            }

            killerGamePlayer = optionalKillerGamePlayer.get();
            GameTeamType killerGameTeamType = killerGamePlayer.getGameTeam().getGameTeamType();

            BukkitUtils.sendBroadcastMessage(killedGameTeamType.getColorCode() + killed.getName() + " §7wurde von " + killerGameTeamType.getColorCode() + killer.getName() + " §7getötet", true);
            killedGamePlayer.setLastDamager(null);
        }

        bedWars.getGameManager().teleportToSpawn(killedGamePlayer);
        bedWars.getGameManager().spawnShops();

        if (bedWars.getGameManager().checkForDropOut(killedGamePlayer)) {
            Bukkit.getPluginManager().callEvent(new BedWarsDropOutEvent(killedGamePlayer));

            if(killerGamePlayer == null) {
                return;
            }

            StatsUser killerStatsUser = killerGamePlayer.getStatsUser();
            killerStatsUser.addKill();
            killerStatsUser.addPoints(5);
        }
    }
}
