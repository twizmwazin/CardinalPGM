package in.twizmwaz.cardinal.command;

import com.google.common.base.Optional;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScoreCommand {

    @Command(aliases = {"score"}, desc = "Shows the score of the current match.", usage = "")
    public static void score(final CommandContext args, CommandSender sender) throws CommandException {
        if (!sender.hasPermission("cardinal.score") && sender instanceof Player) {
            Optional<TeamModule> team = Teams.getTeamByPlayer((Player) sender);
            if (GameHandler.getGameHandler().getMatch().isRunning() && (!team.isPresent() || !team.get().isObserver())) {
                throw new CommandPermissionsException();
            }
        }
        TimeLimit.Result result = GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult();
        if (result.equals(TimeLimit.Result.HIGHEST_SCORE)) {
            int score = 0;
            for (TeamModule team : Teams.getTeams()) {
                if (!team.isObserver()) {
                    sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.GENERIC_CALCULATING_SCORES_FOR, team.getCompleteName() + ChatColor.RED).getMessage(ChatUtil.getLocale(sender)));
                    for (ScoreModule scoreModule : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                        if (scoreModule.getScore() > score) {
                            score = scoreModule.getScore();
                        }
                    }
                }
            }
            if (TimeLimit.getMatchWinner() == null) {
                sender.sendMessage(ChatColor.GOLD + new LocalizedChatMessage(ChatConstant.GENERIC_TEAMS_ARE_TIED_WITH_POINTS, "" + score).getMessage(ChatUtil.getLocale(sender)));
            } else {
                sender.sendMessage(ChatColor.GOLD + new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_IS_WINNING_WITH_POINTS, TimeLimit.getMatchWinner().getCompleteName() + ChatColor.GOLD, "" + score).getMessage(ChatUtil.getLocale(sender)));
            }
        } else if (result.equals(TimeLimit.Result.TIE)) {
            for (TeamModule team : Teams.getTeams()) {
                if (!team.isObserver()) {
                    sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.GENERIC_CALCULATING_SCORES_FOR, team.getCompleteName() + ChatColor.RED).getMessage(ChatUtil.getLocale(sender)));
                }
            }
            sender.sendMessage(ChatColor.GOLD + ChatConstant.GENERIC_TEAMS_ARE_TIED.getMessage(ChatUtil.getLocale(sender)));
        } else if (result.equals(TimeLimit.Result.TEAM)) {
            for (TeamModule team : Teams.getTeams()) {
                if (!team.isObserver()) {
                    sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.GENERIC_CALCULATING_SCORES_FOR, team.getCompleteName() + ChatColor.RED).getMessage(ChatUtil.getLocale(sender)));
                }
            }
            sender.sendMessage(ChatColor.GOLD + new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_IS_WINNING, GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getTeam().getCompleteName() + ChatColor.GOLD).getMessage(ChatUtil.getLocale(sender)));
        } else if (result.equals(TimeLimit.Result.MOST_PLAYERS)) {
            int players = 0;
            for (TeamModule team : Teams.getTeams()) {
                if (!team.isObserver()) {
                    sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.GENERIC_CALCULATING_SCORES_FOR, team.getCompleteName() + ChatColor.RED).getMessage(ChatUtil.getLocale(sender)));
                    if (team.size() > players) {
                        players = team.size();
                    }
                }
            }
            if (TimeLimit.getMatchWinner() == null) {
                sender.sendMessage(ChatColor.GOLD + new LocalizedChatMessage(ChatConstant.GENERIC_TEAMS_ARE_TIED_WITH_PLAYERS, "" + players).getMessage(ChatUtil.getLocale(sender)));
            } else {
                sender.sendMessage(ChatColor.GOLD + new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_IS_WINNING_WITH_PLAYERS, TimeLimit.getMatchWinner().getCompleteName() + ChatColor.GOLD, "" + players).getMessage(ChatUtil.getLocale(sender)));
            }
        } else if (result.equals(TimeLimit.Result.MOST_OBJECTIVES)) {
            for (TeamModule team : Teams.getTeams()) {
                if (!team.isObserver()) {
                    sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.GENERIC_CALCULATING_SCORES_FOR, team.getCompleteName() + ChatColor.RED).getMessage(ChatUtil.getLocale(sender)));
                    for (GameObjective obj : Teams.getShownObjectives(team)) {
                        if (obj.isComplete()) {
                            sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_OBJECTIVE_WAS_COMPLETED, (obj instanceof WoolObjective ? MiscUtil.convertDyeColorToChatColor(((WoolObjective) obj).getColor()) : (obj instanceof CoreObjective ? ChatColor.RED : ChatColor.AQUA)) + WordUtils.capitalizeFully(obj.getName().replaceAll("_", " ")) + ChatColor.GRAY).getMessage(ChatUtil.getLocale(sender)));
                        } else if (obj.isTouched()) {
                            sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_OBJECTIVE_WAS_TOUCHED, (obj instanceof WoolObjective ? MiscUtil.convertDyeColorToChatColor(((WoolObjective) obj).getColor()) : (obj instanceof CoreObjective ? ChatColor.RED : ChatColor.AQUA)) + WordUtils.capitalizeFully(obj.getName().replaceAll("_", " ")) + ChatColor.GRAY).getMessage(ChatUtil.getLocale(sender)));
                        } else {
                            sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_OBJECTIVE_IS_UNTOUCHED, (obj instanceof WoolObjective ? MiscUtil.convertDyeColorToChatColor(((WoolObjective) obj).getColor()) : (obj instanceof CoreObjective ? ChatColor.RED : ChatColor.AQUA)) + WordUtils.capitalizeFully(obj.getName().replaceAll("_", " ")) + ChatColor.GRAY).getMessage(ChatUtil.getLocale(sender)));
                        }
                    }
                }
            }
            if (TimeLimit.getMatchWinner() == null) {
                sender.sendMessage(ChatColor.GOLD + ChatConstant.GENERIC_TEAMS_ARE_TIED.getMessage(ChatUtil.getLocale(sender)));
            } else {
                int completed = 0;
                int touched = 0;
                for (GameObjective obj : Teams.getShownObjectives(TimeLimit.getMatchWinner())) {
                    if (obj.isComplete()) completed++;
                    else if (obj.isTouched()) touched++;
                }
                sender.sendMessage(ChatColor.GOLD + new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_IS_WINNING_WITH_OBJECTIVES, TimeLimit.getMatchWinner().getCompleteName() + ChatColor.GOLD, "" + completed, "" + touched).getMessage(ChatUtil.getLocale(sender)));
            }
        } else {
            throw new CommandException(ChatConstant.ERROR_CANNOT_CALCULATE_SCORES.getMessage(ChatUtil.getLocale(sender)));
        }
    }

}
