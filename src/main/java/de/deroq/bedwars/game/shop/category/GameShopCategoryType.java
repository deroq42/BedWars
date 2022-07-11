package de.deroq.bedwars.game.shop.category;

/**
 * @author deroq
 * @since 09.07.2022
 */

public enum GameShopCategoryType {

    BLOCKS("§8Blöcke"),
    ARMOR("§8Rüstung"),
    PICKAXES("§8Spitzhacken"),
    WEAPONS("§8Waffen"),
    FOOD("§8Essen"),
    CHESTS("§8Kisten"),
    SPECIAL("§8Spezielles");

    private final String inventoryName;

    GameShopCategoryType(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public String getInventoryName() {
        return inventoryName;
    }
}
