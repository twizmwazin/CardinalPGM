package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
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
        if (sender instanceof Player) {
            TeamModule team = null;
            if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED) || GameHandler.getGameHandler().getMatch().getState().equals(MatchState.CYCLING)) {
                ChatUtils.sendWarningMessage((Player) sender, ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_MATCH_OVER).getMessage(((Player) sender).getLocale()));
                return;
            }
            if (cmd.argsLength() == 0 && !TeamUtils.getTeamByPlayer((Player) sender).isObserver()) {
                throw new CommandException(ChatUtils.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_JOINED, TeamUtils.getTeamByPlayer((Player) sender).getCompleteName() + ChatColor.RED).getMessage(((Player) sender).getLocale())));
            }
            try {
                for (TeamModule teamModule : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
                    if (teamModule.getName().toLowerCase().startsWith(cmd.getJoinedStrings(0).toLowerCase())) {
                        team = teamModule;
                        break;
                    }
                }
                if (team != null) {
                    if (!team.contains(sender)) {
                        team.add((Player) sender, false);
                    } else {
                        throw new CommandException(ChatUtils.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_JOINED, team.getCompleteName() + ChatColor.RED).getMessage(((Player) sender).getLocale())));
                    }
                } else
                    throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_TEAM_MATCH).getMessage(((Player) sender).getLocale()));
            } catch (IndexOutOfBoundsException ex) {
                team = TeamUtils.getTeamWithFewestPlayers(GameHandler.getGameHandler().getMatch());
                team.add((Player) sender, false);
            }
        } else throw new CommandException("Console cannot use this command.");
    }

    @Command(aliases = {"leave"}, desc = "Leave the game.")
    public static void leave(final CommandContext cmd, CommandSender sender) {
        Bukkit.getServer().dispatchCommand(sender, "join observers");
    }
}
