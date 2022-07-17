package de.deroq.bedwars.game.scoreboard;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.models.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

/**
 * @author deroq
 * @since 08.07.2022
 */

public abstract class GameScoreboard {

    protected final BedWars bedWars;
    protected final Scoreboard scoreboard;
    protected final Objective objective;
    protected final Map<Object, String> usedEntries;

    public GameScoreboard(BedWars bedWars) {
        this.bedWars = bedWars;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("bedwars", "dummy");
        this.usedEntries = new HashMap<>();

        objective.setDisplayName("§f§lGOMMEHD.NET");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public abstract void setScoreboard(GamePlayer gamePlayer);

    public abstract void setTablist();

    public abstract void updateScoreboard();

    public abstract void updateTablist();

    public void destroy(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        gamePlayer.setGameScoreboard(null);
    }

    public String getFreeEntry() {
        for (ChatColor chatColor : ChatColor.values()) {
            String entry = "§" + chatColor.getChar();
            if (usedEntries.containsValue(entry)) {
                continue;
            }

            return entry;
        }

        return null;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Objective getObjective() {
        return objective;
    }

    public Map<Object, String> getUsedEntries() {
        return usedEntries;
    }
}
