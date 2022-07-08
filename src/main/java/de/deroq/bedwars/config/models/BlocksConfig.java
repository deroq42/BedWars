package de.deroq.bedwars.config.models;

import de.deroq.bedwars.config.Config;
import org.bukkit.Material;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlocksConfig extends Config {

    private final Set<Material> blocks;

    private BlocksConfig(File file) {
        super(file);
        this.blocks = Stream.of(
                Material.SANDSTONE,
                Material.ENDER_STONE,
                Material.ENDER_CHEST,
                Material.CHEST,
                Material.IRON_BLOCK,
                Material.WEB,
                Material.GLASS,
                Material.GLOWSTONE,
                Material.LADDER,
                Material.TNT,
                Material.FIRE,
                Material.PACKED_ICE,
                Material.CAKE_BLOCK).collect(Collectors.toSet());
    }

    public Set<Material> getBlocks() {
        return blocks;
    }

    public static BlocksConfig create(File file) {
        return new BlocksConfig(file);
    }
}
