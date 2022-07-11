package de.deroq.bedwars.game.team.models;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author deroq
 * @since 06.07.2022
 */

public class GameTeam {

    private final GameTeamType gameTeamType;
    private final Location spawnLocation;
    private final Location bedLocation;
    private List<UUID> players;
    private boolean bedGone;

    private GameTeam(GameTeamType gameTeamType, Location spawnLocation, Location bedLocation) {
        this.gameTeamType = gameTeamType;
        this.spawnLocation = spawnLocation;
        this.bedLocation = bedLocation;
        this.players = new ArrayList<>();
        this.bedGone = false;
    }

    public GameTeamType getGameTeamType() {
        return gameTeamType;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Location getBedLocation() {
        return bedLocation;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public void setPlayers(List<UUID> players) {
        this.players = players;
    }

    public boolean isBedGone() {
        return bedGone;
    }

    public void setBedGone(boolean bedGone) {
        this.bedGone = bedGone;
    }

    public static GameTeam create(GameTeamType gameTeamType, Location spawnLocation, Location bedLocation) {
        return new GameTeam(gameTeamType, spawnLocation, bedLocation);
    }
}
