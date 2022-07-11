package de.deroq.bedwars.game;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.map.models.GameMap;
import de.deroq.bedwars.game.map.models.GameSpawner;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.game.scoreboard.ingame.IngameScoreboard;
import de.deroq.bedwars.game.scoreboard.lobby.LobbyScoreboard;
import de.deroq.bedwars.game.team.models.GameTeam;
import de.deroq.bedwars.game.team.models.GameTeamType;
import de.deroq.bedwars.npc.NPC;
import de.deroq.bedwars.stats.models.StatsUser;
import de.deroq.bedwars.timers.TimerTask;
import de.deroq.bedwars.timers.lobby.LobbyIdleTimer;
import de.deroq.bedwars.timers.lobby.LobbyTimer;
import de.deroq.bedwars.timers.restart.RestartTimer;
import de.deroq.bedwars.utils.BukkitUtils;
import de.deroq.bedwars.utils.GameState;

import de.deroq.bedwars.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author deroq
 * @since 06.07.2022
 */

public class GameManager {

    private final BedWars bedWars;
    private GameState gameState;
    private TimerTask currentTimer;
    private GameMap currentGameMap;
    private Collection<GamePlayer> gamePlayers;
    private boolean forceStarted;
    private boolean forceMapped;

    public final Location LOBBY_LOCATION;
    public final int MIN_PLAYERS;
    public final int MAX_PLAYERS;
    public final int TEAM_COUNT;
    public final int TEAM_SIZE;
    public final Set<Material> BREAKABLE_BLOCKS;
    public final Set<Material> PLACEABLE_BLOCKS;

    /**
     * Constructor of the class.
     */
    public GameManager(BedWars bedWars) {
        this.bedWars = bedWars;
        this.gameState = GameState.LOBBY;
        this.gamePlayers = new ArrayList<>();
        this.forceStarted = false;
        this.forceMapped = false;

        this.LOBBY_LOCATION = BukkitUtils.locationFromString(bedWars.getFileManager().getSettingsConfig().getWaitingLobbyLocation());
        this.MIN_PLAYERS = bedWars.getFileManager().getSettingsConfig().getMinPlayers();
        this.MAX_PLAYERS = bedWars.getFileManager().getSettingsConfig().getMaxPlayers();
        this.TEAM_COUNT = bedWars.getFileManager().getSettingsConfig().getTeamCount();
        this.TEAM_SIZE = bedWars.getFileManager().getSettingsConfig().getTeamSize();
        this.BREAKABLE_BLOCKS = bedWars.getFileManager().getBlocksConfig().getBlocks();
        this.PLACEABLE_BLOCKS = BREAKABLE_BLOCKS;

        initLobbyIdleTimer();
    }

    /**
     * Starts the lobby timer when enough players are online.
     */
    public void initLobbyTimer() {
        if (forceStarted) {
            return;
        }

        if (Bukkit.getOnlinePlayers().size() == MIN_PLAYERS) {
            setCurrentTimer(createLobbyTimer());
        }
    }

    /**
     * Starts the lobby idle timer when too few players are online.
     */
    private void initLobbyIdleTimer() {
        LobbyIdleTimer lobbyIdleTimer = new LobbyIdleTimer(bedWars);
        lobbyIdleTimer.onStart();
    }

    /**
     * Starts the restart timer.
     */
    private void initRestartTimer() {
        currentTimer.onStop();

        RestartTimer restartTimer = new RestartTimer(bedWars);
        restartTimer.onStart();
        this.currentTimer = restartTimer;
    }

    /**
     * @return a new LobbyTimer.
     */
    public TimerTask createLobbyTimer() {
        LobbyTimer lobbyTimer = new LobbyTimer(bedWars);
        lobbyTimer.onStart();
        return lobbyTimer;
    }

    /**
     * Teleports a player to the lobby location.
     *
     * @param player The player to teleport.
     */
    public void teleportToLobby(Player player) {
        if (player.isDead()) {
            player.spigot().respawn();
        }

        if (LOBBY_LOCATION != null) {
            player.teleport(LOBBY_LOCATION);
        }
    }

    /**
     * Teleports all players to the spawn locations on the current map.
     */
    public void teleportToSpawns() {
        getGamePlayers().forEach(gamePlayer -> gamePlayer.getPlayer().teleport(gamePlayer.getGameTeam().getSpawnLocation()));
    }

    public void startSpawningItems() {
        currentGameMap.getItemSpawners()
                .keySet()
                .stream()
                .map(GameSpawner::valueOf)
                .forEach(gameSpawner -> Bukkit.getScheduler().runTaskTimer(bedWars, () -> currentGameMap.getItemSpawners().get(gameSpawner.toString())
                        .stream()
                        .map(BukkitUtils::locationFromString)
                        .forEach(location -> {
                            Item item = location.getWorld().dropItemNaturally(location, gameSpawner.getItemStack());
                            item.setVelocity(new Vector(0, 0, 0));
                        }), gameSpawner.getDelay(), gameSpawner.getPeriod()));
    }

    public void spawnShops() {
        currentGameMap.getShops().forEach(NPC::spawn);
    }

    /**
     * Teleports a player to his spawn.
     *
     * @param gamePlayer The GamePlayer who gets teleported.
     */
    public void teleportToSpawn(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        if (player.isDead()) {
            player.spigot().respawn();
        }

        player.teleport(gamePlayer.getGameTeam().getSpawnLocation());
    }

    /**
     * Triggers on bed break.
     *
     * @param block The block which got destroyed.
     * @return the GameTeam of the bed which has been destroyed.
     */
    public GameTeam onBedBreak(Block block) {
        Location location = block.getLocation();

        for (GameTeam gameTeam : currentGameMap.getGameTeams()) {
            Location bedLocation = gameTeam.getBedLocation();

            if (block.equals(bedLocation.getBlock())) {
                return gameTeam;
            }

            if (bedLocation.getBlock().getLocation().distanceSquared(location) == 1) {
                return gameTeam;
            }
        }

        return null;
    }

    /**
     * Triggers on player ingame quit.
     *
     * @param gamePlayer The GamePlayer who quit.
     */
    public void onCombatLog(GamePlayer gamePlayer) {
        if (gamePlayer.getLastDamager() == null) {
            return;
        }

        //Add kill to player
    }

    /**
     * Checks for drop out.
     *
     * @param gamePlayer The GamePlayer who could be dropped out.
     * @return true if the player is dropped out.
     */
    public boolean checkForDropOut(GamePlayer gamePlayer) {
        return gamePlayer.getGameTeam().isBedGone();
    }

    /**
     * Checks for a win.
     *
     * @return null if there is no winner yet.
     */
    public GameTeam checkForWin() {
        if (currentGameMap.getGameTeams().size() > 1) {
            return null;
        }

        return currentGameMap.getGameTeams().get(0);
    }

    /**
     * Triggers on game win.
     *
     * @param gameTeam The winning team.
     */
    public void onWin(GameTeam gameTeam) {
        GameTeamType gameTeamType = gameTeam.getGameTeamType();

        getGamePlayers().forEach(gamePlayer -> {
            Player player = gamePlayer.getPlayer();
            teleportToLobby(player);
            setSpectator(gamePlayer, false);
            PlayerUtils.loadPlayer(player);

            if(gamePlayer.getGameTeam().getGameTeamType() != gameTeamType) {
                StatsUser statsUser = gamePlayer.getStatsUser();
                statsUser.addWin();
                statsUser.addPoints(20);
            }
        });

        BukkitUtils.sendBroadcastMessage("Team " + gameTeamType.getColorCode() + gameTeamType.getName() + " §7hat die Runde gewonnen!", true);
        BukkitUtils.spawnFirework(LOBBY_LOCATION);
        this.gameState = GameState.RESTART;
        initRestartTimer();
    }

    /**
     * Sets a player into the spectator mode.
     *
     * @param gamePlayer The GamePlayer who is to spectate.
     */
    public void setSpectator(GamePlayer gamePlayer, boolean spectator) {
        gamePlayer.setSpectator(spectator, getAlive());

        if (spectator) {
            gamePlayer.getPlayer().teleport(BukkitUtils.locationFromString(currentGameMap.getSpectatorLocation()));
        }
    }

    public void setLobbyScoreboard(GamePlayer gamePlayer) {
        LobbyScoreboard lobbyScoreboard = new LobbyScoreboard(bedWars);
        lobbyScoreboard.setScoreboard(gamePlayer.getPlayer());
        lobbyScoreboard.setTablist();
        gamePlayer.setGameScoreboard(lobbyScoreboard);
    }

    public void setIngameScoreboard(GamePlayer gamePlayer) {
        IngameScoreboard ingameScoreboard = new IngameScoreboard(bedWars);
        ingameScoreboard.setScoreboard(gamePlayer.getPlayer());
        ingameScoreboard.setTablist();
        gamePlayer.setGameScoreboard(ingameScoreboard);
    }

    public void updateScoreboard() {
        getGamePlayers().forEach(gamePlayer -> gamePlayer.getGameScoreboard().updateScoreboard());
    }

    public void updateTablist() {
        getGamePlayers().forEach(gamePlayer -> gamePlayer.getGameScoreboard().updateTablist());
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public TimerTask getCurrentTimer() {
        return currentTimer;
    }

    public void setCurrentTimer(TimerTask currentTimer) {
        this.currentTimer = currentTimer;
    }

    public GameMap getCurrentGameMap() {
        return currentGameMap;
    }

    public void setCurrentGameMap(GameMap currentGameMap) {
        this.currentGameMap = currentGameMap;
    }

    public Collection<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Collection<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Optional<GamePlayer> getGamePlayer(UUID uuid) {
        return gamePlayers
                .stream()
                .filter(gamePlayer -> gamePlayer.getUuid().equals(uuid))
                .findFirst();
    }

    public List<GamePlayer> getAlive() {
        return gamePlayers
                .stream()
                .filter(gamePlayer -> !gamePlayer.isSpectator())
                .collect(Collectors.toList());
    }

    public List<GamePlayer> getSpectators() {
        return gamePlayers
                .stream()
                .filter(GamePlayer::isSpectator)
                .collect(Collectors.toList());
    }

    public boolean isForceStarted() {
        return forceStarted;
    }

    public void setForceStarted(boolean forceStarted) {
        this.forceStarted = forceStarted;
    }

    public boolean isForceMapped() {
        return forceMapped;
    }

    public void setForceMapped(boolean forceMapped) {
        this.forceMapped = forceMapped;
    }
}
