package de.deroq.bedwars.game.scoreboard.ingame;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.scoreboard.GameScoreboard;
import de.deroq.bedwars.game.team.models.GameTeam;
import de.deroq.bedwars.game.team.models.GameTeamType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

/**
 * @author deroq
 * @since 08.07.2022
 */

public class IngameScoreboard extends GameScoreboard {

    public IngameScoreboard(BedWars bedWars) {
        super(bedWars);
    }

    @Override
    public void setScoreboard(Player player) {
        bedWars.getGameManager().getCurrentGameMap().getGameTeams().forEach(gameTeam -> {
            GameTeamType gameTeamType = gameTeam.getGameTeamType();
            Team team = scoreboard.registerNewTeam("bed-" + gameTeamType.getName());
            String bedStatus = (gameTeam.isBedGone() ? "§4✘ " : "§2✔ ");
            String entry = getFreeEntry();

            team.setPrefix(bedStatus);
            team.addEntry(entry);
            team.setSuffix(gameTeamType.getColorCode() + gameTeamType.getName());

            objective.getScore(entry).setScore(gameTeam.getPlayers().size());
            usedEntries.put(gameTeam, entry);
        });

        objective.getScore("§5").setScore(bedWars.getGameManager().TEAM_SIZE + 1);
        player.setScoreboard(scoreboard);
    }

    @Override
    public void setTablist() {
        bedWars.getGameManager().getCurrentGameMap().getGameTeams().forEach(gameTeam -> {
            GameTeamType gameTeamType = gameTeam.getGameTeamType();
            Team team = scoreboard.registerNewTeam("tablist-" + gameTeamType.getName());
            String name = gameTeamType.getName();
            String colorCode = gameTeamType.getColorCode();

            team.setPrefix(colorCode + name + " §7| " + colorCode);
        });

        Team spectatorTeam = scoreboard.registerNewTeam("tablist-spec");
        spectatorTeam.setPrefix("§7");

        bedWars.getGameManager().getGamePlayers().forEach(gamePlayer -> {
            Player player = gamePlayer.getPlayer();
            GameTeam gameTeam = gamePlayer.getGameTeam();

            if (gameTeam == null) {
                spectatorTeam.addEntry(player.getName());
                player.setDisplayName(spectatorTeam.getPrefix() + player.getName());
            } else {
                GameTeamType gameTeamType = gameTeam.getGameTeamType();
                String name = gameTeamType.getName();

                scoreboard.getTeam("tablist-" + name).addEntry(player.getName());
                player.setDisplayName(gameTeamType.getColorCode() + player.getName());
            }
        });
    }

    @Override
    public void updateScoreboard() {
        bedWars.getGameManager().getCurrentGameMap().getGameTeams().forEach(gameTeam -> {
            GameTeamType gameTeamType = gameTeam.getGameTeamType();
            Team team = scoreboard.getTeam("bed-" + gameTeamType.getName());
            String bedStatus = (gameTeam.isBedGone() ? "§4✘ " : "§2✔ ");

            team.setPrefix(bedStatus);
            team.setSuffix(gameTeamType.getColorCode() + gameTeamType.getName());
            objective.getScore(usedEntries.get(gameTeam)).setScore(gameTeam.getPlayers().size());
        });
    }

    @Override
    public void updateTablist() {
        Team spectatorTeam = scoreboard.getTeam("tablist-spec");
        if (spectatorTeam == null) {
            return;
        }

        bedWars.getGameManager().getGamePlayers().forEach(gamePlayer -> {
            Player player = gamePlayer.getPlayer();
            GameTeam gameTeam = gamePlayer.getGameTeam();

            if (gameTeam == null) {
                spectatorTeam.addEntry(player.getName());
                player.setDisplayName(spectatorTeam.getPrefix() + player.getName());
            } else {
                GameTeamType gameTeamType = gameTeam.getGameTeamType();
                scoreboard.getTeam("tablist-" + gameTeamType.getName()).addEntry(player.getName());
                player.setDisplayName(gameTeamType.getColorCode() + player.getName());
            }
        });
    }
}
