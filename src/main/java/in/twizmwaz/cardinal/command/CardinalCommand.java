package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.UpdateHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CardinalCommand {

    @Command(aliases = "cardinal", flags = "v", desc = "Various functions related to Cardinal.")
    public static void cardinal(final CommandContext cmd, CommandSender sender) {
        if (cmd.hasFlag('v')) {
            sender.sendMessage(ChatColor.GOLD + ChatConstant.UI_VERSION.asMessage(new UnlocalizedChatMessage(Cardinal.getInstance().getDescription().getVersion())).getMessage(ChatUtils.getLocale(sender)));
            Bukkit.getScheduler().runTaskAsynchronously(Cardinal.getInstance(), UpdateHandler.getUpdateHandler().getNotificationTask(sender));
        }
    }

}
