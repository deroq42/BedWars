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

public class CreateMapCommand implements CommandExecutor {

    private final BedWars bedWars;

    public CreateMapCommand(BedWars bedWars) {
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

        if(args.length != 1) {
            player.sendMessage(Constants.PREFIX + "§e/createMap <name>");
            return true;
        }

        String id = args[0];
        bedWars.getGameMapManager().createMap(id).thenAcceptAsync(exists -> {
            if (exists) {
                player.sendMessage(Constants.PREFIX + "Diese Map gibt es bereits");
                return;
            }

            player.sendMessage(Constants.PREFIX + "§aMap wurde erstellt");
        });

        return false;
    }
}
