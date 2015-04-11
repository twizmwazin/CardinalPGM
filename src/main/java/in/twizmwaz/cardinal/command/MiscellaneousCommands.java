package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.util.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MiscellaneousCommands {

    @Command(aliases = {"say"}, desc = "Console message to all players.")
    @CommandPermissions("cardinal.say")
    public static void say(final CommandContext cmd, CommandSender sender) throws CommandException {
        String locale = ChatUtils.getLocale(sender);
        if (sender.hasPermission("cardinal.say") && (cmd.argsLength() == 0)) {
            sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_NOT_ENOUGH_ARGS_SAY).getMessage(locale));
        }
        if (cmd.argsLength() >= 1) {
            ChatUtils.getGlobalChannel().sendMessage(ChatColor.WHITE + "<" + ChatColor.GOLD + "×" + ChatColor.AQUA + "Console" + ChatColor.WHITE + "> " + cmd.getJoinedStrings(0));
        }
    }

    @Command(aliases = {"broadcast"}, desc = "Send a message to all players.")
    @CommandPermissions("cardinal.broadcast")
    public static void broadcast(final CommandContext cmd, CommandSender sender) throws CommandException {
        String locale = ChatUtils.getLocale(sender);
        if (sender.hasPermission("cardinal.broadcast") && (cmd.argsLength() == 0)) {
            sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_NOT_ENOUGH_ARGS_BROADCAST).getMessage(locale));
        }
        if (cmd.argsLength() >= 1) {
            ChatUtils.getGlobalChannel().sendMessage(ChatColor.RED + "[Broadcast] " + cmd.getJoinedStrings(0));
        }
    }
}