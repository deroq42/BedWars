package de.deroq.bedwars.config.models;

import de.deroq.bedwars.config.Config;
import de.deroq.bedwars.game.shop.category.GameShopCategoryType;
import de.deroq.bedwars.game.shop.item.GameShopItemPriceType;
import de.deroq.bedwars.game.shop.item.GameShopItem;
import org.bukkit.Material;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ShopItemsConfig extends Config {

    private final List<GameShopItem> shopItems;

    private ShopItemsConfig(File file) {
        super(file);
        this.shopItems = Arrays.asList(new GameShopItem.builder()
                .setName("Sandstone")
                .setMaterial(Material.SANDSTONE)
                .setAmount(2)
                .setLore(Arrays.asList("§7Die idealen Blöcke um schnell in die Mitte zu kommen"))
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
