package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.util.VersionUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public class CardinalCommand {

    @Command(aliases = {"version"}, desc = "Get CardinalPGM version", min=0, max=0)
    @CommandPermissions("cardinal.version")
    public static void version(final CommandContext args, CommandSender sender) throws CommandException {
        sender.sendMessage(ChatColor.AQUA + "CardinalPGM is currently running at " + VersionUtil.getJarVersion());
    }
}
