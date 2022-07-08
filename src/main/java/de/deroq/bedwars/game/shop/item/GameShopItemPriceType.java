package de.deroq.bedwars.game.shop.item;

import org.bukkit.Material;

public enum GameShopItemPriceType {

    BRONZE("§c", "Bronze", Material.CLAY_BRICK),
    IRON("§7", "Eisen", Material.IRON_INGOT),
    GOLD("§6", "Gold", Material.GOLD_INGOT);

    private final String colorCode;
    private final String name;
    private final Material material;

    GameShopItemPriceType(String colorCode, String name, Material material) {
        this.colorCode = colorCode;
        this.name = name;
        this.material = material;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }
}
