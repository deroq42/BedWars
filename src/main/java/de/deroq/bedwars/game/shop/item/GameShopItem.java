package de.deroq.bedwars.game.shop.item;

import de.deroq.bedwars.game.models.ItemBuilder;
import de.deroq.bedwars.game.shop.category.GameShopCategoryType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class GameShopItem {

    private final String name;
    private final Material material;
    private final int amount;
    private final List<String> description;
    private final Map<String, Integer> enchantments;
    private final GameShopCategoryType categoryType;
    private final GameShopItemPriceType priceType;
    private final int price;

    private GameShopItem(String name, Material material, int amount, List<String> description, Map<String, Integer> enchantments, GameShopCategoryType categoryType, GameShopItemPriceType priceType, int price) {
        this.name = name;
        this.material = material;
        this.amount = amount;
        this.description = description;
        this.enchantments = enchantments;
        this.categoryType = categoryType;
        this.priceType = priceType;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public List<String> getDescription() {
        return description;
    }

    public Map<String, Integer> getEnchantments() {
        return enchantments;
    }

    public GameShopCategoryType getCategoryType() {
        return categoryType;
    }

    public GameShopItemPriceType getPriceType() {
        return priceType;
    }

    public int getPrice() {
        return price;
    }

    public ItemStack asItemStack() {
        return new ItemBuilder(material)
                .setDisplayName(name)
                .setAmount(amount)
                .addLoreLine("§fPreis: " + priceType.getColorCode() + price + " " + priceType.getName())
                .addLoreAll(description)
                .addEnchantmentAll(enchantments)
                .build();
    }

    public static class builder {

        private String name;
        private Material material;
        private int amount;
        private List<String> lore;
        private Map<String, Integer> enchantments;
        private GameShopCategoryType categoryType;
        private GameShopItemPriceType priceType;
        private int price;

        public builder setName(String name) {
            this.name = name;
            return this;
        }

        public builder setMaterial(Material material) {
            this.material = material;
            return this;
        }

        public builder setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public builder setLore(List<String> lore) {
            this.lore = lore;
            return this;
        }

        public builder setEnchantments(Map<String, Integer> enchantments) {
            this.enchantments = enchantments;
            return this;
        }

        public builder setCategoryType(GameShopCategoryType categoryType) {
            this.categoryType = categoryType;
            return this;
        }

        public builder setPriceType(GameShopItemPriceType priceType) {
            this.priceType = priceType;
            return this;
        }

        public builder setPrice(int price) {
            this.price = price;
            return this;
        }

        public GameShopItem build() {
            return new GameShopItem(
                    name,
                    material,
                    amount,
                    lore,
                    enchantments,
                    categoryType,
                    priceType,
                    price);
        }
    }
}
