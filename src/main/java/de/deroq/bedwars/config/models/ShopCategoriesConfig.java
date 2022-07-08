package de.deroq.bedwars.config.models;

import de.deroq.bedwars.config.Config;
import de.deroq.bedwars.game.shop.category.GameShopCategory;
import de.deroq.bedwars.game.shop.category.GameShopCategoryType;
import org.bukkit.Material;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ShopCategoriesConfig extends Config {

    private final List<GameShopCategory> shopCategories;

    private ShopCategoriesConfig(File file) {
        super(file);
        this.shopCategories = Arrays.asList(GameShopCategory.create(
                "Blöcke",
                Material.SANDSTONE,
                "§7Hier findest du Blöcke aller Art",
                GameShopCategoryType.BLOCKS
        ));
    }

    public List<GameShopCategory> getShopCategories() {
        return shopCategories;
    }

    public static ShopCategoriesConfig create(File file) {
        return new ShopCategoriesConfig(file);
    }
}
