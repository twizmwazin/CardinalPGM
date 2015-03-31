package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.UpdateHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CardinalCommand {

    @Command(aliases = "cardinal", flags = "v", desc = "Various functions related to Cardinal.")
    public static void cardinal(final CommandContext cmd, CommandSender sender) {
        if (cmd.hasFlag('v')) {
            sender.sendMessage("This server is running CardinalPGM version " + Cardinal.getInstance().getDescription().getVersion());
            if (UpdateHandler.getUpdateHandler().checkUpdates()) sender.sendMessage(ChatColor.GOLD + "A newer version of Cardinal is available!");
            else sender.sendMessage(ChatColor.GOLD + "You are on the latest version of Cardinal!");

        }
    }
    
}
