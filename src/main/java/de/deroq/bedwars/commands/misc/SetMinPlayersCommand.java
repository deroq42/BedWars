package de.deroq.bedwars.commands.misc;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.utils.Constants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class SetMinPlayersCommand extends Command {

    private final BedWars bedWars;

    public SetMinPlayersCommand(String name, BedWars bedWars) {
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

        bedWars.getFileManager().getSettingsConfig().setMinPlayers(i);
        try {
            bedWars.getFileManager().saveConfig(bedWars.getFileManager().getSettingsConfig());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        player.sendMessage(Constants.PREFIX + "§aDie Mindestanzahl an Spielern wurde auf " + i + " gesetzt");
        return false;
    }
}
