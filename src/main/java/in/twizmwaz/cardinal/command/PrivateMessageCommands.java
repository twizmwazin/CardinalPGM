package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class PrivateMessageCommands {

    @Command(aliases = {"msg", "message", "pm", "privatemessage", "whisper", "tell"}, desc = "Send a private message to a player.", usage = "/{cmd} <player> <message>", min = 2)
    public static void pm(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) throw new CommandException(ChatConstant.ERROR_PLAYER_COMMAND.getMessage(Locale.getDefault().toString()));

        Player target = Bukkit.getPlayer(cmd.getString(0), sender);
        if (target == null) {
            throw new CommandException(ChatConstant.ERROR_PLAYER_NOT_FOUND.getMessage(((Player) sender).getLocale()));
        }
        if (Settings.getSettingByName("PrivateMessages") == null || Settings.getSettingByName("PrivateMessages").getValueByPlayer(target).getValue().equalsIgnoreCase("all")) {
            target.sendMessage(ChatColor.GRAY + "(From " + TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() + ChatColor.GRAY + "): " + ChatColor.RESET + assembleMessage(cmd));
            sender.sendMessage(ChatColor.GRAY + "(To " + TeamUtils.getTeamColorByPlayer(target) + target.getDisplayName() + ChatColor.GRAY + "): " + ChatColor.RESET + assembleMessage(cmd));
        } else {
            sender.sendMessage(new LocalizedChatMessage(ChatConstant.ERROR_PLAYER_DISABLED_PMS, target.getDisplayName()).getMessage(((Player) sender).getLocale()));
        }
    }

    private static String assembleMessage(CommandContext context) {
        return context.getJoinedStrings(1);
    }
}
