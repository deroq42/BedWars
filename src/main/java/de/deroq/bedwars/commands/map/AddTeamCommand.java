package de.deroq.bedwars.commands.map;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.team.models.GameTeamType;
import de.deroq.bedwars.utils.Constants;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddTeamCommand extends Command {

    private final BedWars bedWars;

    public AddTeamCommand(String name, BedWars bedWars) {
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

        if (args.length != 2) {
            player.sendMessage(Constants.PREFIX + "§e/addTeam <map> <team>");
            return true;
        }

        String map = args[0];
        bedWars.getGameMapManager().getMap(map).thenAcceptAsync(gameMap -> {
            if (gameMap == null) {
                player.sendMessage(Constants.PREFIX + "Diese Map gibt es nicht");
                return;
            }

            String team = args[1].toUpperCase();
            if(!EnumUtils.isValidEnum(GameTeamType.class, team)) {
                player.sendMessage(Constants.PREFIX + "Gib ein valides Team an: " + GameTeamType.values());
                return;
            }

            gameMap.getTeams().add(GameTeamType.valueOf(team));
            bedWars.getGameMapManager().updateMap(gameMap).thenAcceptAsync(b -> player.sendMessage(Constants.PREFIX + "§aTeam wurde hinzugefügt"));
        });

        return false;
    }
}
