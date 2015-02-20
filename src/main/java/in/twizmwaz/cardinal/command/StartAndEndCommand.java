package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class StartAndEndCommand {
    private static int timer;
    private static boolean waiting = false;

    @Command(aliases = {"start"}, desc = "Starts the match.", usage = "[time]")
    @CommandPermissions("cardinal.match.start")
    public static void start(CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.WAITING)) {
            int time = 30;
            try {
                time = cmd.getInteger(0);
            } catch (IndexOutOfBoundsException ex) {
            }
            GameHandler.getGameHandler().getMatch().start(time);
        } else if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING)) {
            try {
                GameHandler.getGameHandler().getMatch().getStartTimer().setTime(cmd.getInteger(0));
            } catch (NullPointerException e) {
            }
        } else if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED)) {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_RESUME).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()));
        } else {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_START).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()));
        }

    }

    @Command(aliases = {"end", "finish"}, desc = "Ends the match.", usage = "[time]")
    @CommandPermissions("cardinal.match.end")
    public static void end(CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().getState() == MatchState.PLAYING) {
            try {
                TeamModule team = TeamUtils.getTeamByName(cmd.getString(0));
                GameHandler.getGameHandler().getMatch().end(team);
            } catch (IndexOutOfBoundsException ex) {
                GameHandler.getGameHandler().getMatch().end(null);
            }

        } else throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_END).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()));
    }

}
