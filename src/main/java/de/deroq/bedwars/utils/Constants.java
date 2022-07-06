package de.deroq.bedwars.utils;

import de.deroq.bedwars.models.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Constants {

    public static final String PREFIX = "§7[§eBedWars§7] ";
    /* IF CLOUD SYSTEM USAGE, IT HAD BEEN THE SERVER GROUP */
    public static final String SERVER_GROUP = Bukkit.getServerName();

    public static final ItemStack TEAM_SELECTION_ITEM = new ItemBuilder(Material.WATCH)
            .setDisplayName("§eTeam wählen")
            .build();

    public static final ItemStack LOBBY_ITEM = new ItemBuilder(Material.SLIME_BALL)
            .setDisplayName("§aZur Lobby")
            .build();

    public static final ItemStack SPECTATOR_ITEM = new ItemBuilder(Material.COMPASS)
            .setDisplayName("§cSpieler zuschauen")
            .build();
}
