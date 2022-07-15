package de.deroq.bedwars;

import de.deroq.bedwars.commands.game.ForceMapCommand;
import de.deroq.bedwars.commands.game.StatsCommand;
import de.deroq.bedwars.commands.map.*;
import de.deroq.bedwars.commands.misc.*;
import de.deroq.bedwars.commands.game.StartCommand;
import de.deroq.bedwars.config.FileManager;
import de.deroq.bedwars.game.GameManager;
import de.deroq.bedwars.game.map.GameMapManager;
import de.deroq.bedwars.game.shop.GameShopManager;
import de.deroq.bedwars.game.team.GameTeamManager;
import de.deroq.bedwars.listeners.*;
import de.deroq.bedwars.listeners.bedwars.BedWarsDropOutListener;
import de.deroq.bedwars.listeners.bedwars.BedWarsShopInteractListener;
import de.deroq.bedwars.listeners.misc.*;
import de.deroq.bedwars.stats.StatsManager;
import de.deroq.bedwars.stats.models.StatsUser;
import de.deroq.bedwars.utils.Constants;
import de.deroq.database.models.DatabaseService;
import de.deroq.database.models.DatabaseServiceType;
import de.deroq.database.services.cassandra.CassandraDatabaseService;
import de.deroq.database.services.mongo.MongoDatabaseService;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

/**
 * @author deroq
 * @since 06.07.2022
 */

public class BedWars extends JavaPlugin {

    private MongoDatabaseService mongoDatabaseService;
    private CassandraDatabaseService cassandraDatabaseService;
    private FileManager fileManager;
    private GameManager gameManager;
    private GameMapManager gameMapManager;
    private GameTeamManager gameTeamManager;
    private GameShopManager gameShopManager;
    private StatsManager statsManager;

    @Override
    public void onEnable() {
        initMongoDatabase();
        initCassandraDatabase();
        initManagers();
        registerListeners();
        registerCommands();

        getLogger().info("BedWars has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("BedWars has been disabled.");
    }

    private void initMongoDatabase() {
        this.mongoDatabaseService = (MongoDatabaseService) new DatabaseService.builder(DatabaseServiceType.MONGO)
                .setHost("localhost")
                .setDatabase("bedwars")
                .setUsername("root")
                .setPassword("123456")
                .setPort(27017)
                .build();

        mongoDatabaseService.connect();
    }

    private void initCassandraDatabase() {
        this.cassandraDatabaseService = (CassandraDatabaseService) new DatabaseService.builder(DatabaseServiceType.CASSANDRA)
                .setHost("localhost")
                .setDatabase("bedwars")
                .setUsername("root")
                .setPassword("123456")
                .setPort(7000)
                .setKeySpace("bedwars")
                .setMappers(Collections.singletonList(StatsUser.class))
                .build();

        cassandraDatabaseService.connect();
        cassandraDatabaseService.getDatabaseServiceMethods().createTable(Constants.CREATE_STATS_TABLE);
    }

    private void initManagers() {
        this.fileManager = new FileManager();
        fileManager.loadFiles();

        this.gameManager = new GameManager(this);
        this.gameMapManager = new GameMapManager(this);
        this.gameTeamManager = new GameTeamManager(this);
        this.gameShopManager = new GameShopManager(this);
        this.statsManager = new StatsManager(this);
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new PlayerLoginListener(this), this);
        pluginManager.registerEvents(new PlayerInteractListener(this), this);
        pluginManager.registerEvents(new InventoryClickListener(this), this);
        pluginManager.registerEvents(new BlockPlaceListener(this), this);
        pluginManager.registerEvents(new BlockBreakListener(this), this);
        pluginManager.registerEvents(new EntityDamageListener(this), this);
        pluginManager.registerEvents(new EntityDamageByEntityListener(this), this);
        pluginManager.registerEvents(new PlayerDeathListener(this), this);
        pluginManager.registerEvents(new AsyncPlayerChatListener(this), this);

        /* MISC */
        pluginManager.registerEvents(new FoodLevelChangeListener(this), this);
        pluginManager.registerEvents(new CreatureSpawnListener(), this);
        pluginManager.registerEvents(new CraftItemListener(), this);
        pluginManager.registerEvents(new PlayerDropItemListener(this), this);
        pluginManager.registerEvents(new PlayerBedEnterListener(), this);
        pluginManager.registerEvents(new PlayerPickUpItemListener(this), this);
        pluginManager.registerEvents(new EntityExplodeListener(this), this);
        pluginManager.registerEvents(new WeatherChangeListener(), this);
        pluginManager.registerEvents(new PlayerAchievementAwardedListener(), this);

        /* BEDWARS */
        pluginManager.registerEvents(new BedWarsDropOutListener(this), this);
        pluginManager.registerEvents(new BedWarsShopInteractListener(this), this);
    }

    private void registerCommands() {
        /* GAME */
        getCommand("start").setExecutor(new StartCommand(this));
        getCommand("forcemap").setExecutor(new ForceMapCommand(this));
        getCommand("stats").setExecutor(new StatsCommand(this));

        /* MAP */
        getCommand("createMap").setExecutor(new CreateMapCommand(this));
        getCommand("deleteMap").setExecutor(new DeleteMapCommand(this));
        getCommand("setSpectator").setExecutor(new SetSpectatorCommand(this));
        getCommand("addBuilder").setExecutor(new AddBuilderCommand(this));
        getCommand("addTeam").setExecutor(new AddTeamCommand(this));
        getCommand("setSpawn").setExecutor(new SetSpawnCommand(this));
        getCommand("setBed").setExecutor(new SetBedCommand(this));
        getCommand("addSpawner").setExecutor(new AddSpawnerCommand(this));
        getCommand("addShop").setExecutor(new AddShopCommand(this));
        getCommand("setMaxBuildHeight").setExecutor(new SetMaxBuildHeightCommand(this));

        /* MISC */
        getCommand("setLobby").setExecutor(new SetLobbyCommand(this));
        getCommand("setMinPlayers").setExecutor(new SetMinPlayersCommand(this));
        getCommand("setMaxPlayers").setExecutor(new SetMaxPlayersCommand(this));
        getCommand("setTeamSize").setExecutor(new SetTeamSizeCommand(this));
        getCommand("setTeamCount").setExecutor(new SetTeamCountCommand(this));
    }

    public MongoDatabaseService getMongoDatabaseService() {
        return mongoDatabaseService;
    }

    public CassandraDatabaseService getCassandraDatabaseService() {
        return cassandraDatabaseService;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public GameMapManager getGameMapManager() {
        return gameMapManager;
    }

    public GameTeamManager getGameTeamManager() {
        return gameTeamManager;
    }

    public GameShopManager getGameShopManager() {
        return gameShopManager;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }
}
