package de.deroq.bedwars;

import de.deroq.bedwars.commands.game.ForceMapCommand;
import de.deroq.bedwars.commands.map.*;
import de.deroq.bedwars.commands.misc.*;
import de.deroq.bedwars.commands.game.StartCommand;
import de.deroq.bedwars.config.FileManager;
import de.deroq.bedwars.game.GameManager;
import de.deroq.bedwars.game.map.GameMapManager;
import de.deroq.bedwars.game.team.GameTeamManager;
import de.deroq.bedwars.listeners.InventoryClickListener;
import de.deroq.bedwars.listeners.PlayerInteractListener;
import de.deroq.bedwars.listeners.PlayerJoinListener;
import de.deroq.bedwars.listeners.PlayerQuitListener;
import de.deroq.bedwars.listeners.bedwars.BedWarsDropOutListener;
import de.deroq.database.models.DatabaseServiceBuilder;
import de.deroq.database.models.DatabaseServiceType;
import de.deroq.database.services.mongo.MongoDatabaseService;

import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BedWars extends JavaPlugin {

    private MongoDatabaseService mongoDatabaseService;
    private FileManager fileManager;
    private GameManager gameManager;
    private GameMapManager gameMapManager;
    private GameTeamManager gameTeamManager;

    @Override
    public void onEnable() {
        initDatabase();
        initManagers();
        registerListeners();
        registerCommands();

        getLogger().info("BedWars has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("BedWars has been disabled.");
    }

    private void initDatabase() {
        this.mongoDatabaseService = (MongoDatabaseService) new DatabaseServiceBuilder(DatabaseServiceType.MONGO)
                .setHost("localhost")
                .setDatabase("bedwars")
                .setUsername("root")
                .setPassword("123456")
                .setPort(27017)
                .build();

        mongoDatabaseService.connect();
    }

    private void initManagers() {
        this.fileManager = new FileManager();
        fileManager.loadFiles();

        this.gameManager = new GameManager(this);
        this.gameMapManager = new GameMapManager(this);
        this.gameTeamManager = new GameTeamManager(this);
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerInteractListener(this), this);
        pluginManager.registerEvents(new InventoryClickListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new BedWarsDropOutListener(this), this);
    }

    private void registerCommands() {
        SimpleCommandMap commandMap = ((CraftServer) Bukkit.getServer()).getCommandMap();
        /* GAME */
        commandMap.register("start", new StartCommand("start", this));
        commandMap.register("forcemap", new ForceMapCommand("forcemap", this));

        /* MAP */
        commandMap.register("createMap", new CreateMapCommand("createMap", this));
        commandMap.register("deleteMap", new DeleteMapCommand("deleteMap", this));
        commandMap.register("setSpectator", new SetSpectatorCommand("setSpectator", this));
        commandMap.register("addBuilder", new AddBuilderCommand("addBuilder", this));
        commandMap.register("addTeam", new AddTeamCommand("addTeam", this));
        commandMap.register("setSpawn", new SetSpawnCommand("setSpawn", this));
        commandMap.register("setBed", new SetBedCommand("setBed", this));
        commandMap.register("addSpawner", new AddSpawnerCommand("addSpawner", this));
        commandMap.register("addShop", new AddShopCommand("addShop", this));
        commandMap.register("setMaxBuildHeight", new SetMaxBuildHeightCommand("setMaxBuildHeight", this));

        /* MISC */
        commandMap.register("setLobby", new SetLobbyCommand("setLobby", this));
        commandMap.register("setMinPlayers", new SetMinPlayersCommand("setMinPlayers", this));
        commandMap.register("setMaxPlayers", new SetMaxPlayersCommand("setMaxPlayers", this));
        commandMap.register("setTeamSize", new SetTeamSizeCommand("setTeamSize", this));
        commandMap.register("setTeamCount", new SetTeamCountCommand("setTeamCount", this));
    }

    public MongoDatabaseService getMongoDatabaseService() {
        return mongoDatabaseService;
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
}
