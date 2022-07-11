package de.deroq.bedwars.config.configs;

import de.deroq.bedwars.config.Config;
import de.deroq.bedwars.game.shop.category.GameShopCategory;
import de.deroq.bedwars.game.shop.category.GameShopCategoryType;
import org.bukkit.Material;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author deroq
 * @since 09.07.2022
 */

public class ShopCategoriesConfig extends Config {

    private final List<GameShopCategory> shopCategories;

    private ShopCategoriesConfig(File file) {
        super(file);
        this.shopCategories = Collections.singletonList(GameShopCategory.create(
                "Blöcke",
                Material.SANDSTONE,
                Collections.singletonList("§7Hier findest du Blöcke aller Art"),
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
