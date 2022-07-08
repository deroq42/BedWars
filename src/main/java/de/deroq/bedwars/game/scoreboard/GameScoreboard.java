package de.deroq.bedwars.game.scoreboard;

import de.deroq.bedwars.BedWars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public abstract class GameScoreboard {

    protected final BedWars bedWars;
    protected final Scoreboard scoreboard;
    protected final Objective objective;

    public GameScoreboard(BedWars bedWars) {
        this.bedWars = bedWars;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("bedwars", "dummy");
        objective.setDisplayName("  §f§lGOMMEHD.NET  ");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public abstract void setScoreboard(Player player);

    public abstract void setTablist();

    public abstract void updateScoreboard();

    public abstract void updateTablist();

    public void destroy(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Objective getObjective() {
        return objective;
    }
}
