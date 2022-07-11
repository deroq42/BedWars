package de.deroq.bedwars.commands.game;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.utils.Constants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author deroq
 * @since 11.07.2022
 */

public class StatsCommand extends Command {

    private final BedWars bedWars;

    public StatsCommand(String name, BedWars bedWars) {
        super(name);
        this.bedWars = bedWars;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;

        if (args.length < 1) {
            bedWars.getStatsManager().getStatsUser(player.getName()).thenAcceptAsync(statsUser -> player.sendMessage(statsUser.toString()));
            return false;
        }

        String name = args[0];
        bedWars.getStatsManager().getStatsUser(name).thenAcceptAsync(statsUser -> {
            if (statsUser == null) {
                player.sendMessage(Constants.PREFIX + "Spieler konnte nicht gefunden werden");
                return;
            }

            player.sendMessage(statsUser.toString());
        });

        return false;
    }
}