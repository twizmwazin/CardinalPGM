package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import org.bukkit.command.CommandSender;

/**
 * Created by kevin on 10/31/14.
 */
public class CycleCommand {

    @Command(aliases = {"cycle"}, desc = "Cycles the world and loads a new world.", usage = "[time]")
    public static void cycle(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!sender.isOp()) {
            throw new CommandException("You must be op to cycle!");
        }
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            throw new CommandException("Cannot cycle while the match is running!");
        }
        GameHandler.getGameHandler().cycleAndMakeMatch();

    }

}
