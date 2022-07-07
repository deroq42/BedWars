package de.deroq.bedwars.timers.lobby;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.timers.TimerTask;
import de.deroq.bedwars.utils.BukkitUtils;
import de.deroq.bedwars.utils.GameState;
import org.bukkit.Bukkit;

public class LobbyIdleTimer extends TimerTask {

    //WHERE THE TIMER STOPS TO COUNT UP
    private final int TOTAL_SECONDS = 600;

    public LobbyIdleTimer(BedWars bedWars) {
        super(bedWars, false, 20*60*3, 20*60*3);
        setCurrentSeconds(0);
        setTotalSeconds(TOTAL_SECONDS);
    }

    @Override
    public void onTick() {
        if(bedWars.getGameManager().getGameState() != GameState.LOBBY) {
            onStop();
            return;
        }

        if(Bukkit.getOnlinePlayers().size() < bedWars.getGameManager().MIN_PLAYERS) {
            BukkitUtils.sendBroadcastMessage("Es werden §e" + bedWars.getGameManager().MIN_PLAYERS + " Spieler §7benötigt, um die Runde zu starten");
        }
    }

    @Override
    public void onFinish() {
        if(Bukkit.getOnlinePlayers().size() < bedWars.getGameManager().MIN_PLAYERS) {
            Bukkit.shutdown();
        }
    }
}
