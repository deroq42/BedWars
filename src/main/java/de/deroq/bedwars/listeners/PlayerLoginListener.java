package de.deroq.bedwars.listeners;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.utils.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author deroq
 * @since 07.07.2022
 */

public class PlayerLoginListener implements Listener {

    private final BedWars bedWars;

    public PlayerLoginListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        if (bedWars.getGameManager().getGameState() == GameState.LOBBY) {
            if (Bukkit.getOnlinePlayers().size() >= bedWars.getGameManager().MAX_PLAYERS) {
                if (!player.hasPermission("bedwars.fulljoin")) {
                    event.disallow(PlayerLoginEvent.Result.KICK_FULL, "§cDie Runde ist voll. Du kannst dir unter shop.gommehd.net den Premium-Rang kaufen.");
                    return;
                }

                List<Player> nonPremiumPlayers = getNonPremiumPlayers();
                if(nonPremiumPlayers.size() == 0) {
                    event.disallow(PlayerLoginEvent.Result.KICK_FULL, "§cDie Runde ist voll mit Premium-Spielern, daher kannst du nicht beitreten.");
                    return;
                }

                if(bedWars.getGameManager().getCurrentTimer().getCurrentSeconds() <= 5) {
                    event.disallow(PlayerLoginEvent.Result.KICK_FULL, "§cDie Runde startet bereits.");
                    return;
                }

                Player kickedPlayer = nonPremiumPlayers.get(new Random().nextInt(nonPremiumPlayers.size()));
                kickedPlayer.sendMessage("§cDu wurdest aus der Runde gekickt, da ein Premium-Spieler beigetreten ist. Du kannst dir unter shop.gommehd.net den Premium-Rang kaufen.");
                kickedPlayer.kickPlayer("");
                event.allow();
            }
            return;
        }

        if (bedWars.getGameManager().getGameState() == GameState.INGAME) {
            if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
                event.disallow(PlayerLoginEvent.Result.KICK_FULL, "§cDer Server ist voll");
            }
            return;
        }

        if (bedWars.getGameManager().getGameState() == GameState.RESTART) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cDie Runde ist bereits vorbei");
        }
    }

    private List<Player> getNonPremiumPlayers() {
        return Bukkit.getOnlinePlayers()
                .stream()
                .filter(players -> !players.hasPermission("bedwars.fulljoin"))
                .collect(Collectors.toList());
    }
}
