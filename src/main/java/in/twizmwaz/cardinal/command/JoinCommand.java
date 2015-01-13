package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.JoinType;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand {

    @Command(aliases = {"join", "j"}, desc = "Join a team", usage = "[team]")
    public static void join(final CommandContext cmd, CommandSender sender) throws CommandException {
        PgmTeam team;
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED) || GameHandler.getGameHandler().getMatch().getState().equals(MatchState.CYCLING)) {
            sender.sendMessage(ChatColor.RED + "Match is over " + ChatColor.DARK_AQUA + "- " + ChatColor.GOLD + "Please wait for the server to cycle");
            return;
        }
        try {
            team = GameHandler.getGameHandler().getMatch().getTeamByName(cmd.getString(0));
            PgmTeam playerTeam = GameHandler.getGameHandler().getMatch().getTeam((Player) sender);
            if (team == playerTeam)
                throw new CommandException(ChatColor.RED + "You have already joined " + playerTeam.getCompleteName());
            team.add((Player) sender, JoinType.VOLUNTARY);
        } catch (IndexOutOfBoundsException ex) {
            team = GameHandler.getGameHandler().getMatch().getTeamWithFewestPlayers();
            team.add((Player) sender, JoinType.VOLUNTARY);
        } catch (NullPointerException ex) {
            throw new CommandException("No teams matched query.");
        }
    }

    @Command(aliases = {"leave"}, desc = "Leave the game")
    public static void leave(final CommandContext cmd, CommandSender sender) {
        Bukkit.getServer().dispatchCommand(sender, "join observers");
    }
}
