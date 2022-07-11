package de.deroq.bedwars.listeners;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.game.team.models.GameTeam;
import de.deroq.bedwars.game.team.models.GameTeamType;
import de.deroq.bedwars.stats.models.StatsUser;
import de.deroq.bedwars.utils.BukkitUtils;
import de.deroq.bedwars.utils.Constants;
import de.deroq.bedwars.utils.GameState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;

/**
 * @author deroq
 * @since 07.07.2022
 */

public class BlockBreakListener implements Listener {

    private final BedWars bedWars;

    public BlockBreakListener(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (bedWars.getGameManager().getGameState() != GameState.INGAME) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
            }
            return;
        }

        if (!bedWars.getGameManager().BREAKABLE_BLOCKS.contains(block.getType())) {
            if(player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
            }

            if (block.getType() == Material.BED_BLOCK) {
                GameTeam gameTeam = bedWars.getGameManager().onBedBreak(block);

                if (gameTeam == null) {
                    return;
                }

                Optional<GamePlayer> optionalGamePlayer = bedWars.getGameManager().getGamePlayer(player.getUniqueId());
                if (!optionalGamePlayer.isPresent()) {
                    return;
                }

                GamePlayer gamePlayer = optionalGamePlayer.get();
                if (gamePlayer.isSpectator()) {
                    return;
                }

                if (gamePlayer.getGameTeam().equals(gameTeam)) {
                    player.sendMessage(Constants.PREFIX + "Du kannst dein eigenes Bett nicht zerstören!");
                    return;
                }

                GameTeamType bedGameTeamType = gameTeam.getGameTeamType();
                GameTeamType playerGameTeamType = gamePlayer.getGameTeam().getGameTeamType();
                BukkitUtils.sendBroadcastMessage("Das Bett von Team " + bedGameTeamType.getColorCode() + bedGameTeamType.getName() + " §7wurde von " + playerGameTeamType.getColorCode() + player.getName() + " §7zerstört", true);
                BukkitUtils.sendBroadcastSound(Sound.WITHER_DEATH);

                gameTeam.setBedGone(true);
                block.setType(Material.AIR);
                bedWars.getGameManager().updateScoreboard();

                StatsUser statsUser = gamePlayer.getStatsUser();
                statsUser.addBed();
                statsUser.addPoints(10);
            }
        } else {
            if(!bedWars.getGameManager().getCurrentGameMap().getPlacedBlocks().contains(block.getLocation())) {
                if(player.getGameMode() != GameMode.CREATIVE) {
                    event.setCancelled(true);
                    return;
                }
            }

            bedWars.getGameManager().getCurrentGameMap().getPlacedBlocks().remove(block.getLocation());
        }

    }
}
