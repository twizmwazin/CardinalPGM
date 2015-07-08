package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.ScoreboardUtils;
import in.twizmwaz.cardinal.util.StringUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MatchCommand {

    @Command(aliases = {"matchinfo", "match"}, desc = "Shows information about the currently playing match.", usage = "")
    public static void match(final CommandContext args, CommandSender sender) throws CommandException {
        sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "------" + ChatColor.DARK_AQUA + " " + new LocalizedChatMessage(ChatConstant.UI_MATCH_INFO).getMessage(ChatUtils.getLocale(sender)) + " " + ChatColor.GRAY + "(" + GameHandler.getGameHandler().getMatch().getNumber() + ")" + ChatColor.RED + " " + ChatColor.STRIKETHROUGH + "------");
        sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_TIME).getMessage(ChatUtils.getLocale(sender)) + ": " + ChatColor.GOLD + (Cardinal.getInstance().getConfig().getBoolean("matchTimeMillis") ? StringUtils.formatTimeWithMillis(MatchTimer.getTimeInSeconds()) : StringUtils.formatTime(MatchTimer.getTimeInSeconds())));
        String teams = "";
        boolean hasObjectives = false;
        for (TeamModule team : TeamUtils.getTeams()) {
            int players = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (TeamUtils.getTeamByPlayer(player).isPresent()) {
                    if (TeamUtils.getTeamByPlayer(player).get() == team) {
                        players++;
                    }
                }
            }
            teams += team.getCompleteName() + ChatColor.GRAY + ": " + ChatColor.RESET + players + (team.isObserver() ? "" : ChatColor.GRAY + "/" + team.getMax() + ChatColor.AQUA + " | ");
            if (TeamUtils.getShownObjectives(team).size() > 0) hasObjectives = true;
        }
        if (ScoreboardUtils.getHills().size() > 0) hasObjectives = true;
        sender.sendMessage(teams);
        Match match = GameHandler.getGameHandler().getMatch();
        if (match.isRunning() || match.getState().equals(MatchState.ENDED) || match.getState().equals(MatchState.CYCLING)) {
            if (hasObjectives) {
                sender.sendMessage(ChatColor.RED + "---- " + new LocalizedChatMessage(ChatConstant.UI_GOALS).getMessage(ChatUtils.getLocale(sender)) + " ----");
                for (TeamModule team : TeamUtils.getTeams()) {
                    if (!team.isObserver()) {
                        if (TeamUtils.getShownObjectives(team).size() > 0 || ScoreboardUtils.getHills().size() > 0) {
                            String objectives = "";
                            for (GameObjective objective : TeamUtils.getShownObjectives(team)) {
                                objectives += (objective.isComplete() ? ChatColor.GREEN : ChatColor.DARK_RED) + WordUtils.capitalizeFully(objective.getName().replaceAll("_", " ")) + "  ";
                            }
                            for (HillObjective hill : ScoreboardUtils.getHills()) {
                                if (hill.getTeam() != null) {
                                    if (hill.getTeam() == team) {
                                        objectives += ChatColor.GREEN + WordUtils.capitalizeFully(hill.getName().replaceAll("_", " ") + "  ");
                                        break;
                                    }
                                }
                                objectives += ChatColor.DARK_RED + WordUtils.capitalizeFully(hill.getName().replaceAll("_", " ") + "  ");
                            }
                            objectives = objectives.trim();
                            sender.sendMessage(team.getCompleteName() + ChatColor.GRAY + ": " + objectives);
                        }
                    }
                }
            }
            if (ScoreModule.matchHasScoring()) {
                String score = "";
                for (ScoreModule scoreModule : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                    score += scoreModule.getTeam().getColor() + "" + scoreModule.getScore() + " ";
                }
                score = score.trim();
                double timeRemaining;
                if (TimeLimit.getMatchTimeLimit() != 0) {
                    timeRemaining = TimeLimit.getMatchTimeLimit() - MatchTimer.getTimeInSeconds();
                    sender.sendMessage(ChatColor.DARK_AQUA + "Score: " + score + (TimeLimit.getMatchTimeLimit() != 0 ? ChatColor.RED + "  " + StringUtils.formatTime(timeRemaining) : "") + (ScoreModule.matchHasMax() ? ChatColor.GRAY + "  [" + ScoreModule.max() + "]" : ""));
                }
            }
        }
    }

}
