package de.deroq.bedwars.game.shop.item;

import de.deroq.bedwars.game.models.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum GameShopItemPriceType {

    BRONZE("§c", "Bronze", new ItemBuilder(Material.CLAY_BRICK).setDisplayName("§cBronze").build()),
    IRON("§7", "Eisen", new ItemBuilder(Material.IRON_INGOT).setDisplayName("§7Eisen").build()),
    GOLD("§6", "Gold", new ItemBuilder(Material.GOLD_INGOT).setDisplayName("§6Gold").build());

    private final String colorCode;
    private final String name;
    private final ItemStack itemStack;

    GameShopItemPriceType(String colorCode, String name, ItemStack itemStack) {
        this.colorCode = colorCode;
        this.name = name;
        this.itemStack = itemStack;
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
}
