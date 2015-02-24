package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SnowflakesCommand {

    @Command(aliases = {"snowflakes"}, desc = "View your own or another player's snowflake count.", usage = "[player]")
    public static void settings(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (cmd.argsLength() == 0) {
            Bukkit.dispatchCommand(sender, "snowflakes " + sender.getName());
        } else {
            String name = Bukkit.getOfflinePlayer(cmd.getString(0)).getName();
            sender.sendMessage(ChatColor.DARK_PURPLE + (sender.getName().equals(name) ? "You have " : TeamUtils.getTeamColorByPlayer(Bukkit.getOfflinePlayer(name)) + name + ChatColor.DARK_PURPLE + " has ") + ChatColor.GOLD + (Cardinal.getCardinalDatabase().get(Bukkit.getOfflinePlayer(cmd.getString(0)), "snowflakes").equals("") ? "0" : Cardinal.getCardinalDatabase().get(Bukkit.getOfflinePlayer(cmd.getString(0)), "snowflakes")) + " snowflake" + (Cardinal.getCardinalDatabase().get(Bukkit.getOfflinePlayer(cmd.getString(0)), "snowflakes").equals("1") ? "" : "s"));
        }
    }

}
