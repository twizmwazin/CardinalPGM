package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.*;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommands {

    @Command(aliases = {"tp", "teleport"}, desc = "Teleport players.", usage = "<player> [to, x] [y] [z]", min = 1, max = 4)
    public static void teleport(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            if (cmd.argsLength() == 1) {
                if (sender.hasPermission("cardinal.teleport") || (TeamUtils.getTeamByPlayer((Player) sender) != null && TeamUtils.getTeamByPlayer((Player) sender).isObserver())) {
                    try {
                        Player player = Bukkit.getPlayer(cmd.getString(0));
                        ((Player) sender).teleport(player);
                        sender.sendMessage(ChatColor.YELLOW + "Teleported.");
                    } catch (NullPointerException e) {
                        throw new CommandException("Player specified not online!");
                    }
                } else {
                    throw new CommandPermissionsException();
                }
            } else if (cmd.argsLength() == 2) {
                if (sender.hasPermission("cardinal.teleport")) {
                    try {
                        Player from = Bukkit.getPlayer(cmd.getString(0));
                        Player to = Bukkit.getPlayer(cmd.getString(1));
                        from.teleport(to);
                        sender.sendMessage(ChatColor.YELLOW + "Teleported.");
                    } catch (NullPointerException e) {
                        throw new CommandException("Player specified not online!");
                    }
                } else {
                    throw new CommandPermissionsException();
                }
            } else if (cmd.argsLength() == 4) {
                if (sender.hasPermission("cardinal.teleport")) {
                    try {
                        Player from = Bukkit.getPlayer(cmd.getString(0));
                        double x = cmd.getDouble(1);
                        double y = cmd.getDouble(2);
                        double z = cmd.getDouble(3);
                        from.teleport(new Location(from.getWorld(), x, y, z));
                        sender.sendMessage(ChatColor.YELLOW + "Teleported.");
                    } catch (NullPointerException e) {
                        throw new CommandException("Player specified not online!");
                    }
                } else {
                    throw new CommandPermissionsException();
                }
            } else {
                throw new CommandUsageException("Invalid arguments.", "/teleport <player> [to, x] [y] [z]");
            }
        } else {
            throw new CommandException("Console cannot use this command.");
        }
    }

    @Command(aliases = {"bring", "tphere", "grab"}, desc = "Teleport a player to you.", usage = "[map]", min = 1, max = 1)
    @CommandPermissions("cardinal.teleport")
    public static void teleportHere(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            try {
                Player teleporting = Bukkit.getPlayer(cmd.getString(0));
                teleporting.teleport((Player) sender);
                sender.sendMessage(ChatColor.YELLOW + "Teleported.");
            } catch (NullPointerException e) {
                throw new CommandException("Player specified not online!");
            }
        } else throw new CommandException("Console cannot use this command.");
    }

}
