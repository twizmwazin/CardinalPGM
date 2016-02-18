package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Players;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class SnowflakesCommand {

    @Command(aliases = {"snowflakes"}, desc = "View your own or another player's snowflake count.", usage = "[player]")
    public static void settings(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (cmd.argsLength() == 0) {
            Bukkit.dispatchCommand(sender, "snowflakes " + sender.getName());
        } else {
            OfflinePlayer player = Bukkit.getOfflinePlayer(cmd.getString(0));
            String snowflakes = Cardinal.getCardinalDatabase().get(player, "snowflakes");
            if (snowflakes.equals("")) snowflakes = "0";
            if (sender.equals(player)) {
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "{0} " + ChatColor.GOLD + "{1} {2}", ChatConstant.MISC_YOU_HAVE.asMessage(), new UnlocalizedChatMessage("{0}", snowflakes), snowflakes.equals("1") ? ChatConstant.SNOWFLAKES_SNOWFLAKE.asMessage() : ChatConstant.SNOWFLAKES_SNOWFLAKES.asMessage()).getMessage(ChatUtil.getLocale(sender)));
            } else {
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.DARK_PURPLE + "{0} " + ChatColor.GOLD + "{1} {2}", new LocalizedChatMessage(ChatConstant.MISC_HAS, Players.getName(player) + ChatColor.DARK_PURPLE), new UnlocalizedChatMessage("{0}", snowflakes), snowflakes.equals("1") ? ChatConstant.SNOWFLAKES_SNOWFLAKE.asMessage() : ChatConstant.SNOWFLAKES_SNOWFLAKES.asMessage()).getMessage(ChatUtil.getLocale(sender)));
            }
        }
    }

}
