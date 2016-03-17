package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.UpdateHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.rotation.exception.RotationLoadException;
import in.twizmwaz.cardinal.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class CardinalCommand {

    @Command(aliases = "cardinal", flags = "vr", desc = "Various functions related to Cardinal.", usage = "[-v, -r]")
    public static void cardinal(final CommandContext cmd, CommandSender sender) throws CommandPermissionsException {
        if (cmd.hasFlag('v')) {
            sender.sendMessage(ChatColor.GOLD + ChatConstant.UI_VERSION.asMessage(new UnlocalizedChatMessage(Cardinal.getInstance().getDescription().getVersion())).getMessage(ChatUtil.getLocale(sender)));
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
    }

    @Command(aliases = "newmaps", desc = "Reload map repository.")
    @CommandPermissions("cardinal.newmaps")
    public static void newMaps(final CommandContext cmd, CommandSender sender) {
        try {
            GameHandler.getGameHandler().getRotation().setupRotation();
            sender.sendMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_REPO_RELOAD,
                    "" + GameHandler.getGameHandler().getRotation().getLoaded().size())).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()));
        } catch (RotationLoadException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe(new LocalizedChatMessage(ChatConstant.GENERIC_REPO_RELOAD_FAIL, "" + GameHandler.getGameHandler().getRotation().getLoaded().size()).getMessage(Locale.getDefault().toString()));
            sender.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_REPO_RELOAD_FAIL,
                    "" + GameHandler.getGameHandler().getRotation().getLoaded().size())).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()));
        }
    }

}
