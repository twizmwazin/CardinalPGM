package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.ModuleContainer;
import org.bukkit.command.CommandSender;

/**
 * Created by kevin on 11/17/14.
 */
public class MatchCommand {

    private static ModuleContainer modules = GameHandler.getGameHandler().getMatch().getModules();

    @Command(aliases = {"matchinfo", "match"}, desc = "Shows information about the currently playing match", usage = "", min = 0, max = 0)
    public static void map(final CommandContext args, CommandSender sender) throws CommandException {
        sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "----------------" + ChatColor.RESET + " " + ChatColor.DARK_AQUA + "Match Info" + ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "----------------");
        //sender.sendMessage(ChatColor.DARK_PURPLE + "Time: " + ChatColor.GOLD + StringUtils.formatTime( modules.getTime().getTime()));
    }

}
