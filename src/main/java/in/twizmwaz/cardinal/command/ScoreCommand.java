package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.MiscUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScoreCommand {

    @Command(aliases = {"score"}, desc = "Shows the score of the current match.", usage = "")
    public static void map(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender.hasPermission("cardinal.score") || (sender instanceof Player && TeamUtils.getTeamByPlayer((Player) sender) != null && TeamUtils.getTeamByPlayer((Player) sender).isObserver())) {
            TimeLimit.Result result = GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult();
            if (result.equals(TimeLimit.Result.HIGHEST_SCORE)) {
                int score = 0;
                for (TeamModule team : TeamUtils.getTeams()) {
                    if (!team.isObserver()) {
                        sender.sendMessage(ChatColor.RED + "Calculating scores for " + team.getCompleteName());
                        for (ScoreModule scoreModule : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                            if (scoreModule.getScore() > score) {
                                score = scoreModule.getScore();
                            }
                        }
                    }
                }
                if (TimeLimit.getMatchWinner() == null) {
                    sender.sendMessage(ChatColor.GOLD + "Teams are tied with " + score + " points");
                } else {
                    sender.sendMessage(TimeLimit.getMatchWinner().getCompleteName() + ChatColor.GOLD + " is winning with " + score + " points");
                }
            } else if (result.equals(TimeLimit.Result.TIE)) {
                for (TeamModule team : TeamUtils.getTeams()) {
                    if (!team.isObserver()) {
                        sender.sendMessage(ChatColor.RED + "Calculating scores for " + team.getCompleteName());
                    }
                }
                sender.sendMessage(ChatColor.GOLD + "Teams are tied");
            } else if (result.equals(TimeLimit.Result.TEAM)) {
                for (TeamModule team : TeamUtils.getTeams()) {
                    if (!team.isObserver()) {
                        sender.sendMessage(ChatColor.RED + "Calculating scores for " + team.getCompleteName());
                    }
                }
                sender.sendMessage(GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getTeam().getCompleteName() + ChatColor.GOLD + " is winning");
            } else if (result.equals(TimeLimit.Result.MOST_PLAYERS)) {
                int players = 0;
                for (TeamModule team : TeamUtils.getTeams()) {
                    if (!team.isObserver()) {
                        sender.sendMessage(ChatColor.RED + "Calculating scores for " + team.getCompleteName());
                        if (team.size() > players) {
                            players = team.size();
                        }
                    }
                }
                if (TimeLimit.getMatchWinner() == null) {
                    sender.sendMessage(ChatColor.GOLD + "Teams are tied with " + players + " players");
                } else {
                    sender.sendMessage(TimeLimit.getMatchWinner().getCompleteName() + ChatColor.GOLD + " is winning with " + players + " players");
                }
            } else if (result.equals(TimeLimit.Result.MOST_OBJECTIVES)) {
                for (TeamModule team : TeamUtils.getTeams()) {
                    if (!team.isObserver()) {
                        sender.sendMessage(ChatColor.RED + "Calculating scores for " + team.getCompleteName());
                        for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                            if (obj.isComplete()) {
                                sender.sendMessage((obj instanceof WoolObjective ? MiscUtils.convertDyeColorToChatColor(((WoolObjective) obj).getColor()) : (obj instanceof CoreObjective ? ChatColor.RED : ChatColor.AQUA)) + WordUtils.capitalizeFully(obj.getName().replaceAll("_", " ")) + ChatColor.GRAY + " was completed!");
                            } else if (obj.isTouched()) {
                                sender.sendMessage((obj instanceof WoolObjective ? MiscUtils.convertDyeColorToChatColor(((WoolObjective) obj).getColor()) : (obj instanceof CoreObjective ? ChatColor.RED : ChatColor.AQUA)) + WordUtils.capitalizeFully(obj.getName().replaceAll("_", " ")) + ChatColor.GRAY + " was touched!");
                            } else {
                                sender.sendMessage((obj instanceof WoolObjective ? MiscUtils.convertDyeColorToChatColor(((WoolObjective) obj).getColor()) : (obj instanceof CoreObjective ? ChatColor.RED : ChatColor.AQUA)) + WordUtils.capitalizeFully(obj.getName().replaceAll("_", " ")) + ChatColor.GRAY + " is untouched!");
                            }
                        }
                    }
                }
                if (TimeLimit.getMatchWinner() == null) {
                    sender.sendMessage(ChatColor.GOLD + "Teams are tied");
                } else {
                    int completed = 0;
                    int touched = 0;
                    for (GameObjective obj : TeamUtils.getShownObjectives(TimeLimit.getMatchWinner())) {
                        if (obj.isComplete()) completed ++;
                        else if (obj.isTouched()) touched ++;
                    }
                    sender.sendMessage(TimeLimit.getMatchWinner().getCompleteName() + ChatColor.GOLD + " is winning with " + completed + " completed and " + touched + " touched objectives");
                }
            } else {
                throw new CommandException("Could not calculate scores for teams!");
            }
        } else throw new CommandPermissionsException();
    }

}
