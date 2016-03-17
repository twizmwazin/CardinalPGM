package in.twizmwaz.cardinal.command;

import com.google.common.base.Optional;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.TimeLimitChangeEvent;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.module.modules.timeNotifications.TimeNotifications;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Strings;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TimeLimitCommand {

    @Command(aliases = {"timelimit", "tl"}, desc = "Modify the time limit of the current match.", usage = "<cancel, add, set> <time> [result]")
    public static void timeLimit(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (cmd.argsLength() == 0) {
            if (TimeLimit.getMatchTimeLimit() != 0) {
                sender.sendMessage(ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.GENERIC_TIME_LIMIT_WITH_RESULT, ChatColor.AQUA + Strings.formatTimeWithMillis(TimeLimit.getMatchTimeLimit()) + ChatColor.YELLOW, ChatColor.WHITE + (GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().equals(TimeLimit.Result.TEAM) ? GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getTeam().getCompleteName() + " wins" : GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().name().toLowerCase().replaceAll("_", " ")) + ChatColor.YELLOW).getMessage(ChatUtil.getLocale(sender)));
            } else {
                sender.sendMessage(ChatColor.YELLOW + ChatConstant.GENERIC_NO_TIME_LIMIT.getMessage(ChatUtil.getLocale(sender)));
            }
        } else if (cmd.argsLength() > 1 && (cmd.getString(0).equalsIgnoreCase("add") || cmd.getString(0).equalsIgnoreCase("set"))) {
            if (!sender.hasPermission("cardinal.timelimit")) {
                throw new CommandPermissionsException();
            }
            int time;
            try {
                time = Strings.timeStringToSeconds(cmd.getString(1));
            } catch (NumberFormatException e) {
                if (!cmd.getString(1).equalsIgnoreCase("cancel")) {
                    throw new CommandException(ChatConstant.ERROR_TIME_FORMAT_STRING.getMessage(ChatUtil.getLocale(sender)));
                }
                time = 0;
            }
            for (TimeLimit module : GameHandler.getGameHandler().getMatch().getModules().getModules(TimeLimit.class)) {
                if (cmd.getString(0).equalsIgnoreCase("set")) {
                    module.setTimeLimit(time);
                } else {
                    module.setTimeLimit(module.getTimeLimit() + time);
                }
            }
            TimeNotifications.resetNextMessage();
            if (cmd.argsLength() > 2) {
                try {
                    if (TimeLimit.Result.valueOf(cmd.getJoinedStrings(2).toUpperCase().replaceAll(" ", "_")).equals(TimeLimit.Result.TEAM)) {
                        throw new CommandException(ChatConstant.ERROR_NO_RESULT_MATCH.getMessage(ChatUtil.getLocale(sender)));
                    }
                    for (TimeLimit module : GameHandler.getGameHandler().getMatch().getModules().getModules(TimeLimit.class)) {
                        module.setResult(TimeLimit.Result.valueOf(cmd.getJoinedStrings(2).toUpperCase().replaceAll(" ", "_")));
                    }
                } catch (IllegalArgumentException e) {
                    if (Teams.getTeamByName(cmd.getJoinedStrings(2)) == null) {
                        throw new CommandException(ChatConstant.ERROR_NO_RESULT_MATCH.getMessage(ChatUtil.getLocale(sender)));
                    }
                    for (TimeLimit module : GameHandler.getGameHandler().getMatch().getModules().getModules(TimeLimit.class)) {
                        module.setResult(TimeLimit.Result.TEAM);
                        Optional<TeamModule> team = Teams.getTeamByName(cmd.getJoinedStrings(2));
                        if (team.isPresent()) module.setTeam(team.get());
                    }
                }
            }
            if (TimeLimit.getMatchTimeLimit() != 0) {
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}" + " " + ChatColor.AQUA + "{1}" + ChatColor.YELLOW + " with the result " + ChatColor.WHITE + "{2}", "The time limit is", Strings.formatTimeWithMillis(TimeLimit.getMatchTimeLimit()), GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().equals(TimeLimit.Result.TEAM) ? GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getTeam().getCompleteName() + " wins" : GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().name().toLowerCase().replaceAll("_", " ")).getMessage(ChatUtil.getLocale(sender)));
            } else {
                sender.sendMessage(ChatColor.YELLOW + ChatConstant.GENERIC_TIME_LIMIT_CANCELLED.getMessage(ChatUtil.getLocale(sender)));
            }
            Bukkit.getServer().getPluginManager().callEvent(new TimeLimitChangeEvent());
        } else {
            if (!sender.hasPermission("cardinal.timelimit")) {
                throw new CommandPermissionsException();
            }
            if (!cmd.getString(0).equalsIgnoreCase("cancel")) {
                throw new CommandUsageException(ChatConstant.ERROR_TOO_FEW_ARGUMENTS.getMessage(ChatUtil.getLocale(sender)), "/timelimit <add, set> <time> [result]");
            }
            Bukkit.dispatchCommand(sender, "timelimit set cancel");
        }
    }

}
