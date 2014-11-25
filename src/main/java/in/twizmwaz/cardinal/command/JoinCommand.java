package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by kevin on 11/20/14.
 */
public class JoinCommand {

    @Command(aliases = {"join", "j"}, desc = "Join a team", usage = "[team]")
    public static void join(final CommandContext cmd, CommandSender sender) throws CommandException {
        PgmTeam team;
        if (GameHandler.getGameHandler().getMatch().getTeamByName(cmd.getString(0)) != null) {
            team = GameHandler.getGameHandler().getMatch().getTeamByName(cmd.getString(0));
        } else if (GameHandler.getGameHandler().getMatch().getTeamById(cmd.getString(0)) != null) {
            team = GameHandler.getGameHandler().getMatch().getTeamById(cmd.getString(0));

        } else throw new CommandException("No team named" + cmd.getString(0));
        team.add((Player) sender);
        ((Player) sender).setPlayerListName(team.getColor() + Bukkit.getPlayer(sender.getName()).getDisplayName());

    }

}
