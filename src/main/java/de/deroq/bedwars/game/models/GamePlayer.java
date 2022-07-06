package de.deroq.bedwars.game.models;

import de.deroq.bedwars.game.team.models.GameTeam;
import de.deroq.bedwars.utils.GameState;
import de.deroq.bedwars.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.UUID;

public class GamePlayer {

    private final UUID uuid;
    private GameTeam gameTeam;
    private UUID lastDamager;
    private boolean spectator;

    public GamePlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public static GamePlayer create(UUID uuid) {
        return new GamePlayer(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public GameTeam getGameTeam() {
        return gameTeam;
    }

    public void setGameTeam(GameTeam gameTeam) {
        this.gameTeam = gameTeam;
    }

    public boolean isSpectator() {
        return spectator;
    }

    public void setSpectator(boolean spectator, Collection<GamePlayer> alive) {
        this.spectator = spectator;

        Player player = getPlayer();
        if(player == null) {
            return;
        }

        PlayerUtils.loadPlayer(player);

        if(spectator) {
            player.setGameMode(GameMode.ADVENTURE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 3), false);
            alive.forEach(gamePlayer -> gamePlayer.getPlayer().hidePlayer(player));
            PlayerUtils.loadInventory(player, GameState.INGAME);

            this.gameTeam = null;
            this.lastDamager = null;
        } else {
            player.setGameMode(GameMode.SURVIVAL);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            alive.forEach(gamePlayer -> gamePlayer.getPlayer().showPlayer(player));
        }

        player.setAllowFlight(spectator);
        player.setFlying(spectator);
        player.spigot().setCollidesWithEntities(!spectator);
    }

    public UUID getLastDamager() {
        return lastDamager;
    }

    public void setLastDamager(UUID lastDamager) {
        this.lastDamager = lastDamager;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}