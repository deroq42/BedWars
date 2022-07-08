package de.deroq.bedwars.game.scoreboard.ingame;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.scoreboard.GameScoreboard;
import de.deroq.bedwars.game.team.models.GameTeam;
import de.deroq.bedwars.game.team.models.GameTeamType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class IngameScoreboard extends GameScoreboard {

    private final Map<GameTeam, String> usedEntries;

    public IngameScoreboard(BedWars bedWars) {
        super(bedWars);
        this.usedEntries = new HashMap<>();
    }

    @Override
    public void setScoreboard(Player player) {
        bedWars.getGameManager().getCurrentGameMap().getGameTeams().forEach(gameTeam -> {
            GameTeamType gameTeamType = gameTeam.getGameTeamType();
            Team team = scoreboard.registerNewTeam("bed-" +gameTeamType.getName());
            String bedStatus = (gameTeam.isBedGone() ? "§4✘ " : "§2✔ ");
            String entry = getFreeEntry();

            team.setPrefix(bedStatus);
            team.addEntry(entry);
            team.setSuffix(gameTeamType.getColorCode() + gameTeamType.getName());

            objective.getScore(entry).setScore(gameTeam.getPlayers().size());
            usedEntries.put(gameTeam, entry);
        });

        objective.getScore("§5§l").setScore(bedWars.getGameManager().TEAM_SIZE + 1);
        player.setScoreboard(scoreboard);
    }

    @Override
    public void setTablist(Player player) {
        bedWars.getGameManager().getCurrentGameMap().getGameTeams().forEach(gameTeam -> {
            GameTeamType gameTeamType = gameTeam.getGameTeamType();
            Team team = scoreboard.registerNewTeam("tablist-" + gameTeamType.getName());
            team.setPrefix(gameTeamType.getColorCode() + gameTeamType.getName() + " §7| " + gameTeamType.getColorCode());
        });

        Team spectatorTeam = scoreboard.registerNewTeam("tablist-spec");
        spectatorTeam.setPrefix("§7");

        bedWars.getGameManager().getGamePlayers().forEach(gamePlayer -> {
            GameTeam gameTeam = gamePlayer.getGameTeam();

            if(gameTeam == null) {
                spectatorTeam.addEntry(gamePlayer.getPlayer().getName());
            } else {
                GameTeamType gameTeamType = gameTeam.getGameTeamType();
                scoreboard.getTeam("tablist-" + gameTeamType.getName()).addEntry(gamePlayer.getPlayer().getName());
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

        if(spectatorTeam == null) {
            return;
        }

        bedWars.getGameManager().getGamePlayers().forEach(gamePlayer -> {
            GameTeam gameTeam = gamePlayer.getGameTeam();

            if(gameTeam == null) {
                spectatorTeam.addEntry(gamePlayer.getPlayer().getName());
            } else {
                scoreboard.getTeam("tablist-" + gameTeam.getGameTeamType().getName()).addEntry(gamePlayer.getPlayer().getName());
            }
        });
    }

    private String getFreeEntry() {
        for (ChatColor chatColor : ChatColor.values()) {
            String entry = "§" + chatColor.getChar();
            if (usedEntries.containsValue(entry)) {
                continue;
            }

            return entry;
        }

        return null;
    }
}
