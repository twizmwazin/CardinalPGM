package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by kevin on 11/20/14.
 */
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
            team.add((Player) sender);
        } catch (IndexOutOfBoundsException ex) {
            team = GameHandler.getGameHandler().getMatch().getTeamWithFewestPlayers();
            team.add((Player) sender);
        } catch (NullPointerException ex) {
            throw new CommandException("No team named " + cmd.getString(0));
        }

    }

}
