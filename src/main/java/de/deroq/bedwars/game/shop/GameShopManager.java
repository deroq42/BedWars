package de.deroq.bedwars.game.shop;

import de.deroq.bedwars.BedWars;
import de.deroq.bedwars.game.models.ItemBuilder;
import de.deroq.bedwars.game.shop.category.GameShopCategory;
import de.deroq.bedwars.game.shop.category.GameShopCategoryType;
import de.deroq.bedwars.game.shop.item.GameShopItem;
import de.deroq.bedwars.utils.BukkitUtils;
import de.deroq.bedwars.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author deroq
 * @since 09.07.2022
 */

public class GameShopManager {

    private final BedWars bedWars;
    private final List<ItemStack> categoryDisplayItems;
    private final Inventory mainInventory;
    private final Map<GameShopCategoryType, Inventory> categoryInventories;

    public GameShopManager(BedWars bedWars) {
        this.bedWars = bedWars;
        this.categoryDisplayItems = loadCategoryDisplayItems();
        this.mainInventory = loadMainInventory();
        this.categoryInventories = loadCategoryInventories();
    }

    /**
     * Triggers on click in the shop inventory.
     *
     * @param player The player who clicks the inventory.
     * @param itemStack The item which gets clicked.
     * @param clickType How the item gets clicked.
     * @param hotbarButton Which hotbar button gets clicked.
     */
    public void handleInventoryClick(Player player, ItemStack itemStack, ClickType clickType, int hotbarButton) {
        if (itemStack.isSimilar(Constants.PLACEHOLDER)) {
            return;
        }

        Optional<GameShopCategoryType> optionalCategoryType = getCategoryByItem(itemStack);
        if (optionalCategoryType.isPresent()) {
            GameShopCategoryType categoryType = optionalCategoryType.get();
            player.openInventory(getCategoryInventory(categoryType));
            return;
        }

        Optional<GameShopItem> optionalShopItem = getShopItemByItem(itemStack);
        if (!optionalShopItem.isPresent()) {
            return;
        }

        GameShopItem shopItem = optionalShopItem.get();
        if (!containsPriceType(player, shopItem)) {
            player.sendMessage(Constants.PREFIX + "Du hast nicht genug " + shopItem.getPriceType().getName());
            player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 3, 1);
            return;
        }

        if ((clickType != ClickType.NUMBER_KEY && clickType != ClickType.SHIFT_LEFT) || isSword(shopItem) || isArmor(shopItem) || isPickaxe(shopItem)) {
            addItem(player, shopItem);
            removeItem(player, shopItem, 1);
            player.playSound(player.getLocation(), Sound.EAT, 3, 1);
            return;
        }

        if (clickType == ClickType.NUMBER_KEY) {
            ItemStack hotbarItemStack = player.getInventory().getItem(hotbarButton);

            int freeSlot = getNextFreeItemSlot(player);
            if(freeSlot == -1) {
                player.sendMessage(Constants.PREFIX + "Du hast zu viele Items im Inventar");
                player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 3, 1);
                return;
            }

            if(hotbarItemStack.getType() != shopItem.getMaterial()) {
                player.getInventory().setItem(freeSlot, hotbarItemStack);
                setItem(player, shopItem, hotbarButton);
            } else {
                hotbarItemStack.setAmount(hotbarItemStack.getAmount() + shopItem.getAmount());
            }

            removeItem(player, shopItem, 1);
            player.playSound(player.getLocation(), Sound.EAT, 3, 1);
            return;
        }

        ItemStack priceType = shopItem.getPriceType().getItemStack();
        int price = shopItem.getPrice();
        int amount = getPriceTypeAmountInInventory(player, priceType) / price;

        for (int i = 0; i < amount; i++) {
            addItem(player, shopItem);
        }

        removeItem(player, shopItem, amount);
        player.playSound(player.getLocation(), Sound.EAT, 3, 1);

    }

    /**
     * Adds an item in the players inventory.
     *
     * @param player The player who gets the item added.
     * @param shopItem The ShopItem which gets added.
     */
    private void addItem(Player player, GameShopItem shopItem) {
        player.getInventory().addItem(shopItem.asItemStack());
    }

    /**
     * Sets an item in the players inventory.
     *
     * @param player The player who gets the item set.
     * @param shopItem The ShopItem which gets set.
     * @param slot The slot where the ShopItem gets set.
     */
    private void setItem(Player player, GameShopItem shopItem, int slot) {
        player.getInventory().setItem(slot, shopItem.asItemStack());
    }

    /**
     * Removes an item from the players inventory.
     *
     * @param player The player who gets item removed.
     * @param shopItem The ShopItem which gets removed.
     * @param amount The amount multiplied by the price.
     */
    private void removeItem(Player player, GameShopItem shopItem, int amount) {
        ItemStack priceType = shopItem.getPriceType().getItemStack();
        int price = shopItem.getPrice();
        player.getInventory().removeItem(new ItemBuilder(priceType).setAmount(price * amount).build());
        player.updateInventory();
    }

    /**
     * Checks if the player can buy this item.
     *
     * @param player The player who wants to buy.
     * @param shopItem The ShopItem to buy.
     * @return true if the player can buy it.
     */
    private boolean containsPriceType(Player player, GameShopItem shopItem) {
        ItemStack priceType = shopItem.getPriceType().getItemStack();
        int price = shopItem.getPrice();
        return player.getInventory().containsAtLeast(priceType, price);
    }

    /**
     * Gets the next free item slot.
     *
     * @param player The player for whom the next free item slot is.
     * @return -1 if there is no free slot.
     */
    private int getNextFreeItemSlot(Player player) {
        for(int slot = 0; slot < 36; slot++) {
            if(player.getInventory().getItem(slot) == null) {
                return slot;
            }
        }

        return -1;
    }

    /**
     * Gets the amount of the price type in the inventory.
     *
     * @param player The player whose amount is to be determined.
     * @param itemStack The price type.
     * @return the amount of the price type in the players inventory.
     */
    private int getPriceTypeAmountInInventory(Player player, ItemStack itemStack) {
        int amount = 0;
        for (ItemStack itemStacks : player.getInventory().getContents()) {
            if (itemStacks == null) {
                continue;
            }

            if (itemStacks.getType() != itemStack.getType()) {
                continue;
            }

            amount += itemStacks.getAmount();
        }

        if (amount > 64) {
            amount = 64;
        }

        return amount;
    }

    /**
     * Loads the main inventory of the shop.
     *
     * @return a new inventory.
     */
    private Inventory loadMainInventory() {
        Inventory inventory = Bukkit.createInventory(null, 45, "§8Shop");
        categoryDisplayItems.forEach(inventory::addItem);

        BukkitUtils.fillInventory(inventory, 9, 18);
        BukkitUtils.fillInventory(inventory, 18, 27);
        BukkitUtils.fillInventory(inventory, 36, 45);
        return inventory;
    }

    /**
     * Loads all display items for each category.
     *
     * @return a new List with all items.
     */
    private List<ItemStack> loadCategoryDisplayItems() {
        return bedWars.getFileManager().getShopCategoriesConfig().getShopCategories()
                .stream()
                .map(shopCategory -> new ItemBuilder(shopCategory.getDisplayedItem())
                        .setDisplayName(shopCategory.getName())
                        .addLoreAll(shopCategory.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Loads all category inventories.
     *
     * @return a new Map with an inventory for each category.
     */
    private Map<GameShopCategoryType, Inventory> loadCategoryInventories() {
        Map<GameShopCategoryType, Inventory> categoryInventories = new HashMap<>();

        bedWars.getFileManager().getShopCategoriesConfig().getShopCategories().forEach(shopCategory -> {
            Inventory inventory = Bukkit.createInventory(null, 45, "§8Shop");
            inventory.setContents(getMainInventory().getContents());
            GameShopCategoryType categoryType = shopCategory.getCategoryType();

            int slot = 27;
            for (GameShopItem shopItem : bedWars.getFileManager().getShopItemsConfig().getShopItems()) {
                if (shopItem.getCategoryType() != categoryType) {
                    continue;
                }

                inventory.setItem(slot, shopItem.asItemStack());
                slot++;
            }

            categoryInventories.put(categoryType, inventory);
        });

        return categoryInventories;
    }

    /**
     * Checks if the item is a sword.
     *
     * @param shopItem The ShopItem which is to check.
     * @return true if the item is a sword.
     */
    private boolean isSword(GameShopItem shopItem) {
        return shopItem.getMaterial().toString().endsWith("SWORD");
    }

    /**
     * Checks if the item is an armor.
     *
     * @param shopItem The ShopItem which is to check.
     * @return true if the item is an armor.
     */
    private boolean isArmor(GameShopItem shopItem) {
        return shopItem.getMaterial().toString().endsWith("HELMET") ||
                shopItem.getMaterial().toString().endsWith("LEGGINGS") ||
                shopItem.getMaterial().toString().endsWith("CHESTPLATE") ||
                shopItem.getMaterial().toString().endsWith("BOOTS");
    }

    /**
     * Checks if the item is a pickaxe.
     *
     * @param shopItem The ShopItem which is to check.
     * @return true if the item is a pickaxe.
     */
    private boolean isPickaxe(GameShopItem shopItem) {
        return shopItem.getMaterial().toString().endsWith("PICKAXE");
    }

    /**
     * Gets the category type by the clicked item.
     *
     * @param itemStack The ItemStack which gets clicked.
     * @return an Optional with a GameShopCategoryType.
     */
    private Optional<GameShopCategoryType> getCategoryByItem(ItemStack itemStack) {
        return bedWars.getFileManager().getShopCategoriesConfig().getShopCategories()
                .stream()
                .filter(shopCategory -> shopCategory.getName().equals(itemStack.getItemMeta().getDisplayName()))
                .map(GameShopCategory::getCategoryType)
                .findFirst();
    }

    /**
     * Gets the ShopItem by the clicked item.
     *
     * @param itemStack The ItemStack which gets clicked.
     * @return an Optional with a GameShopItem.
     */
    private Optional<GameShopItem> getShopItemByItem(ItemStack itemStack) {
        return bedWars.getFileManager().getShopItemsConfig().getShopItems()
                .stream()
                .filter(gameShopItem -> gameShopItem.getName().equals(itemStack.getItemMeta().getDisplayName()))
                .findFirst();
    }

    public Inventory getMainInventory() {
        return mainInventory;
    }

    public Inventory getCategoryInventory(GameShopCategoryType categoryType) {
        return categoryInventories.get(categoryType);
    }
}
