package de.deroq.bedwars.game.scoreboard.lobby;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.game.scoreboard.GameScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

/**
 * @author deroq
 * @since 08.07.2022
 */

public class LobbyScoreboard extends GameScoreboard {

    public LobbyScoreboard(BedWars bedWars) {
        super(bedWars);
    }

    @Override
    public void setScoreboard(GamePlayer gamePlayer) {
        Team mapTeam = scoreboard.registerNewTeam("map");
        mapTeam.setPrefix("§fMap: ");
        mapTeam.addEntry("§e");
        mapTeam.setSuffix("§e" + bedWars.getGameManager().getCurrentGameMap().getMuid());

        objective.getScore("§5").setScore(3);
        objective.getScore("§e").setScore(2);
        objective.getScore("§fGröße: §b" + bedWars.getGameManager().TEAM_COUNT + "x" + bedWars.getGameManager().TEAM_SIZE).setScore(1);
        objective.getScore("§a").setScore(0);

        gamePlayer.setGameScoreboard(this);
        gamePlayer.getPlayer().setScoreboard(scoreboard);
    }

    @Override
    public void setTablist() {
        Team devTeam = scoreboard.registerNewTeam("0000Dev");
        devTeam.setPrefix("§cDev §7| §c");

        Team playerTeam = scoreboard.registerNewTeam("0001Player");
        playerTeam.setPrefix("§a");

        Bukkit.getOnlinePlayers().forEach(players -> {
            if(players.isOp()) {
                devTeam.addEntry(players.getName());
                players.setDisplayName(devTeam.getPrefix() + players.getName());
            } else {
                playerTeam.addEntry(players.getName());
                players.setDisplayName(playerTeam.getPrefix() + players.getName());
            }
        });
    }

    @Override
    public void updateScoreboard() {
        Team mapTeam = scoreboard.getTeam("map");
        if(mapTeam == null) {
            return;
        }

        mapTeam.setPrefix("§fMap: ");
        mapTeam.setSuffix("§e" + bedWars.getGameManager().getCurrentGameMap().getMuid());
    }

    @Override
    public void updateTablist() {
        Team devTeam = scoreboard.getTeam("0000Dev");
        Team playerTeam = scoreboard.getTeam("0001Player");

        if(devTeam == null || playerTeam == null) {
            return;
        }

        Bukkit.getOnlinePlayers().forEach(players -> {
            if(players.isOp()) {
                devTeam.addEntry(players.getName());
                players.setDisplayName(devTeam.getPrefix() + players.getName());
            } else {
                playerTeam.addEntry(players.getName());
                players.setDisplayName(playerTeam.getPrefix() + players.getName());
            }
        });
    }
}
