package in.twizmwaz.cardinal.util;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public class MiscUtils {

    public static Color convertChatColorToColor(ChatColor chatColor) {
        if (chatColor.isColor()) {
            switch (chatColor) {
                case AQUA:
                    return Color.fromRGB(0, 255, 255);
                case BLACK:
                    return Color.fromRGB(0, 0, 0);
                case BLUE:
                    return Color.fromRGB(128, 128, 255);
                case DARK_AQUA:
                    return Color.fromRGB(0, 128, 128);
                case DARK_BLUE:
                    return Color.fromRGB(0, 0, 255);
                case DARK_GRAY:
                    return Color.fromRGB(64, 64, 64);
                case DARK_GREEN:
                    return Color.fromRGB(0, 128, 0);
                case DARK_PURPLE:
                    return Color.fromRGB(128, 0, 128);
                case DARK_RED:
                    return Color.fromRGB(192, 0, 0);
                case GOLD:
                    return Color.fromRGB(192, 0, 0);
                case GRAY:
                    return Color.fromRGB(196, 0, 0);
                case GREEN:
                    return Color.fromRGB(255, 196, 0);
                case LIGHT_PURPLE:
                    return Color.fromRGB(255, 0, 255);
                case RED:
                    return Color.fromRGB(255, 0, 0);
                case WHITE:
                    return Color.fromRGB(255, 255, 255);
                case YELLOW:
                    return Color.fromRGB(255, 255, 0);
                default:
                    return Color.fromRGB(128, 128, 128);
            }
        } else {
            return Color.fromBGR(128, 128, 128);
        }
    }
}
