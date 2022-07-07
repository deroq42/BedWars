package de.deroq.bedwars.commands.map;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.utils.Constants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AddBuilderCommand extends Command {

    private final BedWars bedWars;

    public AddBuilderCommand(String name, BedWars bedWars) {
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

        if(args.length != 2) {
            player.sendMessage(Constants.PREFIX + "§e/addBuilder <map> <builder>");
            return true;
        }

        String map = args[0];
        bedWars.getGameMapManager().getMap(map).thenAcceptAsync(gameMap -> {
            if(gameMap == null) {
                player.sendMessage(Constants.PREFIX + "Diese Map gibt es nicht");
                return;
            }

            gameMap.getBuilders().add(args[1]);
            bedWars.getGameMapManager().updateMap(gameMap).thenAcceptAsync(b -> player.sendMessage(Constants.PREFIX + "§aBuilder wurde hinzugefügt"));
        });
        return false;
    }
}