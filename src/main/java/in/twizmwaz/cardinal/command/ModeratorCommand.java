package in.twizmwaz.cardinal.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;

public class ModeratorCommand {
    @Command(aliases = {"mod"}, desc = "Give a player the rank of moderator", usage = "<player>", min = 1, max = 1)
    @CommandPermissions("cardinal.ranks.give.Moderator")
    public static void mod(CommandContext cmd, CommandSender sender) throws CommandException {
        Bukkit.dispatchCommand(sender, "ranks give " + cmd.getString(0) + " Moderator");
    }

    @Command(aliases = {"demod", "unmod"}, desc = "Remove a player from the rank of moderator", usage = "<player>", min = 1, max = 1)
    @CommandPermissions("cardinal.ranks.take.Moderator")
    public static void demod(CommandContext cmd, CommandSender sender) throws CommandException {
        Bukkit.dispatchCommand(sender, "ranks take " + cmd.getString(0) + " Moderator");
    }
}
