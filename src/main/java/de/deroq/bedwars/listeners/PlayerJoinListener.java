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
        event.setJoinMessage(null);

        PlayerUtils.loadPlayer(player);
        PlayerUtils.loadInventory(player, bedWars.getGameManager().getGameState());

        GamePlayer gamePlayer = GamePlayer.create(player.getUniqueId());
        if(bedWars.getGameManager().getGameState() == GameState.LOBBY) {
            BukkitUtils.sendBroadcastMessage("§e" + player.getName() + " §7hat die Runde betreten " + BukkitUtils.getOnlinePlayers(bedWars.getGameManager().MAX_PLAYERS));
            bedWars.getGameManager().teleportToLobby(player);
            bedWars.getGameManager().initLobbyTimer();
            return;
        }

        if(bedWars.getGameManager().getGameState() != GameState.LOBBY) {
            bedWars.getGameManager().setSpectator(player, true);
        }

        bedWars.getGameManager().getGamePlayers().add(gamePlayer);
    }
}
