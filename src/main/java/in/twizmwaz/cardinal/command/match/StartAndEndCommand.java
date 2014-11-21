package in.twizmwaz.cardinal.command.match;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.MatchState;
import org.bukkit.command.CommandSender;

/**
 * Created by kevin on 11/19/14.
 */
public class StartAndEndCommand {
    private static int timer;
    private static boolean waiting = false;

    @Command(aliases = {"start"}, desc = "Starts the match.", usage = "[time]")
    public static void start(CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().getState() != MatchState.WAITING)
            throw new CommandException("Cannot start a match right now!");
        int time = 30;
        try {
            time = cmd.getInteger(0);
        } catch (IndexOutOfBoundsException ex) {

        }
        GameHandler.getGameHandler().getMatch().start(time);


    }

    @Command(aliases = {"end"}, desc = "Ends the match.", usage = "[time]")
    public static void end(CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().getState() == MatchState.PLAYING) {
            try {
                GameHandler.getGameHandler().getMatch().getTeamByName(cmd.getString(0));
            } catch (IndexOutOfBoundsException ex) {
                GameHandler.getGameHandler().getMatch().end(null);
            }


            //GameHandler.getGameHandler().getMatch().end(cmd.getString(0));
        } else throw new CommandException("Cannot end a game that is not currently playing!");
    }

}
