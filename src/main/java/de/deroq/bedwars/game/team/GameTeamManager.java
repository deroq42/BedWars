package de.deroq.bedwars.game.team;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.map.models.GameMap;
import de.deroq.bedwars.game.models.GamePlayer;
import de.deroq.bedwars.game.team.models.GameTeam;
import de.deroq.bedwars.game.team.models.GameTeamType;
import de.deroq.bedwars.models.ItemBuilder;
import de.deroq.bedwars.utils.Constants;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class GameTeamManager {

    private final BedWars bedWars;
    private final int TEAM_SIZE;
    private final int TEAM_COUNT;

    public GameTeamManager(BedWars bedWars) {
        this.bedWars = bedWars;
        this.TEAM_SIZE = bedWars.getFileManager().getSettingsConfig().getTeamSize();
        this.TEAM_COUNT = bedWars.getFileManager().getSettingsConfig().getTeamCount();
    }

    /**
     * Triggers on player quit.
     *
     * @param gamePlayer The GamePlayer who quit.
     */
    public void onQuit(GamePlayer gamePlayer) {
        if (gamePlayer.getGameTeam() != null) {
            gamePlayer.getGameTeam().getPlayers().remove(gamePlayer.getUuid());
            gamePlayer.setGameTeam(null);
        }
    }

    /**
     * Allocates all players to teams.
     */
    public void allocateTeams() {
        List<GamePlayer> noTeamPlayers = bedWars.getGameManager().getGamePlayers()
                .stream()
                .filter(gamePlayer -> gamePlayer.getGameTeam() == null)
                .collect(Collectors.toList());

        if (noTeamPlayers.size() == 0) {
            return;
        }

        Collections.shuffle(noTeamPlayers);
        GameMap gameMap = bedWars.getGameManager().getCurrentGameMap();

        for (GameTeam gameTeam : gameMap.getGameTeams()) {
            Iterator<GamePlayer> iterator = noTeamPlayers.iterator();
            if (iterator.hasNext()) {
                if (gameTeam.getPlayers().size() >= TEAM_SIZE) {
                    continue;
                }

                GamePlayer gamePlayer = iterator.next();
                gamePlayer.setGameTeam(gameTeam);
                gameTeam.getPlayers().add(gamePlayer.getUuid());
                iterator.remove();
            }
        }
    }

    /**
     * Triggers on inventory click in team selection inventory.
     *
     * @param player    The player who clicks the inventory.
     * @param itemStack The item the player clicked on.
     */
    public void onTeamSelection(Player player, ItemStack itemStack) {
        GameTeam gameTeam = blockToGameTeam(itemStack);
        if (gameTeam == null) {
            return;
        }

        if (gameTeam.getPlayers().size() >= TEAM_SIZE) {
            player.sendMessage(Constants.PREFIX + "Dieses Team ist bereits voll");
            return;
        }

        Optional<GamePlayer> optionalGamePlayer = bedWars.getGameManager().getGamePlayers()
                .stream()
                .filter(gamePlayer -> gamePlayer.getUuid().equals(player.getUniqueId()))
                .findFirst();

        if (!optionalGamePlayer.isPresent()) {
            return;
        }

        GamePlayer gamePlayer = optionalGamePlayer.get();
        if (gamePlayer.getGameTeam() != null) {
            if (gamePlayer.getGameTeam().equals(gameTeam)) {
                player.sendMessage(Constants.PREFIX + "Du bist bereits in diesem Team");
                return;
            }

            gamePlayer.getGameTeam().getPlayers().remove(player.getUniqueId());
        }

        GameTeamType gameTeamType = gameTeam.getGameTeamType();
        gamePlayer.setGameTeam(gameTeam);
        gameTeam.getPlayers().add(player.getUniqueId());
        player.sendMessage(Constants.PREFIX + "Du bist nun in Team " + gameTeamType.getColorCode() + gameTeamType.getName());
        player.closeInventory();
    }

    public void openTeamSelectionInventory(Player player) {
        player.openInventory(generateTeamSelectionInventory());
    }

    /**
     * Generates a new inventory by the team count.
     *
     * @return a new Inventory.
     */
    private Inventory generateTeamSelectionInventory() {
        Inventory inventory = Bukkit.createInventory(null, 9, "§8Team auswählen");

        int i = 0;
        for (GameTeamType gameTeamType : GameTeamType.values()) {
            if (i < TEAM_COUNT) {
                int slot = 0;

                if (TEAM_COUNT == 8) {
                    slot = i;
                    if (i >= 4) {
                        slot += 1;
                    }
                }

                if (TEAM_COUNT == 4) {
                    slot = (i * 2) + 1;
                }

                if (TEAM_COUNT == 3) {
                    slot = (i * 2) + 2;
                }

                if (TEAM_COUNT == 2) {
                    slot = (i == 0 ? 2 : 6);
                }

                List<String> lore = new ArrayList<>();
                List<String> teamMembers = bedWars.getGameManager().getGamePlayers()
                        .stream()
                        .filter(gamePlayer -> gamePlayer.getGameTeam() != null)
                        .filter(gamePlayer -> gamePlayer.getGameTeam().getGameTeamType() == gameTeamType)
                        .map(gamePlayer -> gamePlayer.getPlayer().getName())
                        .collect(Collectors.toList());

                if (!teamMembers.isEmpty()) {
                    for (String name : teamMembers) {
                        lore.add("§7➥ " + gameTeamType.getColorCode() + name);
                    }
                }

                inventory.setItem(slot,
                        new ItemBuilder(Material.WOOL)
                                .setWoolColor(gameTeamType.getWoolColor())
                                .setDisplayName(gameTeamType.getColorCode() + gameTeamType.getName())
                                .addLoreAll(lore)
                                .build());
                i++;
            }
        }

        return inventory;
    }

    /**
     * Maps block to game team.
     */
    private GameTeam blockToGameTeam(ItemStack itemStack) {
        if (itemStack.getType() != Material.WOOL) {
            return null;
        }

        GameMap gameMap = bedWars.getGameManager().getCurrentGameMap();
        if (gameMap == null) {
            return null;
        }

        for (GameTeamType gameTeamType : GameTeamType.values()) {
            if (gameTeamType.getWoolColor().getData() != itemStack.getDurability()) {
                continue;
            }

            Optional<GameTeam> optionalGameTeam = gameMap
                    .getGameTeams()
                    .stream()
                    .filter(gameTeam -> gameTeam.getGameTeamType() == gameTeamType)
                    .findFirst();

            return optionalGameTeam.orElse(null);

        }

        return null;
    }
}