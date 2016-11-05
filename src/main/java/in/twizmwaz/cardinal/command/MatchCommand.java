package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Config;
import in.twizmwaz.cardinal.util.Scoreboards;
import in.twizmwaz.cardinal.util.Strings;
import in.twizmwaz.cardinal.util.Teams;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MatchCommand {

    @Command(aliases = {"matchinfo", "match"}, desc = "Shows information about the currently playing match.", usage = "")
    public static void match(final CommandContext args, CommandSender sender) throws CommandException {
        TeamModule senderTeam = Teams.getTeamById("observers").get();
        if (sender instanceof Player) senderTeam = Teams.getTeamByPlayer((Player) sender).or(senderTeam);

        sender.sendMessage(ChatColor.STRIKETHROUGH + "                              " + ChatColor.RESET + ChatColor.YELLOW + " " + new LocalizedChatMessage(ChatConstant.UI_MATCH, GameHandler.getGameHandler().getMatch().getNumber() + "").getMessage(ChatUtil.getLocale(sender)) + " " + ChatColor.RESET + ChatColor.STRIKETHROUGH + "                              ");
        sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_TIME).getMessage(ChatUtil.getLocale(sender)) + ": " + ChatColor.GOLD + (Config.matchTimeMillis ? Strings.formatTimeWithMillis(MatchTimer.getTimeInSeconds()) : Strings.formatTime(MatchTimer.getTimeInSeconds())));
        String teams = "";
        boolean hasObjectives = false;
        for (TeamModule team : Teams.getTeams()) {
            teams += team.getCompleteName() + ChatColor.GRAY + ": " + ChatColor.RESET + team.size() + (team.isObserver() ? "" : ChatColor.GRAY + "/" + team.getMax() + ChatColor.DARK_GRAY + " | ");
            if (Teams.getShownObjectives(team).size() > 0) hasObjectives = true;
        }
        sender.sendMessage(teams);
        if (Scoreboards.getHills().size() > 0) hasObjectives = true;
        if (hasObjectives) {
            sender.sendMessage(ChatColor.DARK_AQUA + new LocalizedChatMessage(ChatConstant.UI_GOALS).getMessage(ChatUtil.getLocale(sender)) + ":");
            for (TeamModule team : Teams.getTeams()) {
                if (!team.isObserver() && (Teams.getShownObjectives(team).size() > 0 || Scoreboards.getHills().size() > 0)) {
                    String objectives = "";
                    for (GameObjective objective : Teams.getShownObjectives(team)) {
                        objectives += objective.getScoreboardHandler().getPrefix(senderTeam) + " " + ChatColor.RESET + WordUtils.capitalizeFully(objective.getName().replaceAll("_", " ")) + "  ";
                    }
                    for (GameObjective hill : Scoreboards.getHills()) {
                        objectives += hill.getScoreboardHandler().getPrefix(senderTeam) + " " + WordUtils.capitalizeFully(hill.getName().replaceAll("_", " ") + "  ");
                    }
                    objectives = objectives.trim();
                    sender.sendMessage("  " + team.getCompleteName() + ChatColor.GRAY + ": " + objectives);
                }
            }
        }
        if (ScoreModule.matchHasScoring()) {
            String score = "";
            for (ScoreModule scoreModule : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                score += scoreModule.getTeam().getColor() + "" + scoreModule.getScore() + " ";
            }
            sender.sendMessage(new UnlocalizedChatMessage(ChatColor.DARK_AQUA + "{0}: " + score.trim() + (ScoreModule.matchHasMax() ? ChatColor.GRAY + "  [" + ScoreModule.max() + "]" : ""), ChatConstant.MISC_SCORE.asMessage()).getMessage(ChatUtil.getLocale(sender)));
        }
    }

}
