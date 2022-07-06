package de.deroq.bedwars.config.models;

import de.deroq.bedwars.config.Config;

import java.io.File;

public class SettingsConfig extends Config {

    private String waitingLobbyLocation;
    private int minPlayers;
    private int maxPlayers;
    private int teamCount;
    private int teamSize;

    private SettingsConfig(File file) {
        super(file.getName());
    }

    public String getWaitingLobbyLocation() {
        return waitingLobbyLocation;
    }

    public void setWaitingLobbyLocation(String waitingLobbyLocation) {
        this.waitingLobbyLocation = waitingLobbyLocation;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public static SettingsConfig create(File file) {
        return new SettingsConfig(file);
    }
}
