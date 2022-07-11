package de.deroq.bedwars.game.map;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.map.models.GameMap;
import de.deroq.bedwars.game.team.models.GameTeam;
import de.deroq.bedwars.game.team.models.GameTeamType;
import de.deroq.bedwars.npc.NPC;
import de.deroq.bedwars.utils.BukkitUtils;
import de.deroq.bedwars.utils.Constants;
import de.deroq.database.services.mongo.MongoDatabaseServiceMethods;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author deroq
 * @since 06.07.2022
 */

public class GameMapManager {

    private final BedWars bedWars;
    private final MongoDatabaseServiceMethods databaseServiceMethods;
    private final MongoCollection<GameMap> collection;
    private final Map<String, GameMap> mapCache;

    public GameMapManager(BedWars bedWars) {
        this.bedWars = bedWars;
        this.databaseServiceMethods = bedWars.getMongoDatabaseService().getDatabaseServiceMethods();
        this.collection = bedWars.getMongoDatabaseService().getCollection("bedWarsMaps-" + Constants.SERVER_GROUP, GameMap.class);
        this.mapCache = new HashMap<>();

        cacheMaps().thenAcceptAsync(b -> {
            setupMaps();
            GameMap gameMap = pickRandomMap();
            bedWars.getGameManager().setCurrentGameMap(gameMap);
        });
    }

    /**
     * Creates a GameMap.
     *
     * @param muid The id of the map to insert.
     * @return a Future with a Boolean which returns false if the GameMap has been inserted.
     */
    public CompletableFuture<Boolean> createMap(String muid) {
        return databaseServiceMethods.onInsert(
                collection,
                Filters.eq("muid", muid),
                new GameMap(muid, Constants.SERVER_GROUP));
    }

    /**
     * Deletes a GameMap.
     *
     * @param muid The id of the map to delete.
     * @return a Future with a Boolean which returns false if the GameMap has been deleted.
     */
    public CompletableFuture<Boolean> deleteMap(String muid) {
        return databaseServiceMethods.onDelete(
                collection,
                Filters.eq("muid", muid));
    }

    /**
     * Updates a GameMap.
     *
     * @param gameMap The GameMap to update
     * @return a Future with a Boolean which returns false if the GameMap has been updated.
     */
    public CompletableFuture<Boolean> updateMap(GameMap gameMap) {
        return databaseServiceMethods.onUpdate(
                collection,
                Filters.eq("muid", gameMap.getMuid()),
                gameMap);
    }

    /**
     * Gets a GameMap by its id.
     *
     * @param muid The id of the map to get.
     * @return a Future with a GameMap.
     */
    public CompletableFuture<GameMap> getMap(String muid) {
        return databaseServiceMethods.getAsync(
                collection,
                Filters.eq("muid", muid));
    }

    /**
     * Caches all GameMaps.
     *
     * @return a Future with a Boolean which returns false if all maps has been cached.
     */
    public CompletableFuture<Boolean> cacheMaps() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        databaseServiceMethods.getAsyncCollection(collection).thenAcceptAsync(gameMaps -> {
            gameMaps.forEach(gameMap -> mapCache.put(gameMap.getMuid(), gameMap));
            future.complete(false);
        });

        return future;
    }

    /**
     * @return a random GameMap from the cache.
     */
    private GameMap pickRandomMap() {
        int random = new Random().nextInt(mapCache.size());
        return new ArrayList<>(mapCache.values()).get(random);
    }

    /**
     * Creates GameTeams, Shops, etc. for all loaded maps.
     */
    private void setupMaps() {
        mapCache.values().forEach(gameMap -> {
            gameMap.setGameTeams(new ArrayList<>());
            gameMap.setPlacedBlocks(new ArrayList<>());
            gameMap.setShops(new ArrayList<>());

            /* Create new GameTeams and add them to a list. */
            gameMap.getTeams().forEach(gameTeamType -> {
                GameTeam gameTeam = GameTeam.create(
                        GameTeamType.valueOf(gameTeamType),
                        BukkitUtils.locationFromString(gameMap.getSpawnLocations().get(gameTeamType)),
                        BukkitUtils.locationFromString(gameMap.getBedLocations().get(gameTeamType)));

                gameMap.getGameTeams().add(gameTeam);
            });

            /* Creates new npcs so the entity ids get loaded. */
            gameMap.getShopLocations()
                    .stream()
                    .map(BukkitUtils::locationFromString)
                    .forEach(location -> {
                        NPC npc = new NPC.builder()
                                .setUuid(UUID.randomUUID())
                                .setName("§6§lShop")
                                .setValue(Constants.SHOP_VALUE)
                                .setSignature(Constants.SHOP_SIGNATURE)
                                .setLocation(location)
                                .build();

                        gameMap.getShops().add(npc);
                    });
        });
    }

    public Map<String, GameMap> getMapCache() {
        return mapCache;
    }
}
