package de.deroq.bedwars.commands.map;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.map.models.GameSpawner;
import de.deroq.bedwars.game.team.models.GameTeamType;
import de.deroq.bedwars.utils.BukkitUtils;
import de.deroq.bedwars.utils.Constants;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author deroq
 * @since 06.07.2022
 */

public class AddSpawnerCommand implements CommandExecutor {

    private final BedWars bedWars;

    public AddSpawnerCommand(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;
        if (!player.hasPermission("bedwars.setup")) {
            player.sendMessage(Constants.PREFIX + "§cDazu hast du keine Rechte");
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(Constants.PREFIX + "§e/addSpawner <map> <spawner>");
            return true;
        }

        String map = args[0];
        bedWars.getGameMapManager().getMap(map).thenAcceptAsync(gameMap -> {
            if (gameMap == null) {
                player.sendMessage(Constants.PREFIX + "Diese Map gibt es nicht");
                return;
            }

            String spawner = args[1].toUpperCase();
            if (!EnumUtils.isValidEnum(GameSpawner.class, spawner)) {
                player.sendMessage(Constants.PREFIX + "Gib ein validen Spawner an: " + Arrays.toString(GameSpawner.values()));
                return;
            }

            if(!gameMap.getItemSpawners().containsKey(spawner)) {
                gameMap.getItemSpawners().put(spawner, new ArrayList<>());
            }

            gameMap.getItemSpawners().get(spawner).add(BukkitUtils.locationToString(player.getLocation()));
            bedWars.getGameMapManager().updateMap(gameMap).thenAcceptAsync(b -> player.sendMessage(Constants.PREFIX + "§aSpawner wurde hinzugefügt"));
        });

        return false;
    }
}
