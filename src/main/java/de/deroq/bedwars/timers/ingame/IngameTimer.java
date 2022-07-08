package de.deroq.bedwars.timers.ingame;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.timers.TimerTask;
import de.deroq.bedwars.timers.restart.RestartTimer;
import de.deroq.bedwars.utils.BukkitUtils;
import de.deroq.bedwars.utils.GameState;
import de.deroq.bedwars.utils.PlayerUtils;
import org.bukkit.Bukkit;

import java.util.Arrays;

public class IngameTimer extends TimerTask {

    //WHERE THE TIMER STOPS TO COUNT UP
    private final int TOTAL_SECONDS = 60*60;

    public IngameTimer(BedWars bedWars) {
        super(bedWars, false, 0, 20);
        setTotalSeconds(TOTAL_SECONDS);
        setCurrentSeconds(0);
    }

    @Override
    public void onTick() {
        if(Arrays.asList(30*60, 45*60, 55*60, 59*60).contains(currentSeconds)) {
            final int REMAINING_TIME = (TOTAL_SECONDS - currentSeconds) / 60;
            BukkitUtils.sendBroadcastMessage("Die Runde endet in §e" + REMAINING_TIME + " Minuten", true);
        }
    }

    @Override
    public void onFinish() {
        RestartTimer restartTimer = new RestartTimer(bedWars);
        restartTimer.onStart();

        bedWars.getGameManager().setGameState(GameState.RESTART);
        bedWars.getGameManager().setCurrentTimer(restartTimer);

        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerUtils.loadPlayer(player);
            bedWars.getGameManager().teleportToLobby(player);
        });
    }
}
