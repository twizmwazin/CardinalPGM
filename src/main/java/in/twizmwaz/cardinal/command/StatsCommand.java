package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.stats.Stats;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class StatsCommand {

    @Command(aliases = "stats", desc = "View a player's stats.", usage = "[player]")
    public static void cardinal(final CommandContext cmd, CommandSender sender) {
        if (cmd.argsLength() == 0) {
            Bukkit.dispatchCommand(sender, "stats " + sender.getName());
        } else {
            OfflinePlayer player = Bukkit.getOfflinePlayer(cmd.getString(0));
            sender.sendMessage(TeamUtils.getTeamColorByPlayer(player) + player.getName() + "'s " + ChatColor.GRAY + "Match Statistics");
            sender.sendMessage(ChatColor.GRAY + " Kills: " + ChatColor.GREEN + GameHandler.getGameHandler().getMatch().getModules().getModule(Stats.class).getKillsByPlayer(player));
            sender.sendMessage(ChatColor.GRAY + " Deaths: " + ChatColor.DARK_RED + GameHandler.getGameHandler().getMatch().getModules().getModule(Stats.class).getDeathsByPlayer(player));
            sender.sendMessage(ChatColor.GRAY + " KD: " + ChatColor.GOLD + (Math.round(GameHandler.getGameHandler().getMatch().getModules().getModule(Stats.class).getKdByPlayer(player) * 100.0) / 100.0));
        }
    }

}
