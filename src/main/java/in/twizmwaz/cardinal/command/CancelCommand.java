package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        } catch (NullPointerException ex) {
        }
        Bukkit.broadcastMessage(ChatColor.GREEN + "All countdowns have been cancelled.");
    }
}
