package de.deroq.bedwars.commands.map;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.utils.BukkitUtils;
import de.deroq.bedwars.utils.Constants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AddShopCommand extends Command {

    private final BedWars bedWars;

    public AddShopCommand(String name, BedWars bedWars) {
        super(name);
        this.bedWars = bedWars;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;
        if (!player.hasPermission("bedwars.setup")) {
            player.sendMessage(Constants.PREFIX + "§cDazu hast du keine Rechte");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Constants.PREFIX + "§e/addShop <map>");
            return true;
        }

        String map = args[0];
        bedWars.getGameMapManager().getMap(map).thenAcceptAsync(gameMap -> {
            if (gameMap == null) {
                player.sendMessage(Constants.PREFIX + "Diese Map gibt es nicht");
                return;
            }


            gameMap.getShopLocations().add(BukkitUtils.locationToString(player.getLocation()));
            bedWars.getGameMapManager().updateMap(gameMap).thenAcceptAsync(b -> player.sendMessage(Constants.PREFIX + "§aShop wurde hinzugefügt"));
        });

        return false;
    }
}
