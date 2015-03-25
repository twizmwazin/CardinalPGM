package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.module.modules.timeNotifications.TimeNotifications;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.StringUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TimeLimitCommand {

    @Command(aliases = {"timelimit", "tl"}, desc = "Modify the time limit of the current match.")
    public static void timeLimit(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (cmd.argsLength() == 0) {
            if (TimeLimit.getMatchTimeLimit() != 0) {
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}" + " " + ChatColor.AQUA + "{1}" + ChatColor.YELLOW + " with the result " + ChatColor.WHITE + "{2}", "The time limit is", StringUtils.formatTimeWithMillis(TimeLimit.getMatchTimeLimit()), GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().equals(TimeLimit.Result.TEAM) ? GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getTeam().getCompleteName() + " wins" : GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().name().toLowerCase().replaceAll("_", " ")).getMessage(ChatUtils.getLocale(sender)));
            } else {
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", "There is no time limit.").getMessage(ChatUtils.getLocale(sender)));
            }
        } else if (cmd.argsLength() == 1) {
            if (!sender.hasPermission("cardinal.timelimit")) throw new CommandPermissionsException();
            int time;
            try {
                time = StringUtils.timeStringToSeconds(cmd.getString(0));
            } catch (NumberFormatException e) {
                if (cmd.getString(0).equalsIgnoreCase("cancel")) {
                    time = 0;
                } else {
                    throw new CommandException("Time format expected, string received instead.");
                }
            }
            for (TimeLimit module : GameHandler.getGameHandler().getMatch().getModules().getModules(TimeLimit.class)) {
                module.setTimeLimit(time);
            }
            TimeNotifications.resetNextMessage();
            if (TimeLimit.getMatchTimeLimit() != 0) {
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}" + " " + ChatColor.AQUA + "{1}" + ChatColor.YELLOW + " with the result " + ChatColor.WHITE + "{2}", "The time limit is", StringUtils.formatTimeWithMillis(TimeLimit.getMatchTimeLimit()), GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().equals(TimeLimit.Result.TEAM) ? GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getTeam().getCompleteName() + " wins" : GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().name().toLowerCase().replaceAll("_", " ")).getMessage(ChatUtils.getLocale(sender)));
            } else {
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", "Time limit cancelled").getMessage(ChatUtils.getLocale(sender)));
            }
        } else {
            if (!sender.hasPermission("cardinal.timelimit")) throw new CommandPermissionsException();
            int time;
            try {
                time = StringUtils.timeStringToSeconds(cmd.getString(0));
            } catch (NumberFormatException e) {
                if (cmd.getString(0).equalsIgnoreCase("cancel")) {
                    time = 0;
                } else {
                    throw new CommandException("Time format expected, string received instead.");
                }
            }
            for (TimeLimit module : GameHandler.getGameHandler().getMatch().getModules().getModules(TimeLimit.class)) {
                module.setTimeLimit(time);
            }
            TimeNotifications.resetNextMessage();
            try {
                if (TimeLimit.Result.valueOf(cmd.getJoinedStrings(1).toUpperCase().replaceAll(" ", "_")).equals(TimeLimit.Result.TEAM)) {
                    throw new CommandException("No results match query.");
                }
                for (TimeLimit module : GameHandler.getGameHandler().getMatch().getModules().getModules(TimeLimit.class)) {
                    module.setResult(TimeLimit.Result.valueOf(cmd.getJoinedStrings(1).toUpperCase().replaceAll(" ", "_")));
                }
            } catch (IllegalArgumentException e) {
                if (TeamUtils.getTeamByName(cmd.getJoinedStrings(1)) != null) {
                    for (TimeLimit module : GameHandler.getGameHandler().getMatch().getModules().getModules(TimeLimit.class)) {
                        module.setResult(TimeLimit.Result.TEAM);
                        module.setTeam(TeamUtils.getTeamByName(cmd.getJoinedStrings(1)));
                    }
                } else {
                    throw new CommandException("No results match query.");
                }
            }
            if (TimeLimit.getMatchTimeLimit() != 0) {
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}" + " " + ChatColor.AQUA + "{1}" + ChatColor.YELLOW + " with the result " + ChatColor.WHITE + "{2}", "The time limit is", StringUtils.formatTimeWithMillis(TimeLimit.getMatchTimeLimit()), GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().equals(TimeLimit.Result.TEAM) ? GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getTeam().getCompleteName() + " wins" : GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().name().toLowerCase().replaceAll("_", " ")).getMessage(ChatUtils.getLocale(sender)));
            } else {
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", "Time limit cancelled").getMessage(ChatUtils.getLocale(sender)));
            }
        }
    }

}
