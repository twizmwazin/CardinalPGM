package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.startTimer.StartTimer;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReadyCommand {

    @Command(aliases = {"ready"}, desc = "State that your team is ready")
    public static void ready(final CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = (Player) sender;
        TeamModule team = TeamUtils.getTeamByPlayer(player);
        if (!team.isObserver()) {
            if (!team.isReady()) {
                team.setReady(true);
                Bukkit.broadcastMessage(team.getCompleteName() + ChatColor.YELLOW + " is now ready");
                if (TeamUtils.teamsReady()) {
                    GameHandler.getGameHandler().getMatch().start(30 * 20);
                }
            } else player.sendMessage(ChatColor.RED + "Your team is already ready");
        } else player.sendMessage(team.getCompleteName() + ChatColor.YELLOW + " cannot be ready");
    }

    @Command(aliases = {"unready"}, desc = "State that your team is not ready")
    public static void unready(final CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = (Player) sender;
        TeamModule team = TeamUtils.getTeamByPlayer(player);
        if (!team.isObserver()) {
            if (team.isReady()) {
                team.setReady(false);
                Bukkit.broadcastMessage(team.getCompleteName() + ChatColor.YELLOW + " is no longer ready");
                if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING)) {
                    GameHandler.getGameHandler().getMatch().setState(MatchState.WAITING);
                    GameHandler.getGameHandler().getMatch().getModules().getModule(StartTimer.class).setCancelled(true);
                    Bukkit.broadcastMessage(ChatColor.RED + "Match start countdown cancelled because " + team.getCompleteName() + ChatColor.RED + " became un-ready.");
                }
            } else player.sendMessage(ChatColor.RED + "Your team is already un-ready");
        } else player.sendMessage(team.getCompleteName() + ChatColor.YELLOW + " cannot be ready");
    }

}
