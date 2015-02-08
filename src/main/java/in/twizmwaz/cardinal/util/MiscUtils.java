package in.twizmwaz.cardinal.util;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MiscUtils {

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

    public static Color convertHexToRGB(String color) {
        return Color.fromRGB(Integer.valueOf(color.substring(0, 2), 16), Integer.valueOf(color.substring(2, 4), 16), Integer.valueOf(color.substring(4, 6), 16));
    }

    public static <T> List<T> getSortedHashMapKeyset(HashMap<T, Integer> sorting) {
        List<T> types = new ArrayList<>();
        HashMap<T, Integer> clone = new HashMap<>();
        for (T player : sorting.keySet()) {
            clone.put(player, sorting.get(player));
        }
        for (int i = 0; i < sorting.size(); i ++) {
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
