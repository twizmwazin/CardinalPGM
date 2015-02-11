package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import in.twizmwaz.cardinal.GameHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CancelCommand {

    @Command(aliases = {"cancel"}, desc = "Cancels the current countdown.")
    public static void cancel(final CommandContext cmd, CommandSender sender) {
        if(!(sender.hasPermission("cardinal.cancel")))
            sender.sendMessage(ChatColor.RED + "You don't have permission.");
        try {
            if (GameHandler.getGameHandler().getCycleTimer() != null) {
                GameHandler.getGameHandler().getCycleTimer().setCancelled(true);
                GameHandler.getGameHandler().getMatch().getStartTimer().setCancelled(true);
            }
        } catch (NullPointerException ex) {}
        Bukkit.broadcastMessage(ChatColor.GREEN + "All countdowns have been cancelled.");
    }
}
