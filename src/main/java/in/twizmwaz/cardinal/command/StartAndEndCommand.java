package in.twizmwaz.cardinal.command;

import com.google.common.base.Optional;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.startTimer.StartTimer;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.command.CommandSender;

public class StartAndEndCommand {
    private static int timer;
    private static boolean waiting = false;

    @Command(aliases = {"start", "begin"}, desc = "Starts the match.", usage = "[time]", flags = "f")
    @CommandPermissions("cardinal.match.start")
    public static void start(CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.WAITING)) {
            int time = 600;
            if (cmd.argsLength() > 0) time = cmd.getInteger(0) * 20;
            GameHandler.getGameHandler().getMatch().start(time, cmd.hasFlag('f'));
        } else if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING)) {
            GameHandler.getGameHandler().getMatch().getModules().getModule(StartTimer.class).setTime(cmd.argsLength() > 0 ? cmd.getInteger(0) * 20 : 30 * 20);
            GameHandler.getGameHandler().getMatch().getModules().getModule(StartTimer.class).setForced(cmd.hasFlag('f'));
        } else if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED)) {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_RESUME).getMessage(ChatUtil.getLocale(sender)));
        } else {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_START).getMessage(ChatUtil.getLocale(sender)));
        }

    }

    @Command(aliases = {"end", "finish"}, desc = "Ends the match.", usage = "[team]")
    @CommandPermissions("cardinal.match.end")
    public static void end(CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().getState() == MatchState.PLAYING) {
            if (cmd.argsLength() > 0) {
                Optional<TeamModule> team = Teams.getTeamByName(cmd.getString(0));
                GameHandler.getGameHandler().getMatch().end(team.orNull());
            } else {
                GameHandler.getGameHandler().getMatch().end(Optional.<TeamModule>absent().orNull());
            }
        } else
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_END).getMessage(ChatUtil.getLocale(sender)));
    }

}
