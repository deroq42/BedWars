package de.deroq.bedwars.game.team.models;

import org.bukkit.DyeColor;

/**
 * @author deroq
 * @since 06.07.2022
 */

public enum GameTeamType {

    RED("§c", "Rot", DyeColor.RED),
    BLUE("§9", "Blau", DyeColor.BLUE),
    YELLOW("§e", "Gelb", DyeColor.YELLOW),
    GREEN("§2", "Grün", DyeColor.GREEN),
    ORANGE("§6", "Orange", DyeColor.ORANGE),
    CYAN("§b", "Türkis", DyeColor.CYAN),
    PINK("§d", "Pink", DyeColor.PINK),
    BLACK("§0", "Schwarz", DyeColor.BLACK);

    private final String colorCode;
    private final String name;
    private final DyeColor woolColor;

    GameTeamType(String colorCode, String name, DyeColor woolColor) {
        this.colorCode = colorCode;
        this.name = name;
        this.woolColor = woolColor;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getName() {
        return name;
    }

    public DyeColor getWoolColor() {
        return woolColor;
    }
}
