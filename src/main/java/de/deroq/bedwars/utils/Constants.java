package de.deroq.bedwars.utils;

import de.deroq.bedwars.models.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

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

    public static final List<Material> PLACEABLE_BLOCKS = Arrays.asList(
            Material.SANDSTONE,
            Material.ENDER_STONE,
            Material.ENDER_CHEST,
            Material.CHEST,
            Material.IRON_BLOCK,
            Material.WEB,
            Material.GLASS,
            Material.GLOWSTONE,
            Material.LADDER,
            Material.TNT,
            Material.FIRE,
            Material.PACKED_ICE,
            Material.CAKE_BLOCK
    );

    public static final List<Material> BREAKABLE_BLOCKS = PLACEABLE_BLOCKS;
}
