package in.twizmwaz.cardinal.command;

import com.google.common.base.Optional;
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

    @Command(aliases = {"join", "play"}, desc = "Join a team.", usage = "[team]")
    public static void join(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED) || GameHandler.getGameHandler().getMatch().getState().equals(MatchState.CYCLING)) {
                ChatUtils.sendWarningMessage((Player) sender, ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_MATCH_OVER).getMessage(((Player) sender).getLocale()));
                return;
            }
            Optional<TeamModule> originalTeam = TeamUtils.getTeamByPlayer((Player) sender);
            if (cmd.argsLength() == 0 && originalTeam.isPresent() && !originalTeam.get().isObserver()) {
                throw new CommandException(ChatUtils.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_JOINED, TeamUtils.getTeamByPlayer((Player) sender).get().getCompleteName() + ChatColor.RED).getMessage(((Player) sender).getLocale())));
            }
            Optional<TeamModule> destinationTeam = Optional.absent();
            if (cmd.argsLength() > 0) {
                for (TeamModule teamModule : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
                    if (teamModule.getName().toLowerCase().startsWith(cmd.getJoinedStrings(0).toLowerCase())) {
                        destinationTeam = Optional.of(teamModule);
                        break;
                    }
                }
                if (destinationTeam.isPresent()) {
                    if (!destinationTeam.get().contains(sender)) {
                        destinationTeam.get().add((Player) sender, false);
                    } else {
                        throw new CommandException(ChatUtils.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_JOINED, destinationTeam.get().getCompleteName() + ChatColor.RED).getMessage(((Player) sender).getLocale())));
                    }
                } else
                    throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_TEAM_MATCH).getMessage(((Player) sender).getLocale()));
            } else {
                destinationTeam = TeamUtils.getTeamWithFewestPlayers(GameHandler.getGameHandler().getMatch());
                if (destinationTeam.isPresent()) {
                    destinationTeam.get().add((Player) sender, false);
                }
            }
        } else throw new CommandException("Console cannot use this command.");
    }

    @Command(aliases = {"leave"}, desc = "Leave the game.")
    public static void leave(final CommandContext cmd, CommandSender sender) {
        Bukkit.getServer().dispatchCommand(sender, "join observers");
    }
}
