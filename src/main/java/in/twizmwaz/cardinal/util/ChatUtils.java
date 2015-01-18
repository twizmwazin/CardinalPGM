package in.twizmwaz.cardinal.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtils {

    public static void sendWarningMessage(Player player, String msg) {
        player.sendMessage(ChatColor.YELLOW + " \u26A0 " + ChatColor.RED + msg);
    }

}
