package de.deroq.bedwars.config;

import com.google.gson.Gson;
import de.deroq.bedwars.config.configs.BlocksConfig;
import de.deroq.bedwars.config.configs.SettingsConfig;
import de.deroq.bedwars.config.configs.ShopCategoriesConfig;
import de.deroq.bedwars.config.configs.ShopItemsConfig;

import java.io.*;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author deroq
 * @since 06.07.2022
 */

public class FileManager {

    private final File FOLDER;
    private final File SETTINGS_FILE;
    private final File SHOP_CATEGORIES_FILE;
    private final File SHOP_ITEMS_FILE;
    private final File BLOCKS_FILE;

    private SettingsConfig settingsConfig;
    private ShopCategoriesConfig shopCategoriesConfig;
    private ShopItemsConfig shopItemsConfig;
    private BlocksConfig blocksConfig;

    public FileManager() {
        this.FOLDER = new File("plugins/BedWars/");
        this.SETTINGS_FILE = new File(FOLDER.getPath(), "settings.json");
        this.SHOP_CATEGORIES_FILE = new File(FOLDER.getPath(), "shopCategories.json");
        this.SHOP_ITEMS_FILE = new File(FOLDER.getPath(), "shopItems.json");
        this.BLOCKS_FILE = new File(FOLDER.getPath(), "blocks.json");
    }

    public void loadFiles() {
        try {
            if (!FOLDER.exists()) {
                FOLDER.mkdirs();
            }

            if (FOLDER.isDirectory() && FOLDER.listFiles().length == 0) {
                createConfigs();
                return;
            }

            this.settingsConfig = (SettingsConfig) readConfig(SETTINGS_FILE, SettingsConfig.class);
            this.shopCategoriesConfig = (ShopCategoriesConfig) readConfig(SHOP_CATEGORIES_FILE, ShopCategoriesConfig.class);
            this.shopItemsConfig = (ShopItemsConfig) readConfig(SHOP_ITEMS_FILE, ShopItemsConfig.class);
            this.blocksConfig = (BlocksConfig) readConfig(BLOCKS_FILE, BlocksConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createConfigs() throws IOException {
        SETTINGS_FILE.createNewFile();
        SHOP_CATEGORIES_FILE.createNewFile();
        SHOP_ITEMS_FILE.createNewFile();
        BLOCKS_FILE.createNewFile();

        this.settingsConfig = SettingsConfig.create(SETTINGS_FILE);
        saveConfig(settingsConfig);

        this.shopCategoriesConfig = ShopCategoriesConfig.create(SHOP_CATEGORIES_FILE);
        saveConfig(shopCategoriesConfig);

        this.shopItemsConfig = ShopItemsConfig.create(SHOP_ITEMS_FILE);
        saveConfig(shopItemsConfig);

        this.blocksConfig = BlocksConfig.create(BLOCKS_FILE);
        saveConfig(blocksConfig);
    }

    public void saveConfig(Config config) throws IOException {
        Optional<File> optionalConfigFile = Arrays.stream(FOLDER.listFiles())
                .filter(file -> file.getName().equals(config.getFileName()))
                .findFirst();

        if (!optionalConfigFile.isPresent()) {
            throw new IOException("Error while getting file " + config.getFileName() + ": File can not be found");
        }

        File configFile = optionalConfigFile.get();
        try (FileWriter fileWriter = new FileWriter(configFile)) {
            fileWriter.write(new Gson().toJson(config));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Config readConfig(File file, Class<? extends Config> clazz) throws FileNotFoundException {
        return new Gson().fromJson(new FileReader(file), clazz);
    }

    public SettingsConfig getSettingsConfig() {
        return settingsConfig;
    }

    public ShopItemsConfig getShopItemsConfig() {
        return shopItemsConfig;
    }

    public ShopCategoriesConfig getShopCategoriesConfig() {
        return shopCategoriesConfig;
    }

    public BlocksConfig getBlocksConfig() {
        return blocksConfig;
    }
}
