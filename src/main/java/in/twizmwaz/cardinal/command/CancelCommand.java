package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.MatchState;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CancelCommand {

    @Command(aliases = {"cancel"}, desc = "Cancels the current countdown.")
    @CommandPermissions("cardinal.match.cancel")
    public static void cancel(final CommandContext cmd, CommandSender sender) {
        try {
            GameHandler.getGameHandler().getCycleTimer().setCancelled(true);
            GameHandler.getGameHandler().getMatch().getStartTimer().setCancelled(true);
            GameHandler.getGameHandler().getMatch().setState(MatchState.WAITING);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        sender.sendMessage(ChatColor.RED + "Canceled the match start!");
    }

}
