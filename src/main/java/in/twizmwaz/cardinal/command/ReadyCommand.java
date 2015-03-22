package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.startTimer.StartTimer;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReadyCommand {

    @Command(aliases = {"ready"}, desc = "Make your team ready.")
    public static void ready(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) throw new CommandException("Console cannot use this command!");
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.WAITING)) {
            TeamModule team = TeamUtils.getTeamByPlayer((Player) sender);
            if (!team.isReady()) {
                team.setReady(true);
                ChatUtils.getGlobalChannel().sendMessage(team.getCompleteName() + ChatColor.YELLOW + " is now ready");
                if (TeamUtils.teamsReady()) GameHandler.getGameHandler().getMatch().start(600);
            } else throw new CommandException("Your team is already ready!");
        } else throw new CommandException("You cannot ready up during or after a match");
    }

    @Command(aliases = {"unready"}, desc = "Make your team not ready.")
    public static void unready(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) throw new CommandException("Console cannot use this command!");
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.WAITING)) {
            TeamModule team = TeamUtils.getTeamByPlayer((Player) sender);
            if (team.isReady()) {
                team.setReady(false);
                ChatUtils.getGlobalChannel().sendMessage(team.getCompleteName() + ChatColor.YELLOW + " is no longer ready");
                if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING)) {
                    GameHandler.getGameHandler().getMatch().setState(MatchState.WAITING);
                    GameHandler.getGameHandler().getMatch().getModules().getModule(StartTimer.class).setCancelled(true);
                    ChatUtils.getGlobalChannel().sendMessage(ChatColor.RED + "Match start countdown cancelled because " + team.getCompleteName() + ChatColor.RED + " became un-ready.");
                }
            } else throw new CommandException("Your team is already not ready!");
        } else throw new CommandException("You cannot unready during or after a match");
    }

}
