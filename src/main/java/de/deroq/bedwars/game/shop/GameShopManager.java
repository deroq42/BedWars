package de.deroq.bedwars.game.shop;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.models.ItemBuilder;
import de.deroq.bedwars.game.shop.category.GameShopCategoryType;
import de.deroq.bedwars.game.shop.category.GameShopCategory;
import de.deroq.bedwars.game.shop.item.GameShopItemPriceType;
import de.deroq.bedwars.game.shop.item.GameShopItem;
import de.deroq.bedwars.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class GameShopManager {

    private final BedWars bedWars;
    private final Inventory categoryMenu;
    private final Map<GameShopCategoryType, Inventory> inventoryMap;

    public GameShopManager(BedWars bedWars) {
        this.bedWars = bedWars;
        this.categoryMenu = loadCategoryMenu();
        this.inventoryMap = new HashMap<>();
        loadCategoryInventories();
    }

    public Inventory getCategoryMenu() {
        return categoryMenu;
    }

    private Inventory loadCategoryMenu() {
        Inventory inventory = Bukkit.createInventory(null, 27, "§8Shop");
        int slot = 9;

        for (GameShopCategory shopCategory : bedWars.getFileManager().getShopCategoriesConfig().getShopCategories()) {
            inventory.setItem(slot, new ItemBuilder(shopCategory.getDisplayedItem())
                    .setDisplayName(shopCategory.getName())
                    .addLoreLine(shopCategory.getDescription())
                    .build());
            slot++;
        }

        BukkitUtils.fillInventory(inventory, 0, 8);
        BukkitUtils.fillInventory(inventory, 18, 26);
        return inventory;
    }

    private void loadCategoryInventories() {
        bedWars.getFileManager().getShopCategoriesConfig().getShopCategories().forEach(shopCategory -> {
            GameShopCategoryType categoryType = shopCategory.getCategoryType();
            Inventory inventory = Bukkit.createInventory(null, 27, categoryType.getInventoryName());

            int slot = 9;
            for (GameShopItem shopItem : bedWars.getFileManager().getShopItemsConfig().getShopItems()) {
                if (shopItem.getCategoryType() != categoryType) {
                    continue;
                }

                GameShopItemPriceType priceType = shopItem.getPriceType();
                if (shopItem.getEnchantments() == null) {
                    inventory.setItem(slot, new ItemBuilder(shopItem.getMaterial())
                            .setAmount(shopItem.getAmount())
                            .addLoreLine("§fPreis: " + priceType.getColorCode() + shopItem.getPrice() + " " + priceType.getName())
                            .addLoreAll(shopItem.getLore())
                            .build());
                } else {
                    inventory.setItem(slot, new ItemBuilder(shopItem.getMaterial())
                            .setAmount(shopItem.getAmount())
                            .addLoreLine("§fPreis: " + priceType.getColorCode() + shopItem.getPrice() + " " + priceType.getName())
                            .addLoreAll(shopItem.getLore())
                            .addEnchantmentAll(shopItem.getEnchantments())
                            .build());
                }
            }

            BukkitUtils.fillInventory(inventory, 0, 9);
            BukkitUtils.fillInventory(inventory, 18, 27);
            inventoryMap.put(categoryType, inventory);
        });
    }

    public Map<GameShopCategoryType, Inventory> getInventoryMap() {
        return inventoryMap;
    }
}
