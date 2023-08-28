package org.lacraft.util.api;

import lombok.AllArgsConstructor;

public class ColorUtil {

    @AllArgsConstructor
    public enum Color {
        BLACK("black"),
        DARK_BLUE("dark_blue"),
        DARK_GREEN("dark_green"),
        DARK_AQUA("dark_aqua"),
        DARK_RED("dark_red"),
        DARK_PURPLE("dark_purple"),
        GOLD("gold"),
        GRAY("gray"),
        DARK_GRAY("dark_gray"),
        BLUE("blue"),
        GREEN("green"),
        AQUA("aqua"),
        RED("red"),
        LIGHT_PURPLE("light_purple"),
        YELLOW("yellow"),
        WHITE("white");
        ;
        private final String colorName;

    }

    public static String colorText(String text, Color color) {
        return String.format("<%s>%s</%s>", color.colorName, text, color.colorName);
    }
}
