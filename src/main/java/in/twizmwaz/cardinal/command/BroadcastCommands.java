package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Players;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastCommands {

    @Command(aliases = {"say"}, desc = "Sends a message from the console.", min = 1, usage = "<message>")
    @CommandPermissions("cardinal.say")
    public static void say(final CommandContext cmd, CommandSender sender) throws CommandException {
        ChatUtil.getGlobalChannel().sendMessage(ChatColor.WHITE + "<" + Players.getName(Bukkit.getConsoleSender()) + ChatColor.WHITE + "> " + cmd.getJoinedStrings(0));
    }

    @Command(aliases = {"broadcast", "bc"}, desc = "Broadcasts a message to all players.", min = 1, usage = "<message>")
    @CommandPermissions("cardinal.broadcast")
    public static void broadcast(final CommandContext cmd, CommandSender sender) throws CommandException {
        ChatUtil.getGlobalChannel().sendMessage(ChatColor.RED + "[Broadcast] " + cmd.getJoinedStrings(0));
    }

    @Command(aliases = {"me"}, desc = "Send a global message starting with your name", min = 1, usage = "<message>")
    @CommandPermissions("cardinal.me")
    public static void me(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_CONSOLE_NO_USE).getMessage(ChatUtil.getLocale(sender)));
        if (!GameHandler.getGameHandler().getGlobalMute()) {
            ChatUtil.getGlobalChannel().sendMessage("\u002A " + Players.getName(sender) + " " + ChatColor.YELLOW + cmd.getJoinedStrings(0));
        }
    }
}