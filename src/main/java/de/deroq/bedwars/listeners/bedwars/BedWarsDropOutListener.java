package de.deroq.bedwars.listeners.bedwars;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.events.BedWarsDropOutEvent;
import de.deroq.bedwars.game.map.models.GameMap;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.game.team.models.GameTeam;
import de.deroq.bedwars.game.team.models.GameTeamType;
import de.deroq.bedwars.utils.BukkitUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BedWarsDropOutListener implements Listener {

    private final BedWars bedWars;

    public BedWarsDropOutListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onBedWarsDropOut(BedWarsDropOutEvent event) {
        GamePlayer gamePlayer = event.getGamePlayer();
        GameTeam gameTeam = gamePlayer.getGameTeam();

        bedWars.getGameManager().setSpectator(gamePlayer.getPlayer(), true);
        gameTeam.getPlayers().remove(gamePlayer.getUuid());

        if(gameTeam.getPlayers().size() != 0) {
            return;
        }

        GameTeamType gameTeamType = gameTeam.getGameTeamType();
        GameMap gameMap = bedWars.getGameManager().getCurrentGameMap();
        gameMap.getGameTeams().remove(gameTeam);

        BukkitUtils.sendBroadcastMessage("Team " + gameTeamType.getColorCode() + gameTeamType.getName() + " §7ist ausgeschieden");
        BukkitUtils.sendBroadcastMessage("Verbleibende Teams: §e" + gameMap.getGameTeams().size());

        GameTeam winnerTeam = bedWars.getGameManager().checkForWin();
        if(winnerTeam != null) {
            bedWars.getGameManager().onWin(winnerTeam);
        }
    }
}
