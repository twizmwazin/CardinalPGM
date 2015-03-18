package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScoreCommand {

    @Command(aliases = {"score"}, desc = "Shows the score of the current match.", usage = "")
    public static void map(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender.hasPermission("cardinal.score") || (sender instanceof Player && TeamUtils.getTeamByPlayer((Player) sender) != null && TeamUtils.getTeamByPlayer((Player) sender).isObserver())) {
            if (ScoreModule.matchHasScoring()) {
                int winningScore = 0;
                TeamModule winningTeam = null;
                for (TeamModule team : TeamUtils.getTeams()) {
                    if (!team.isObserver()) {
                        sender.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "Calculating score for {0}", team.getCompleteName() + ChatColor.RED).getMessage(ChatUtils.getLocale(sender)));
                        for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                            if (score.getTeam() == team) {
                                if (score.getScore() > winningScore) {
                                    winningTeam = team;
                                    winningScore = score.getScore();
                                } else if (score.getScore() == winningScore) {
                                    winningTeam = null;
                                }
                            }
                        }
                    }
                }
                if (winningTeam != null) {
                    sender.sendMessage(new UnlocalizedChatMessage(ChatColor.GOLD + "{0} is winning with {1} point{2}!", winningTeam.getCompleteName() + ChatColor.GOLD, winningScore + "", winningScore != 1 ? "s" : "").getMessage(ChatUtils.getLocale(sender)));
                } else {
                    sender.sendMessage(new UnlocalizedChatMessage(ChatColor.GOLD + "Teams tied with {0} point{1}!", winningScore + "", winningScore != 1 ? "s" : "").getMessage(ChatUtils.getLocale(sender)));
                }
            } else if (GameHandler.getGameHandler().getMatch().getModules().getModule(Blitz.class) != null) {
                int winningPlayers = 0;
                TeamModule winningTeam = null;
                for (TeamModule team : TeamUtils.getTeams()) {
                    if (!team.isObserver()) {
                        sender.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "Calculating score for {0}", team.getCompleteName() + ChatColor.RED).getMessage(ChatUtils.getLocale(sender)));
                        if (team.size() > winningPlayers) {
                            winningTeam = team;
                            winningPlayers = team.size();
                        } else if (team.size() == winningPlayers) {
                            winningTeam = null;
                        }
                    }
                }
                if (winningTeam != null) {
                    sender.sendMessage(new UnlocalizedChatMessage(ChatColor.GOLD + "{0} is winning with {1} player{2}!", winningTeam.getCompleteName() + ChatColor.GOLD, winningPlayers + "", winningPlayers != 1 ? "s" : "").getMessage(ChatUtils.getLocale(sender)));
                } else {
                    sender.sendMessage(new UnlocalizedChatMessage(ChatColor.GOLD + "Teams tied with {0} player{1}!", winningPlayers + "", winningPlayers != 1 ? "s" : "").getMessage(ChatUtils.getLocale(sender)));
                }
            } else {

            }
        } else throw new CommandPermissionsException();
    }

}
