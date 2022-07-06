package de.deroq.bedwars.game.map.models;

import de.deroq.bedwars.game.team.models.GameTeam;
import de.deroq.bedwars.game.team.models.GameTeamType;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

public class GameMap {

    private final String muid;
    private final String serverGroup;
    private List<String> builders;
    private List<GameTeamType> teams;
    private Map<GameTeamType, String> spawnLocations;
    private Map<GameTeamType, String> bedLocations;
    private Map<GameSpawner, List<String>> itemSpawners;
    private List<String> shopLocations;
    private String spectatorLocation;
    private double maxBuildHeight;

    /**
     * GAME INTERNAL STUFF
     */

    private List<GameTeam> gameTeams;
    private List<Location> placedBlocks;

    //Public constructor due to pojo exceptions.
    public GameMap(String muid, String serverGroup) {
        this.muid = muid;
        this.serverGroup = serverGroup;
    }

    public String getMuid() {
        return muid;
    }

    public String getServerGroup() {
        return serverGroup;
    }

    public List<String> getBuilders() {
        return builders;
    }

    public void setBuilders(List<String> builders) {
        this.builders = builders;
    }

    public List<GameTeamType> getTeams() {
        return teams;
    }

    public void setTeams(List<GameTeamType> teams) {
        this.teams = teams;
    }

    public Map<GameTeamType, String> getSpawnLocations() {
        return spawnLocations;
    }

    public void setSpawnLocations(Map<GameTeamType, String> spawnLocations) {
        this.spawnLocations = spawnLocations;
    }

    public Map<GameTeamType, String> getBedLocations() {
        return bedLocations;
    }

    public void setBedLocations(Map<GameTeamType, String> bedLocations) {
        this.bedLocations = bedLocations;
    }

    public Map<GameSpawner, List<String>> getItemSpawners() {
        return itemSpawners;
    }

    public void setItemSpawners(Map<GameSpawner, List<String>> itemSpawners) {
        this.itemSpawners = itemSpawners;
    }

    public List<String> getShopLocations() {
        return shopLocations;
    }

    public void setShopLocations(List<String> shopLocations) {
        this.shopLocations = shopLocations;
    }

    public String getSpectatorLocation() {
        return spectatorLocation;
    }

    public void setSpectatorLocation(String spectatorLocation) {
        this.spectatorLocation = spectatorLocation;
    }

    public double getMaxBuildHeight() {
        return maxBuildHeight;
    }

    public void setMaxBuildHeight(double maxBuildHeight) {
        this.maxBuildHeight = maxBuildHeight;
    }

    @BsonIgnore
    public List<GameTeam> getGameTeams() {
        return gameTeams;
    }

    public void setGameTeams(List<GameTeam> gameTeams) {
        this.gameTeams = gameTeams;
    }

    @BsonIgnore
    public List<Location> getPlacedBlocks() {
        return placedBlocks;
    }

    public void setPlacedBlocks(List<Location> placedBlocks) {
        this.placedBlocks = placedBlocks;
    }
}
