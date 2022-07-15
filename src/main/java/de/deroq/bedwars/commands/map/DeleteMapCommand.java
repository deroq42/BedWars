package de.deroq.bedwars.commands.map;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.utils.Constants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author deroq
 * @since 06.07.2022
 */

public class DeleteMapCommand implements CommandExecutor {

    private final BedWars bedWars;

    public DeleteMapCommand(BedWars bedWars) {
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

        if (args.length != 1) {
            player.sendMessage(Constants.PREFIX + "§e/deleteMap <map>");
            return true;
        }

        String map = args[0];
        bedWars.getGameMapManager().deleteMap(map).thenAcceptAsync(doesntExist -> {
            if(doesntExist) {
                player.sendMessage(Constants.PREFIX + "Diese Map gibt es nicht");
                return;
            }

            player.sendMessage(Constants.PREFIX + "§aMap wurde gelöscht");
        });

        return false;
    }
}
