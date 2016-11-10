package in.twizmwaz.cardinal.command;

import com.google.common.base.Optional;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.module.modules.timers.StartTimer;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Config;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.command.CommandSender;

public class StartAndEndCommand {

    @Command(aliases = {"start", "begin"}, desc = "Starts the match.", usage = "[time]", flags = "f")
    @CommandPermissions("cardinal.match.start")
    public static void start(CommandContext cmd, CommandSender sender) throws CommandException {
        StartTimer start = GameHandler.getGameHandler().getMatch().getModules().getModule(StartTimer.class);
        if (!start.startTimer(cmd.getInteger(0, Config.startDefault), cmd.hasFlag('f'))) {
            throw new CommandException(new LocalizedChatMessage(start.getMatch().hasEnded() ?
                    ChatConstant.ERROR_NO_RESUME : ChatConstant.ERROR_NO_START).getMessage(ChatUtil.getLocale(sender)));
        }

    }

    @Command(aliases = {"end", "finish"}, desc = "Ends the match.", usage = "[team]", flags = "n")
    @CommandPermissions("cardinal.match.end")
    public static void end(CommandContext cmd, CommandSender sender) throws CommandException {
        if (!GameHandler.getGameHandler().getMatch().isRunning()) {
            throw new CommandException(ChatConstant.ERROR_NO_END.getMessage(ChatUtil.getLocale(sender)));
        }
        if (cmd.argsLength() > 0) {
            Optional<TeamModule> team = Teams.getTeamByName(cmd.getString(0));
            GameHandler.getGameHandler().getMatch().end(team.orNull());
        } else {
            if (cmd.hasFlag('n')) {
                GameHandler.getGameHandler().getMatch().end();
            } else {
                GameHandler.getGameHandler().getMatch().end(TimeLimit.getMatchWinner());
            }
        }
    }

}
