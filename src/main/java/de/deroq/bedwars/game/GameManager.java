package de.deroq.bedwars.game;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.map.models.GameMap;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.game.team.models.GameTeam;
import de.deroq.bedwars.game.team.models.GameTeamType;
import de.deroq.bedwars.npc.NPC;
import de.deroq.bedwars.npc.misc.NPCBuilder;
import de.deroq.bedwars.timers.TimerTask;
import de.deroq.bedwars.timers.lobby.LobbyIdleTimer;
import de.deroq.bedwars.timers.lobby.LobbyTimer;
import de.deroq.bedwars.timers.restart.RestartTimer;
import de.deroq.bedwars.utils.BukkitUtils;
import de.deroq.bedwars.utils.GameState;

import de.deroq.bedwars.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class GameManager {

    private final BedWars bedWars;
    private GameState gameState;
    private TimerTask currentTimer;
    private GameMap currentGameMap;
    private Collection<GamePlayer> gamePlayers;
    private boolean forceStarted;
    private final int MIN_PLAYERS;
    private final Location LOBBY_LOCATION;

    /**
     * Constructor of the class.
     */
    public GameManager(BedWars bedWars) {
        this.bedWars = bedWars;
        this.gameState = GameState.LOBBY;
        this.gamePlayers = new ArrayList<>();
        this.forceStarted = false;
        this.MIN_PLAYERS = bedWars.getFileManager().getSettingsConfig().getMinPlayers();
        this.LOBBY_LOCATION = BukkitUtils.locationFromString(bedWars.getFileManager().getSettingsConfig().getWaitingLobbyLocation());

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

    private void initRestartTimer() {
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

    /**
     * Starts spawning items.
     */
    public void startSpawningItems() {
        currentGameMap.getItemSpawners().keySet().forEach(gameSpawner -> Bukkit.getScheduler().runTaskTimer(bedWars, () -> currentGameMap.getItemSpawners().get(gameSpawner).stream().map(BukkitUtils::locationFromString).forEach(location -> {
            Item item = location.getWorld().dropItemNaturally(location, gameSpawner.getItemStack());
            item.setVelocity(new Vector(0, 0, 0));
        }), gameSpawner.getDelay(), gameSpawner.getPeriod()));
    }

    /**
     * Spawns all shops.
     */
    public void spawnShops() {
        currentGameMap.getShopLocations()
                .stream()
                .map(BukkitUtils::locationFromString)
                .forEach(location -> {
                    NPC npc = new NPCBuilder()
                            .setName("§6§lShop")
                            .setLocation(location)
                            .build();

                    npc.spawn();
                });
    }

    /**
     * Teleports a player to his spawn.
     *
     * @param player The player who got teleported.
     */
    public void teleportToSpawn(Player player) {
        Optional<GamePlayer> optionalGamePlayer = getGamePlayer(player.getUniqueId());
        if(!optionalGamePlayer.isPresent()) {
            return;
        }

        GamePlayer gamePlayer = optionalGamePlayer.get();
        if(gamePlayer.getGameTeam() == null) {
            return;
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

        for(GameTeam gameTeam : currentGameMap.getGameTeams()) {
            Location bedLocation = gameTeam.getBedLocation();

            if(block.equals(bedLocation.getBlock())) {
                return gameTeam;
            }

            if(bedLocation.getBlock().getLocation().distanceSquared(location) == 1) {
                return gameTeam;
            }
        }

        return null;
    }

    /**
     * Checks for drop out.
     *
     * @param gamePlayer The GamePlayer who could be dropped out.
     * @return true if the player is dropped out.
     */
    public boolean checkForDropOut(GamePlayer gamePlayer) {
        if(gamePlayer.getGameTeam().isBedGone()) {
            return true;
        }

        return false;
    }

    /**
     * Checks for a win.
     *
     * @return null if there is no winner yet.
     */
    public GameTeam checkForWin() {
        if(currentGameMap.getGameTeams().size() > 1) {
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

        Bukkit.getOnlinePlayers().forEach(player -> {
            initRestartTimer();
            teleportToLobby(player);
            setSpectator(player, false);

            BukkitUtils.sendBroadcastMessage("Team" + gameTeamType.getColorCode() + gameTeamType.getName() + " §7hat die Runde gewonnen!");
            BukkitUtils.spawnFirework(LOBBY_LOCATION);
            PlayerUtils.loadPlayer(player);
        });
    }

    /**
     * Sets a player into the spectator mode.
     *
     * @param player The player who is to spectate.
     */
    public void setSpectator(Player player, boolean spectator) {
        Optional<GamePlayer> optionalGamePlayer = getGamePlayer(player.getUniqueId());
        if(!optionalGamePlayer.isPresent()) {
            return;
        }

        GamePlayer gamePlayer = optionalGamePlayer.get();
        gamePlayer.setSpectator(spectator, getAlive());

        if(spectator) {
            player.teleport(BukkitUtils.locationFromString(currentGameMap.getSpectatorLocation()));
        }
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

    public boolean isForceStarted() {
        return forceStarted;
    }

    public void setForceStarted(boolean forceStarted) {
        this.forceStarted = forceStarted;
    }
}
