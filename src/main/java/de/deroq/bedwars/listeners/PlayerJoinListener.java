package de.deroq.bedwars.listeners;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.utils.BukkitUtils;
import de.deroq.bedwars.utils.GameState;
import de.deroq.bedwars.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final BedWars bedWars;

    public PlayerJoinListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GamePlayer.create(player.getUniqueId());
        event.setJoinMessage(null);

        PlayerUtils.loadPlayer(player);
        PlayerUtils.loadInventory(player, bedWars.getGameManager().getGameState());

        if (bedWars.getGameManager().getGameState() == GameState.LOBBY) {
            BukkitUtils.sendBroadcastMessage("§e" + player.getName() + " §7hat die Runde betreten " + BukkitUtils.getOnlinePlayers(bedWars.getGameManager().MAX_PLAYERS), true);
            bedWars.getGameManager().teleportToLobby(player);
            bedWars.getGameManager().initLobbyTimer();
            bedWars.getGameManager().setLobbyScoreboard(gamePlayer);
        } else {
            bedWars.getGameManager().setSpectator(gamePlayer, true);
            bedWars.getGameManager().setIngameScoreboard(gamePlayer);
        }

        bedWars.getGameManager().getGamePlayers().add(gamePlayer);
        bedWars.getGameManager().updateTablist();
    }
}
