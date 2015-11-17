package in.twizmwaz.cardinal.command;

import com.google.common.base.Optional;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.teamRegister.TeamRegisterModule;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class JoinCommand {

    @Command(aliases = {"join", "play"}, desc = "Join a team.", usage = "[team]")
    public static void join(final CommandContext cmd, CommandSender sender) throws CommandException {
        Map<String,String> playerTeams = GameHandler.getGameHandler().getMatch().getModules().getModule(TeamRegisterModule.class).getPlayersTeams();
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED) || GameHandler.getGameHandler().getMatch().getState().equals(MatchState.CYCLING)) {
            throw new CommandException(ChatUtil.getWarningMessage(new LocalizedChatMessage(ChatConstant.ERROR_MATCH_OVER).getMessage(((Player) sender).getLocale())));
        }
        Optional<TeamModule> originalTeam = Teams.getTeamByPlayer((Player) sender);
        if (cmd.argsLength() == 0 && originalTeam.isPresent() && !originalTeam.get().isObserver()) {
            throw new CommandException(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_JOINED, Teams.getTeamByPlayer((Player) sender).get().getCompleteName() + ChatColor.RED).getMessage(((Player) sender).getLocale())));
        }
        if (playerTeams.containsKey(sender.getName())) {
            throw new CommandException(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_NO_PERMISSION).getMessage(ChatUtil.getLocale(sender))));
        }
        Optional<TeamModule> destinationTeam = Optional.absent();
        if (cmd.argsLength() > 0) {
            for (TeamModule teamModule : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
                if (teamModule.getName().toLowerCase().startsWith(cmd.getJoinedStrings(0).toLowerCase())) {
                    destinationTeam = Optional.of(teamModule);
                    break;
                }
            }
            if (!destinationTeam.isPresent()) {
                throw new CommandException(ChatConstant.ERROR_NO_TEAM_MATCH.getMessage(ChatUtil.getLocale(sender)));
            }
            if (destinationTeam.get().contains(sender)) {
                throw new CommandException(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_JOINED, destinationTeam.get().getCompleteName() + ChatColor.RED).getMessage(ChatUtil.getLocale(sender))));
            }
            if (playerTeams.containsKey(sender.getName())) {
                throw new CommandException(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_NO_PERMISSION).getMessage(ChatUtil.getLocale(sender))));
            }
            destinationTeam.get().add((Player) sender, false);
        } else {
            destinationTeam = Teams.getTeamWithFewestPlayers(GameHandler.getGameHandler().getMatch());

            if (destinationTeam.isPresent()) {
                destinationTeam.get().add((Player) sender, false);
            } else {
                throw new CommandException(ChatConstant.ERROR_TEAMS_FULL.getMessage(ChatUtil.getLocale(sender)));
            }
        }
    }

    @Command(aliases = {"leave"}, desc = "Leave the game.")
    public static void leave(final CommandContext cmd, CommandSender sender) {
        Bukkit.getServer().dispatchCommand(sender, "join observers");
    }
}
