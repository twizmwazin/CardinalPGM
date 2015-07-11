package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.util.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class BroadcastCommands {

    @Command(aliases = {"say"}, desc = "Sends a message from the console.", min = 1, usage = "<message>")
    @CommandPermissions("cardinal.say")
    public static void say(final CommandContext cmd, CommandSender sender) throws CommandException {
        ChatUtils.getGlobalChannel().sendMessage(ChatColor.WHITE + "<" + ChatColor.GOLD + "\u2756" + ChatColor.DARK_AQUA + "Console" + ChatColor.WHITE + "> " + cmd.getJoinedStrings(0));
    }

    @Command(aliases = {"broadcast", "bc"}, desc = "Broadcasts a message to all players.", min = 1, usage = "<message>")
    @CommandPermissions("cardinal.broadcast")
    public static void broadcast(final CommandContext cmd, CommandSender sender) throws CommandException {
        ChatUtils.getGlobalChannel().sendMessage(ChatColor.RED + "[Broadcast] " + cmd.getJoinedStrings(0));
    }

}