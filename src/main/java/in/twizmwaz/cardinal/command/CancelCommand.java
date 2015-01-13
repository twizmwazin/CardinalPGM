package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.MatchState;
import org.bukkit.command.CommandSender;

public class CancelCommand {

    @Command(aliases = {"cancel"}, desc = "Cancels the current countdown.")
    @CommandPermissions("cardinal.cancel")
    public static void cancel(final CommandContext cmd, CommandSender sender) {
        try {
            GameHandler.getGameHandler().getCycleTimer().setCancelled(true);
        } catch (NullPointerException ex) {
        }
        try {
            GameHandler.getGameHandler().getMatch().getStartTimer().setCancelled(true);
            GameHandler.getGameHandler().getMatch().setState(MatchState.WAITING);
        } catch (NullPointerException ex) {
        }
        sender.sendMessage("Canceled all countdowns");
    }
}
