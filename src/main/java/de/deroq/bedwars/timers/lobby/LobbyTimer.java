package de.deroq.bedwars.timers.lobby;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.map.models.GameMap;
import de.deroq.bedwars.game.team.models.GameTeamType;
import de.deroq.bedwars.npc.models.PacketReader;
import de.deroq.bedwars.timers.TimerTask;
import de.deroq.bedwars.timers.ingame.IngameTimer;
import de.deroq.bedwars.utils.BukkitUtils;
import de.deroq.bedwars.utils.Constants;
import de.deroq.bedwars.utils.GameState;
import de.deroq.bedwars.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class LobbyTimer extends TimerTask {

    //WHERE THE TIMER BEGINS
    private final int TOTAL_SECONDS = 61;

    public LobbyTimer(BedWars bedWars) {
        super(bedWars, true, 0, 20);
        setTotalSeconds(TOTAL_SECONDS);
        setCurrentSeconds(TOTAL_SECONDS);
    }

    @Override
    public void onTick() {
        if(bedWars.getGameManager().getGameState() != GameState.LOBBY || (Bukkit.getOnlinePlayers().size() < bedWars.getGameManager().MIN_PLAYERS && !bedWars.getGameManager().isForceStarted())) {
            onStop();
            return;
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.setLevel(currentSeconds);
            player.setExp((float) currentSeconds / TOTAL_SECONDS);
        });


        if(Arrays.asList(60, 30, 10, 5, 4, 3, 2, 1).contains(currentSeconds)) {
            BukkitUtils.sendBroadcastMessage("Die Runde startet in §e" + currentSeconds + " " + (currentSeconds != 1 ? "Sekunden" : "Sekunde"), true);
            BukkitUtils.sendBroadcastSound(Sound.NOTE_BASS);

            if(currentSeconds == 10) {
                GameMap gameMap = bedWars.getGameManager().getCurrentGameMap();
                BukkitUtils.sendBroadcastMessage("Es wird auf der Map §e" + gameMap.getMuid() + " §7gespielt, gebaut von: §e" + gameMap.getBuilders(), true);
            }
        }
    }

    @Override
    public void onFinish() {
        IngameTimer ingameTimer = new IngameTimer(bedWars);
        ingameTimer.onStart();
        onStop();

        BukkitUtils.sendBroadcastMessage("§eDas Spiel hat begonnen", true);

        bedWars.getGameManager().setGameState(GameState.INGAME);
        bedWars.getGameTeamManager().allocateTeams();
        bedWars.getGameManager().teleportToSpawns();
        bedWars.getGameManager().startSpawningItems();
        bedWars.getGameManager().spawnShops();
        bedWars.getGameManager().setCurrentTimer(ingameTimer);
        bedWars.getGameManager().getAlive().forEach(gamePlayer -> {
            Player player = gamePlayer.getPlayer();
            GameTeamType gameTeamType = gamePlayer.getGameTeam().getGameTeamType();

            bedWars.getGameManager().setIngameScoreboard(gamePlayer);
            PlayerUtils.loadPlayer(player);
            player.sendMessage(Constants.PREFIX + "Dein Team: " + gameTeamType.getColorCode() + gameTeamType.getName());

            PacketReader packetReader = new PacketReader(bedWars, gamePlayer);
            packetReader.inject();
            gamePlayer.setPacketReader(packetReader);
        });
    }
}
