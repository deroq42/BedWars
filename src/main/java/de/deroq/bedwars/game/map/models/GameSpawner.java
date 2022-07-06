package de.deroq.bedwars.game.map.models;

import de.deroq.bedwars.models.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum GameSpawner {

    BRONZE("§c", "Bronze", new ItemBuilder(Material.CLAY_BRICK).setDisplayName("§cBronze").build(), 0, 15),
    IRON("§7", "Eisen", new ItemBuilder(Material.IRON_INGOT).setDisplayName("§7Eisen").build(), 10*20, 10*20),
    GOLD("§6", "Gold", new ItemBuilder(Material.GOLD_INGOT).setDisplayName("§6Gold").build(), 30*20, 30*20);

    private final String colorCode;
    private final String name;
    private final ItemStack itemStack;
    private final int delay;
    private final int period;

    GameSpawner(String colorCode, String name, ItemStack itemStack, int delay, int period) {
        this.colorCode = colorCode;
        this.name = name;
        this.itemStack = itemStack;
        this.delay = delay;
        this.period = period;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getDelay() {
        return delay;
    }

    public int getPeriod() {
        return period;
    }
}
