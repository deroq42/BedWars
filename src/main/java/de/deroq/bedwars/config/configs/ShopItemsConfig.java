package de.deroq.bedwars.config.configs;

import de.deroq.bedwars.config.Config;
import de.deroq.bedwars.game.shop.category.GameShopCategoryType;
import de.deroq.bedwars.game.shop.item.GameShopItemPriceType;
import de.deroq.bedwars.game.shop.item.GameShopItem;
import org.bukkit.Material;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author deroq
 * @since 09.07.2022
 */

public class ShopItemsConfig extends Config {

    private final List<GameShopItem> shopItems;

    private ShopItemsConfig(File file) {
        super(file);
        this.shopItems = Collections.singletonList(new GameShopItem.builder()
                .setName("Sandstein")
                .setMaterial(Material.SANDSTONE)
                .setAmount(2)
                .setLore(Collections.singletonList("§7Die idealen Blöcke um schnell in die Mitte zu kommen"))
                .setEnchantments(null)
                .setCategoryType(GameShopCategoryType.BLOCKS)
                .setPriceType(GameShopItemPriceType.BRONZE)
                .setPrice(1)
                .build());
    }

    public List<GameShopItem> getShopItems() {
        return shopItems;
    }

    public static ShopItemsConfig create(File file) {
        return new ShopItemsConfig(file);
    }
}
