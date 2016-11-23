package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.UpdateHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class CardinalCommand {

    @Command(aliases = "cardinal", flags = "vru", desc = "Various functions related to Cardinal.", min = 0, max = 0)
    public static void cardinal(final CommandContext cmd, CommandSender sender) throws CommandPermissionsException {
        if (cmd.hasFlag('v')) {
            sender.sendMessage(ChatColor.GOLD + ChatConstant.UI_VERSION.asMessage(new UnlocalizedChatMessage(Cardinal.getInstance().getDescription().getVersion())).getMessage(ChatUtil.getLocale(sender)));
            sender.sendMessage(ChatColor.GOLD + ChatConstant.UI_JAVA_VERSION.asMessage(new UnlocalizedChatMessage(System.getProperty("java.version"))).getMessage(ChatUtil.getLocale(sender)));
            if (System.getProperty("java.version").startsWith("1.7")) {
                sender.sendMessage(ChatColor.DARK_RED + ChatConstant.UI_JAVA_UPDATE.getMessage(ChatUtil.getLocale(sender)));
            }
            Bukkit.getScheduler().runTaskAsynchronously(Cardinal.getInstance(), UpdateHandler.getUpdateHandler().getNotificationTask(sender));
        }
        if (cmd.hasFlag('r')) {
            if (sender.hasPermission("cardinal.reload")) {
                Cardinal.getInstance().reloadConfig();
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_CONFIG_RELOAD)).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()));
                Cardinal.getInstance().registerRanks();
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_RANKS_RELOAD)).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()));
            } else {
                throw new CommandPermissionsException();
            }
        }
        if (cmd.hasFlag('u')) {
            if (sender.hasPermission("cardinal.update")) {
                Bukkit.getScheduler().runTaskAsynchronously(Cardinal.getInstance(), UpdateHandler.getUpdateHandler().getUpdateTask(sender));
            } else {
                throw new CommandPermissionsException();
            }
        }
    }

}
