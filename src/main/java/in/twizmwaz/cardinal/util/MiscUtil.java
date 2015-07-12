package in.twizmwaz.cardinal.util;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MiscUtil {

    public static Color convertChatColorToColor(ChatColor chatColor) {
        if (chatColor.isColor()) {
            switch (chatColor) {
                case AQUA:
                    return convertHexToRGB("55FFFF");
                case BLACK:
                    return convertHexToRGB("000000");
                case BLUE:
                    return convertHexToRGB("5555FF");
                case DARK_AQUA:
                    return convertHexToRGB("00AAAA");
                case DARK_BLUE:
                    return convertHexToRGB("0000AA");
                case DARK_GRAY:
                    return convertHexToRGB("555555");
                case DARK_GREEN:
                    return convertHexToRGB("00AA00");
                case DARK_PURPLE:
                    return convertHexToRGB("AA00AA");
                case DARK_RED:
                    return convertHexToRGB("AA0000");
                case GOLD:
                    return convertHexToRGB("FFAA00");
                case GRAY:
                    return convertHexToRGB("AAAAAA");
                case GREEN:
                    return convertHexToRGB("55FF55");
                case LIGHT_PURPLE:
                    return convertHexToRGB("FF55FF");
                case RED:
                    return convertHexToRGB("FF5555");
                case WHITE:
                    return convertHexToRGB("FFFFFF");
                case YELLOW:
                    return convertHexToRGB("FFFF55");
                default:
                    return convertHexToRGB("AAAAAA");
            }
        } else {
            return convertHexToRGB("AAAAAA");
        }
    }

    public static ChatColor convertDyeColorToChatColor(DyeColor dye) {
        switch (dye) {
            case WHITE:
                return ChatColor.WHITE;
            case ORANGE:
                return ChatColor.GOLD;
            case MAGENTA:
                return ChatColor.LIGHT_PURPLE;
            case LIGHT_BLUE:
                return ChatColor.BLUE;
            case YELLOW:
                return ChatColor.YELLOW;
            case LIME:
                return ChatColor.GREEN;
            case PINK:
                return ChatColor.RED;
            case GRAY:
                return ChatColor.GRAY;
            case SILVER:
                return ChatColor.GRAY;
            case CYAN:
                return ChatColor.DARK_AQUA;
            case PURPLE:
                return ChatColor.DARK_PURPLE;
            case BLUE:
                return ChatColor.DARK_BLUE;
            case BROWN:
                return ChatColor.GOLD;
            case GREEN:
                return ChatColor.DARK_GREEN;
            case RED:
                return ChatColor.DARK_RED;
            case BLACK:
                return ChatColor.BLACK;
        }

        return ChatColor.WHITE;
    }

    public static Color convertHexToRGB(String color) {
        return Color.fromRGB(Integer.valueOf(color.substring(0, 2), 16), Integer.valueOf(color.substring(2, 4), 16), Integer.valueOf(color.substring(4, 6), 16));
    }

    public static <T> List<T> getSortedHashMapKeyset(HashMap<T, Integer> sorting) {
        List<T> types = new ArrayList<>();
        HashMap<T, Integer> clone = new HashMap<>();
        for (T player : sorting.keySet()) {
            clone.put(player, sorting.get(player));
        }
        for (int i = 0; i < sorting.size(); i++) {
            int highestNumber = Integer.MIN_VALUE;
            T highestType = null;
            for (T player : clone.keySet()) {
                if (clone.get(player) > highestNumber) {
                    highestNumber = clone.get(player);
                    highestType = player;
                }
            }
            clone.remove(highestType);
            types.add(highestType);
        }
        return types;
    }
}
