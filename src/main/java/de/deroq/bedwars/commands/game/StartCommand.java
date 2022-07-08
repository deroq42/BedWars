package de.deroq.bedwars.commands.game;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.timers.TimerTask;
import de.deroq.bedwars.timers.lobby.LobbyTimer;
import de.deroq.bedwars.utils.Constants;
import de.deroq.bedwars.utils.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand extends Command {

    private final BedWars bedWars;

    public StartCommand(String name, BedWars bedWars) {
        super(name);
        this.bedWars = bedWars;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;
        if(!player.hasPermission("bedwars.start")) {
            player.sendMessage(Constants.PREFIX + "§cDazu hast du keine Rechte");
            return true;
        }

        if(bedWars.getGameManager().getGameState() != GameState.LOBBY) {
            player.sendMessage(Constants.PREFIX + "Die Runde hat bereits begonnen");
            return true;
        }

        TimerTask currentTimer = bedWars.getGameManager().getCurrentTimer();
        if(currentTimer instanceof LobbyTimer) {
            if(currentTimer.getCurrentSeconds() <= 10) {
                player.sendMessage(Constants.PREFIX + "Die Runde startet bereits");
                return true;
            }
        } else {
            currentTimer = bedWars.getGameManager().createLobbyTimer();
        }

        bedWars.getGameManager().setForceStarted(true);
        currentTimer.setCurrentSeconds(11);
        player.sendMessage(Constants.PREFIX + "§aDie Runde wird gestartet");
        return false;
    }
}
