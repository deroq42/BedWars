package de.deroq.bedwars.listeners;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.events.BedWarsDropOutEvent;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.game.team.models.GameTeamType;
import de.deroq.bedwars.utils.BukkitUtils;
import de.deroq.bedwars.utils.GameState;
import de.deroq.bedwars.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class PlayerQuitListener implements Listener {

    private final BedWars bedWars;

    public PlayerQuitListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);

        Optional<GamePlayer> optionalGamePlayer = bedWars.getGameManager().getGamePlayer(player.getUniqueId());
        if(!optionalGamePlayer.isPresent()) {
            return;
        }

        GamePlayer gamePlayer = optionalGamePlayer.get();
        bedWars.getGameManager().getGamePlayers().remove(gamePlayer);

        if(bedWars.getGameManager().getGameState() == GameState.LOBBY) {
            BukkitUtils.sendBroadcastMessage("§e" + player.getName() + " §7hat die Runde verlassen " + BukkitUtils.getOnlinePlayers(bedWars.getGameManager().MAX_PLAYERS));
            bedWars.getGameTeamManager().onQuit(gamePlayer);
            return;
        }

        if(bedWars.getGameManager().getGameState() == GameState.INGAME) {
            if(!gamePlayer.isSpectator()) {
                GameTeamType gameTeamType = gamePlayer.getGameTeam().getGameTeamType();
                BukkitUtils.sendBroadcastMessage(gameTeamType.getColorCode() + player.getName() + " §7hat die Runde verlassen");
                bedWars.getGameManager().onCombatLog(gamePlayer);
                Bukkit.getPluginManager().callEvent(new BedWarsDropOutEvent(gamePlayer));
            }
        }
    }
}
