package de.deroq.bedwars.timers.restart;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.timers.TimerTask;
import de.deroq.bedwars.utils.BukkitUtils;
import de.deroq.bedwars.utils.GameState;
import org.bukkit.Bukkit;

import java.util.Arrays;

public class RestartTimer extends TimerTask {

    //WHERE THE TIMER BEGINS
    private final int TOTAL_SECONDS = 21;

    public RestartTimer(BedWars bedWars) {
        super(bedWars, true, 0, 20);
        setTotalSeconds(TOTAL_SECONDS);
        setCurrentSeconds(TOTAL_SECONDS);
    }

    @Override
    public void onTick() {
        if(bedWars.getGameManager().getGameState() != GameState.RESTART) {
            onStop();
            return;
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.setLevel(currentSeconds);
            player.setExp((float) currentSeconds / TOTAL_SECONDS);
        });


        if(Arrays.asList(20, 10, 5, 4, 3, 2, 1).contains(currentSeconds)) {
            BukkitUtils.sendBroadcastMessage("Die Runde startet in §e" + currentSeconds + " " + (currentSeconds != 1 ? "Sekunden" : "Sekunde") + " §7neu", true);
        }
    }

    @Override
    public void onFinish() {
        Bukkit.shutdown();
    }
}
