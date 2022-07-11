package de.deroq.bedwars.listeners;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.game.team.models.GameTeam;
import de.deroq.bedwars.game.team.models.GameTeamType;
import de.deroq.bedwars.utils.BukkitUtils;
import de.deroq.bedwars.utils.Constants;
import de.deroq.bedwars.utils.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

/**
 * @author deroq
 * @since 09.07.2022
 */

public class AsyncPlayerChatListener implements Listener {

    private final BedWars bedWars;

    public AsyncPlayerChatListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        final String CHAT_FORMAT = player.getDisplayName() + "§7: §f" + message;

        if (bedWars.getGameManager().getGameState() != GameState.INGAME) {
            event.setFormat(CHAT_FORMAT);
            return;
        }

        event.setCancelled(true);

        Optional<GamePlayer> optionalGamePlayer = bedWars.getGameManager().getGamePlayer(player.getUniqueId());
        if (!optionalGamePlayer.isPresent()) {
            return;
        }

        GamePlayer gamePlayer = optionalGamePlayer.get();
        if (gamePlayer.isSpectator()) {
            bedWars.getGameManager().getSpectators().forEach(gamePlayers -> gamePlayers.getPlayer().sendMessage(CHAT_FORMAT));
            return;
        }

        String[] args = message.split(" ");
        if (Constants.GLOBAL_CHAT_PREFIXES.contains(args[0])) {
            BukkitUtils.sendBroadcastMessage("§8[§7@all§8] " + CHAT_FORMAT.replace(args[0], ""), false);
            return;
        }

        GameTeam gameTeam = gamePlayer.getGameTeam();
        gameTeam.getPlayers().
                stream()
                .map(Bukkit::getPlayer)
                .forEach(players -> players.sendMessage(CHAT_FORMAT));

    }
}
