package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Protocols;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand {

    @Command(aliases = {"list"}, desc = "Lists all online players.", flags = "v")
    public static void list(final CommandContext args, CommandSender sender) {
        boolean version = args.hasFlag('v');
        String result = ChatColor.GRAY + ChatConstant.MISC_ONLINE.asMessage().getMessage(ChatUtil.getLocale(sender)) +
                " (" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + "): " + ChatColor.RESET;
        for (Player player : Bukkit.getOnlinePlayers()) {
            result += player.getDisplayName();
            if (version) {
                result += ChatColor.GRAY + " (" + Protocols.getName(player.getProtocolVersion()) + ")";
            }
            result += ChatColor.RESET + ", ";
        }
        if (result.endsWith(", ")) {
            result = result.substring(0, result.length() - 2);
        }
        sender.sendMessage(result);
    }
}
