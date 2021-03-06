package de.deroq.bedwars.commands.game;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.utils.Constants;
import de.deroq.bedwars.utils.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author deroq
 * @since 06.07.2022
 */

public class ForceMapCommand implements CommandExecutor {

    private final BedWars bedWars;

    public ForceMapCommand(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;
        if (!player.hasPermission("bedwars.forcemap")) {
            player.sendMessage(Constants.PREFIX + "§cDazu hast du keine Rechte");
            return true;
        }

        if(args.length != 1) {
            player.sendMessage(Constants.PREFIX + "§e/forcemap <map>");
            return true;
        }

        if (bedWars.getGameManager().getGameState() != GameState.LOBBY) {
            player.sendMessage(Constants.PREFIX + "Die Runde hat bereits begonnen");
            return true;
        }

        String map = args[0];
        if(!bedWars.getGameMapManager().getMapCache().containsKey(map)) {
            player.sendMessage(Constants.PREFIX + "§eVerfügbare Maps: " + bedWars.getGameMapManager().getMapCache().keySet());
            return true;
        }

        if(bedWars.getGameManager().isForceMapped()) {
            player.sendMessage(Constants.PREFIX + "Es wurde bereits eine Map ausgewählt");
            return true;
        }

        bedWars.getGameManager().setForceMapped(true);
        bedWars.getGameManager().setCurrentGameMap(bedWars.getGameMapManager().getMapCache().get(map));
        bedWars.getGameManager().updateScoreboard();
        player.sendMessage(Constants.PREFIX + "§aMap wurde geändert");
        return false;
    }
}
