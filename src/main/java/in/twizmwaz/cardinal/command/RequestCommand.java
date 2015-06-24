package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RequestCommand {

    @Command(aliases = {"request"}, desc = "Request an action to ranked players.", min = 1, usage = "<message>")
    public static void request(final CommandContext cmd, CommandSender sender) throws CommandException {
        sender.sendMessage(ChatColor.YELLOW + "Your request has been submitted");
        ChatUtils.getAdminChannel().sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() + ChatColor.RESET + ": " + cmd.getJoinedStrings(0));
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Settings.getSettingByName("Sounds") != null && Settings.getSettingByName("Sounds").getValueByPlayer(player).getValue().equalsIgnoreCase("on")) {
                if (player.hasPermission("cardinal.chat.admin")) {
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 10, 1);
                }
            }
        }
    }
}