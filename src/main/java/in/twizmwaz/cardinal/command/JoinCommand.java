package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand {

    @Command(aliases = {"join"}, desc = "Join a team.", usage = "[team]")
    public static void join(final CommandContext cmd, CommandSender sender) throws CommandException {
        TeamModule team = null;
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED) || GameHandler.getGameHandler().getMatch().getState().equals(MatchState.CYCLING)) {
            ChatUtils.sendWarningMessage((Player) sender, ChatColor.RED + "Match is over");
            return;
        }
        try {
            for (TeamModule teamModule : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
                if (teamModule.getName().toLowerCase().startsWith(cmd.getString(0).toLowerCase())) {
                    team = teamModule;
                    break;
                }
            }
            if (team != null) {
                if (!team.contains(sender)) {
                    team.add((Player) sender, false);
                } else throw new CommandException(ChatUtils.getWarningMessage(ChatColor.RED + "You have already joined " + team.getCompleteName()));
            } else throw new CommandException("No teams matched query.");
        } catch (IndexOutOfBoundsException ex) {
            team = TeamUtils.getTeamWithFewestPlayers(GameHandler.getGameHandler().getMatch());
            team.add((Player) sender, false);
        }
    }

    @Command(aliases = {"leave"}, desc = "Leave the game.")
    public static void leave(final CommandContext cmd, CommandSender sender) {
        Bukkit.getServer().dispatchCommand(sender, "join observers");
    }
}
