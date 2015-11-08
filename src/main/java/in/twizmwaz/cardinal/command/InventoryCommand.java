package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModule;
import in.twizmwaz.cardinal.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InventoryCommand {

    @Command(aliases = {"inventory", "inv"}, desc = "Opens a player's inventory", min = 1, usage = "<player>")
    public static void inventory(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        Player target = Bukkit.getPlayer(cmd.getString(0), sender);
        if (target == null) {
            throw new CommandException(ChatConstant.ERROR_PLAYER_NOT_FOUND.getMessage(ChatUtil.getLocale(sender)));
        }
        GameHandler.getGameHandler().getMatch().getModules().getModule(ObserverModule.class).openInventory((Player)sender, target, true);
    }
}
