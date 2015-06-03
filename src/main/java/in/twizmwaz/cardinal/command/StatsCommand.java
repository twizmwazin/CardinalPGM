package in.twizmwaz.cardinal.command;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.stats.Stats;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;

public class StatsCommand {

    @Command(aliases = {"stats", "statistics"}, desc = "View a player's stats.", usage = "[player]")
    public static void stats(final CommandContext cmd, CommandSender sender) {
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

    @Command(aliases = {"statspage", "statisticspage", "reportpage"}, desc = "View a player's stats.", usage = "[player]")
    public static void statsPage(final CommandContext cmd, CommandSender sender) {
        if (!Cardinal.getInstance().getConfig().getBoolean("html.upload")) {
            sender.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", ChatConstant.ERROR_MATCH_REPORT_NOT_ENABLED.asMessage()).getMessage(ChatUtils.getLocale(sender)));
            return;
        }

        if (GameHandler.getGameHandler().getMatch().getState() != MatchState.ENDED) {
            sender.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", ChatConstant.ERROR_STATS_NOT_UPLOADED.asMessage()).getMessage(ChatUtils.getLocale(sender)));
            return;
        }
        if (GameHandler.getGameHandler().getMatch().getModules().getModule(Stats.class).getStatsPage() == null || GameHandler.getGameHandler().getMatch().getModules().getModule(Stats.class).getStatsPage().contains("error"))
            sender.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", ChatConstant.UI_MATCH_REPORT_FAILED.asMessage()).getMessage(ChatUtils.getLocale(sender)));
        else sender.sendMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", ChatConstant.UI_MATCH_REPORT_SUCCESS.asMessage(new UnlocalizedChatMessage(GameHandler.getGameHandler().getMatch().getModules().getModule(Stats.class).getStatsPage()))).getMessage(ChatUtils.getLocale(sender)));
    }

}
