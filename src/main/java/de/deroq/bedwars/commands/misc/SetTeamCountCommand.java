package de.deroq.bedwars.commands.misc;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.utils.Constants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author deroq
 * @since 06.07.2022
 */

public class SetTeamCountCommand extends Command {

    private final BedWars bedWars;

    public SetTeamCountCommand(String name, BedWars bedWars) {
        super(name);
        this.bedWars = bedWars;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;
        if(!player.hasPermission("bedwars.setup")) {
            player.sendMessage(Constants.PREFIX + "§cDazu hast du keine Rechte");
            return true;
        }

        int i = 0;
        try {
            i = Integer.parseInt(args[0]);
        } catch(NumberFormatException exception) {
            player.sendMessage(Constants.PREFIX + "§cGib eine valide Zahl an!");
            return true;
        }

        if(!Arrays.asList(2, 3, 4, 8).contains(i)) {
            player.sendMessage(Constants.PREFIX + "Gib eine valide Teamanzahl an: 2, 3, 4, 8");
            return true;
        }

        bedWars.getFileManager().getSettingsConfig().setTeamCount(i);
        try {
            bedWars.getFileManager().saveConfig(bedWars.getFileManager().getSettingsConfig());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        player.sendMessage(Constants.PREFIX + "§aDie Teamanzahl wurde auf " + i + " gesetzt");
        return false;
    }
}
