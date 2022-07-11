package de.deroq.bedwars.stats;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.stats.models.StatsUser;
import de.deroq.database.services.cassandra.CassandraDatabaseServiceMethods;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class StatsManager {

    private final CassandraDatabaseServiceMethods databaseServiceMethods;

    public StatsManager(BedWars bedWars) {
        this.databaseServiceMethods = bedWars.getCassandraDatabaseService().getDatabaseServiceMethods();
    }

    /**
     * Creates a StatsUser.
     *
     * @param uuid The uuid of the user.
     * @param name The name of the user.
     */
    public void createStatsUser(UUID uuid, String name) {
        StatsUser statsUser = StatsUser.create(
                uuid.toString(),
                name);

        databaseServiceMethods.onInsert(statsUser, StatsUser.class);
    }

    /**
     * Updates a StatsUser.
     *
     * @param statsUser The StatsUser to update.
     */
    public void updateStatsUser(StatsUser statsUser) {
        databaseServiceMethods.onUpdate(
                statsUser,
                StatsUser.class);
    }

    /**
     * Gets a StatsUser.
     *
     * @param name The name of the user.
     * @return a Future with a StatsUser.
     */
    public CompletableFuture<StatsUser> getStatsUser(String name) {
        return databaseServiceMethods.getAsync(
                name,
                StatsUser.class);
    }
}
