package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import in.twizmwaz.cardinal.GameHandler;
import org.bukkit.command.CommandSender;

/**
 * Created by kevin on 10/31/14.
 */
public class CycleCommand {

    @Command(aliases = {"cycle"}, desc = "Cycles the world and loads a new world.", usage = "[time]")
    public static void cycle(CommandContext cmd, CommandSender sender) {
        if (!sender.isOp()) {
            return;
        }
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            return;
        }
        GameHandler.getGameHandler().cycleAndMakeMatch();

    }

}
