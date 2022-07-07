package de.deroq.bedwars.listeners;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.events.BedWarsDropOutEvent;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.game.team.models.GameTeamType;
import de.deroq.bedwars.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Optional;

public class PlayerDeathListener implements Listener {

    private final BedWars bedWars;

    public PlayerDeathListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player killed = event.getEntity();
        Player killer = killed.getKiller();
        event.setDeathMessage(null);

        Optional<GamePlayer> optionalKilledGamePlayer = bedWars.getGameManager().getGamePlayer(killed.getUniqueId());
        if(!optionalKilledGamePlayer.isPresent()) {
            return;
        }

        GamePlayer killedGamePlayer = optionalKilledGamePlayer.get();
        GameTeamType killedGameTeamType = killedGamePlayer.getGameTeam().getGameTeamType();

        if(killer == null) {
            BukkitUtils.sendBroadcastMessage(killedGameTeamType.getColorCode() + killed.getName() + " §7ist gestorben");
        } else {
            Optional<GamePlayer> optionalKillerGamePlayer = bedWars.getGameManager().getGamePlayer(killer.getUniqueId());
            if(!optionalKillerGamePlayer.isPresent()) {
                return;
            }

            GamePlayer killerGamePlayer = optionalKillerGamePlayer.get();
            GameTeamType killerGameTeamType = killerGamePlayer.getGameTeam().getGameTeamType();
            BukkitUtils.sendBroadcastMessage(killedGameTeamType.getColorCode() + killed.getName() + " §7wurde von " + killerGameTeamType.getColorCode() + killer.getName() + " §7getötet");
        }

        bedWars.getGameManager().teleportToSpawn(killed);

        if(bedWars.getGameManager().checkForDropOut(killedGamePlayer)) {
            Bukkit.getPluginManager().callEvent(new BedWarsDropOutEvent(killedGamePlayer));

            //ADD KILL...
        }
    }
}
