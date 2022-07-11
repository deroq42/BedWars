package de.deroq.bedwars.commands.misc;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.utils.BukkitUtils;
import de.deroq.bedwars.utils.Constants;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

/**
 * @author deroq
 * @since 06.07.2022
 */

public class SetLobbyCommand extends Command {

    private final BedWars bedWars;

    public SetLobbyCommand(String name, BedWars bedWars) {
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

        bedWars.getFileManager().getSettingsConfig().setWaitingLobbyLocation(BukkitUtils.locationToString(player.getLocation()));
        try {
            bedWars.getFileManager().saveConfig(bedWars.getFileManager().getSettingsConfig());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        player.sendMessage(Constants.PREFIX + "§aLobby wurde gesetzt");
        return false;
    }
}
