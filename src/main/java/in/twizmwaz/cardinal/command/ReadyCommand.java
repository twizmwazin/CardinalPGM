package in.twizmwaz.cardinal.command;

import com.google.common.base.Optional;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.modules.timers.StartTimer;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Config;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReadyCommand {

    @Command(aliases = {"ready"}, desc = "Make your team ready.")
    public static void ready(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        Match match = GameHandler.getGameHandler().getMatch();
        if (match.isRunning() || match.hasEnded()) {
            throw new CommandException(ChatConstant.ERROR_READY_BEFORE_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        Optional<TeamModule> team = Teams.getTeamOrPlayerByPlayer((Player) sender);
        if (!team.isPresent()) {
            throw new CommandException(ChatConstant.ERROR_TEAM_ABSENT.getMessage(ChatUtil.getLocale(sender)));
        }
        if (team.get().isReady() || (!Config.observersReady && team.get().isObserver())) {
            throw new CommandException(ChatConstant.ERROR_TEAM_ALREADY_READY.getMessage(ChatUtil.getLocale(sender)));
        }
        team.get().setReady(true);
        ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_NOW_READY, team.get().getCompleteName() + ChatColor.YELLOW)));
        if (Config.observersReady ? Teams.teamsReady() : Teams.teamsNoObsReady()) {
            match.getModules().getModule(StartTimer.class).startTimer(Config.startDefault, false);
        }
    }

    @Command(aliases = {"unready"}, desc = "Make your team not ready.")
    public static void unready(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        Match match = GameHandler.getGameHandler().getMatch();
        if (match.isRunning() || match.hasEnded()) {
            throw new CommandException(ChatConstant.ERROR_READY_BEFORE_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        Optional<TeamModule> team = Teams.getTeamOrPlayerByPlayer((Player) sender);
        if (!team.isPresent()) {
            throw new CommandException(ChatConstant.ERROR_TEAM_ABSENT.getMessage(ChatUtil.getLocale(sender)));
        }
        if (!Config.observersReady && team.get().isObserver()) {
            throw new CommandException(ChatConstant.ERROR_TEAM_CAN_NOT_UNREADY.getMessage(ChatUtil.getLocale(sender)));
        }
        if (!team.get().isReady()) {
            throw new CommandException(ChatConstant.ERROR_TEAM_ALREADY_NOT_READY.getMessage(ChatUtil.getLocale(sender)));
        }
        team.get().setReady(false);
        ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_NO_LONGER_READY, team.get().getCompleteName() + ChatColor.YELLOW)));
        if (match.isStarting()) {
            match.getModules().getModule(StartTimer.class).setCancelled(true);
            ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_UNREADY_CANCEL_COUNTDOWN, team.get().getCompleteName() + ChatColor.RED)));
        }
    }

}
