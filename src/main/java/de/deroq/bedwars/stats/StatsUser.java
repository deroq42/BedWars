package de.deroq.bedwars.stats;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Transient;
import de.deroq.bedwars.utils.Constants;

import java.text.NumberFormat;

@Table(keyspace = "bedwars", name = "stats")
public class StatsUser {

    private String uuid;
    @PartitionKey
    private String name;
    private int points;
    private int kills;
    private int deaths;
    private int games;
    private int wins;
    private int beds;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points) {
        setPoints(getPoints() + points);
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void addKill() {
        setKills(getKills() + 1);
    }

    public int getDeaths() {
        return (deaths == 0 ? 1 : deaths);
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void addDeath() {
        setDeaths(getDeaths() + 1);
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public void addGame() {
        setGames(getGames() + 1);
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void addWin() {
        setWins(getWins() + 1);
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public void addBed() {
        setBeds(getBeds() + 1);
    }

    @Transient
    public double getKD() {
        return (double) kills / deaths;
    }

    @Transient
    public String getFormattedKD() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(getKD()).replace("NaN", "0");
    }

    @Transient
    public double getWinProbability() {
        return (double) wins / games * 100;
    }

    @Transient
    public String getFormattedWinProbability() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(getWinProbability()).replace("NaN", "0");
    }

    @Override
    public String toString() {
        return Constants.PREFIX + "Stats von §6" + name + "\n" +
                "§7Punkte: §6" + points + "\n" +
                "§7Kills: §6" + kills + "\n" +
                "§7Tode: §6" + deaths + "\n" +
                "§7K/D: §6" + getFormattedKD() + "\n" +
                "§7Gespiele Spiele: §6" + games + "\n" +
                "§7Gewonnene Spiele: §6" + wins + "\n" +
                "§7Zerstörte Betten: §6" + beds + "\n" +
                "§7Siegeswahrscheinlichkeit: §6" + getFormattedWinProbability();
    }
}
