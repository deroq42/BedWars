package de.deroq.bedwars.commands.map;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.team.models.GameTeamType;
import de.deroq.bedwars.utils.BukkitUtils;
import de.deroq.bedwars.utils.Constants;
import org.apache.commons.lang3.EnumUtils;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Set;

/**
 * @author deroq
 * @since 06.07.2022
 */

public class SetBedCommand implements CommandExecutor {

    private final BedWars bedWars;

    public SetBedCommand(BedWars bedWars) {
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
            player.sendMessage(Constants.PREFIX + "§e/setBed <map> <team>");
            return true;
        }

        String map = args[0];
        bedWars.getGameMapManager().getMap(map).thenAcceptAsync(gameMap -> {
            if (gameMap == null) {
                player.sendMessage(Constants.PREFIX + "Diese Map gibt es nicht");
                return;
            }

            String team = args[1].toUpperCase();
            if (!EnumUtils.isValidEnum(GameTeamType.class, team)) {
                player.sendMessage(Constants.PREFIX + "Gib ein valides Team an: " + Arrays.toString(GameTeamType.values()));
                return;
            }

            gameMap.getBedLocations().put(team, BukkitUtils.locationToString(player.getTargetBlock((Set<Material>) null, 5).getLocation()));
            bedWars.getGameMapManager().updateMap(gameMap).thenAcceptAsync(b -> player.sendMessage(Constants.PREFIX + "§aBett wurde gesetzt"));
        });

        return false;
    }
}
