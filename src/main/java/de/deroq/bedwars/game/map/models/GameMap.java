package de.deroq.bedwars.game.map.models;

import de.deroq.bedwars.game.team.models.GameTeam;
import de.deroq.bedwars.game.team.models.GameTeamType;
import de.deroq.bedwars.npc.NPC;
import de.deroq.bedwars.utils.Constants;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMap {

    private String muid;
    private String serverGroup;
    private List<String> builders;
    private List<String> teams;
    private Map<String, String> spawnLocations;
    private Map<String, String> bedLocations;
    private Map<String, List<String>> itemSpawners;
    private List<String> shopLocations;
    private String spectatorLocation;
    private double maxBuildHeight;

    /* GAME INTERNAL STUFF */

    private List<GameTeam> gameTeams;
    private List<Location> placedBlocks;
    private List<NPC> shops;

    /* Public constructor due to pojo exceptions. */
    public GameMap(String muid, String serverGroup) {
        this.muid = muid;
        this.serverGroup = serverGroup;

        this.builders = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.spawnLocations = new HashMap<>();
        this.bedLocations = new HashMap<>();
        this.itemSpawners = new HashMap<>();
        this.shopLocations = new ArrayList<>();
    }

    /* Public constructor due to pojo exceptions. */
    public GameMap() {

    }

    public String getMuid() {
        return muid;
    }

    public void setMuid(String muid) {
        this.muid = muid;
    }

    public String getServerGroup() {
        return serverGroup;
    }

    public void setServerGroup(String serverGroup) {
        this.serverGroup = serverGroup;
    }

    public List<String> getBuilders() {
        return builders;
    }

    public void setBuilders(List<String> builders) {
        this.builders = builders;
    }

    public List<String> getTeams() {
        return teams;
    }

    public void setTeams(List<String> teams) {
        this.teams = teams;
    }

    public Map<String, String> getSpawnLocations() {
        return spawnLocations;
    }

    public void setSpawnLocations(Map<String, String> spawnLocations) {
        this.spawnLocations = spawnLocations;
    }

    public Map<String, String> getBedLocations() {
        return bedLocations;
    }

    public void setBedLocations(Map<String, String> bedLocations) {
        this.bedLocations = bedLocations;
    }

    public Map<String, List<String>> getItemSpawners() {
        return itemSpawners;
    }

    public void setItemSpawners(Map<String, List<String>> itemSpawners) {
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

    @BsonIgnore
    public List<NPC> getShops() {
        return shops;
    }

    public void setShops(List<NPC> shops) {
        this.shops = shops;
    }
}
