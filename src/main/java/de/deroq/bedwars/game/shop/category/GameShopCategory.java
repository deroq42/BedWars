package de.deroq.bedwars.game.shop.category;

import org.bukkit.Material;

public class GameShopCategory {

    private final String name;
    private final Material displayedItem;
    private final String description;
    private final GameShopCategoryType categoryType;

    private GameShopCategory(String name, Material displayedItem, String description, GameShopCategoryType categoryType) {
        this.name = name;
        this.displayedItem = displayedItem;
        this.description = description;
        this.categoryType = categoryType;
    }

    public String getName() {
        return name;
    }

    public Material getDisplayedItem() {
        return displayedItem;
    }

    public String getDescription() {
        return description;
    }

    public GameShopCategoryType getCategoryType() {
        return categoryType;
    }

    public static GameShopCategory create(String name, Material displayedItem, String description, GameShopCategoryType categoryType) {
        return new GameShopCategory(name, displayedItem, description, categoryType);
    }
}
