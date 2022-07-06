package de.deroq.bedwars.commands.map;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.utils.Constants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateMapCommand extends Command {

    private final BedWars bedWars;

    public CreateMapCommand(String name, BedWars bedWars) {
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
