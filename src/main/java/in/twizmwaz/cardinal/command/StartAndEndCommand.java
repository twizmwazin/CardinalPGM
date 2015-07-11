package in.twizmwaz.cardinal.command;

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
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.command.CommandSender;

public class StartAndEndCommand {

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
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_RESUME).getMessage(ChatUtils.getLocale(sender)));
        } else {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_START).getMessage(ChatUtils.getLocale(sender)));
        }

    }

    @Command(aliases = {"end", "finish"}, desc = "Ends the match.", usage = "[team]")
    @CommandPermissions("cardinal.match.end")
    public static void end(CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().getState() == MatchState.PLAYING) {
            try {
                TeamModule team = TeamUtils.getTeamByName(cmd.getString(0));
                GameHandler.getGameHandler().getMatch().end(team);
            } catch (IndexOutOfBoundsException ex) {
                GameHandler.getGameHandler().getMatch().end(null);
            }

        } else
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_END).getMessage(ChatUtils.getLocale(sender)));
    }

}
