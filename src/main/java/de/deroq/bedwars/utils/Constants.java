package de.deroq.bedwars.utils;

import de.deroq.bedwars.game.models.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author deroq
 * @since 06.07.2022
 */

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

    public static final ItemStack PLACEHOLDER = new ItemBuilder(Material.STAINED_GLASS_PANE)
            .setData((short) 7)
            .setDisplayName(" ")
            .build();

    public static final Set<String> GLOBAL_CHAT_PREFIXES = Stream.of(
            "@a",
            "@all",
            "@global",
            "@A",
            "@ALL",
            "@GLOBAL"
    ).collect(Collectors.toSet());

    public static final String SHOP_VALUE = "ewogICJ0aW1lc3RhbXAiIDogMTY1NzIxMjYzMjIzNSwKICAicHJvZmlsZUlkIiA6ICJiNDBiOTZhZmQyMDQ0MTRlYmJkNTQ5ZTExMDIzOGJkMSIsCiAgInByb2ZpbGVOYW1lIiA6ICJicmVoYWZ0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzYwYTc4Njg2ODIwNTk5MWM2NTkxYjc2NTBjM2RmYjhiYmMwZWYzMjFkNmM0ODQwMTU1YTA1Mjg2YjUzZGMzMmEiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yMzQwYzBlMDNkZDI0YTExYjE1YThiMzNjMmE3ZTllMzJhYmIyMDUxYjI0ODFkMGJhN2RlZmQ2MzVjYTdhOTMzIgogICAgfQogIH0KfQ==";
    public static final String SHOP_SIGNATURE = "yji+v5fWNi7PnjPmi+pd6rAkfkPfUBq7vWW6KcbFZ4syJ53zt2YKN8wQIOkqSPcQEdSA+gtRZYsCKTLn0tU0udyurisgUyKB4fxrocPlGSwLNvzKMwsLKBbX9vdMpqPjLHADfdkU0hPCDNVJxUtewAhN8HCECB9Z/Za4zAztNQik+DugIskmGJJ2ZoXv6bKXP4XGyPpOZn9ZBf0BXDquN/FAmzPDUa8JWhWqaSJqx1j94mxSeN7jr+GPeLnQrHx98yX2yYhFyOIehqvYwIVHJVX0wjm8sAEJWKyR5KlvqLip5mchPfyKcrIZaeGcC1lCUzy7GDcaMHr9FLrgkumCVN+uvTu1gPCN4xzl+jpTL3efB8otbsyHO0TNSr1MWcihlcagYfAIqMRqgFY+vGBWsasJI01F5JA8ypAQJuDVgUmqiZcL+Bb+h/lXWj2E/mLS4fHKIPzWhVrekjYkb+ISzg9ujGGni1Qt6H6H5o8YkX+dQN09/k6f5JibdIcBzQ+IN7+hkZnrRQawoYhazPF/bP3weWWAd9RUyr7i4CVvDte5i8t1c7sM7djNhbHbTAKWv1wxdYNUYgSTSedYI/ODQJWLHbweA9qLGSqvHpgal3L8Sw5PjUoZPHPkerY4RPZkjEmiSZJ8e0CniCoFr0oQjHGpcsnl85Ir51PMcj6H8gs=";
}
