package de.deroq.bedwars.listeners.bedwars;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.events.BedWarsDropOutEvent;
import de.deroq.bedwars.game.map.models.GameMap;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.game.team.models.GameTeam;
import de.deroq.bedwars.game.team.models.GameTeamType;
import de.deroq.bedwars.stats.models.StatsUser;
import de.deroq.bedwars.utils.BukkitUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author deroq
 * @since 06.07.2022
 */

public class BedWarsDropOutListener implements Listener {

    private final BedWars bedWars;

    public BedWarsDropOutListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onBedWarsDropOut(BedWarsDropOutEvent event) {
        GamePlayer gamePlayer = event.getGamePlayer();
        GameTeam gameTeam = gamePlayer.getGameTeam();

        bedWars.getGameManager().setSpectator(gamePlayer, true);
        bedWars.getGameManager().updateScoreboard();
        gameTeam.getPlayers().remove(gamePlayer.getUuid());
        gamePlayer.setGameTeam(null);
        gamePlayer.getPacketReader().eject();

        StatsUser statsUser = gamePlayer.getStatsUser();
        statsUser.addDeath();
        bedWars.getStatsManager().updateStatsUser(statsUser);

        if(gameTeam.getPlayers().size() != 0) {
            return;
        }

        GameTeamType gameTeamType = gameTeam.getGameTeamType();
        GameMap gameMap = bedWars.getGameManager().getCurrentGameMap();
        gameMap.getGameTeams().remove(gameTeam);

        BukkitUtils.sendBroadcastMessage("Team " + gameTeamType.getColorCode() + gameTeamType.getName() + " §7ist ausgeschieden", true);
        BukkitUtils.sendBroadcastMessage("Verbleibende Teams: §e" + gameMap.getGameTeams().size(), true);

        GameTeam winnerTeam = bedWars.getGameManager().checkForWin();
        if(winnerTeam != null) {
            bedWars.getGameManager().onWin(winnerTeam);
        }
    }
}
