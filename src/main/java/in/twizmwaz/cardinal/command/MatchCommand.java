package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.command.CommandSender;

public class MatchCommand {

    @Command(aliases = {"matchinfo", "match"}, desc = "Shows information about the currently playing match", usage = "", min = 0, max = 0)
    public static void map(final CommandContext args, CommandSender sender) throws CommandException {
        sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "----------------" + ChatColor.RESET + " " + ChatColor.DARK_AQUA + "Match Info" + ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "----------------");
    }

}
