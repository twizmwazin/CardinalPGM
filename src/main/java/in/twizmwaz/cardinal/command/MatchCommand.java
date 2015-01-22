package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import org.bukkit.command.CommandSender;

public class MatchCommand {

    @Command(aliases = {"matchinfo", "match"}, desc = "Shows information about the currently playing match", usage = "")
    public static void match(final CommandContext args, CommandSender sender) throws CommandException {
        sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "------" + ChatColor.DARK_AQUA + " Match Info " + ChatColor.GRAY + "(" + GameHandler.getGameHandler().getMatch().getNumber() + ")" + ChatColor.RED + " " + ChatColor.STRIKETHROUGH + "------");
        sender.sendMessage(ChatColor.DARK_PURPLE + "Time: " + ChatColor.GOLD + "");
    }

}
