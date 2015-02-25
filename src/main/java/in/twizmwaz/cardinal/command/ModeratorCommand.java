package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.PlayerNameUpdateEvent;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModeratorCommand {
    @Command(aliases = {"mod"}, desc = "Give a player the rank of moderator", usage = "<player>", min = 1, max = 1)
    @CommandPermissions("cardinal.admin.mod")
    public static void mod(CommandContext cmd, CommandSender sender) throws CommandException {
        OfflinePlayer moderator = Bukkit.getOfflinePlayer(cmd.getString(0));
        if (moderator != null) {
            if (!moderator.isOp()) {
                if (!PermissionModule.isMod(moderator.getUniqueId())) {
                    List<String> players = GameHandler.getGameHandler().getPlugin().getConfig().getStringList("permissions.moderator.players");
                    players.add(moderator.getUniqueId().toString());
                    GameHandler.getGameHandler().getPlugin().getConfig().set("permissions.moderator.players", players);
                    sender.sendMessage(ChatColor.GREEN + "You gave moderator permissions to " + TeamUtils.getTeamColorByPlayer(moderator) + (moderator.isOnline() ? ((Player) moderator).getDisplayName() : moderator.getName()));
                    if (moderator.isOnline()) {
                        ((Player) moderator).sendMessage(ChatColor.GREEN + "You are no longer a moderator!");
                        Bukkit.getServer().getPluginManager().callEvent(new PlayerNameUpdateEvent((Player) moderator));
                    }
                } else {
                    throw new CommandException("The player specified is already a moderator!");
                }
            } else {
                throw new CommandException("The player specified is already op!");
            }
        } else {
            throw new CommandException("Unknown player specified!");
        }
    }

    @Command(aliases = {"demod", "unmod"}, desc = "Remove a player from the rank of moderator", usage = "<player>", min = 1, max = 1)
    @CommandPermissions("cardinal.admin.demod")
    public static void demod(CommandContext cmd, CommandSender sender) throws CommandException {
        OfflinePlayer moderator = Bukkit.getOfflinePlayer(cmd.getString(0));
        if (moderator != null) {
            if (!moderator.isOp()) {
                if (PermissionModule.isMod(moderator.getUniqueId())) {
                    List<String> players = GameHandler.getGameHandler().getPlugin().getConfig().getStringList("permissions.moderator.players");
                    players.remove(moderator.getUniqueId().toString());
                    GameHandler.getGameHandler().getPlugin().getConfig().set("permissions.moderator.players", players);
                    sender.sendMessage(ChatColor.RED + "You removed moderator permissions from " + TeamUtils.getTeamColorByPlayer(moderator) + (moderator.isOnline() ? ((Player) moderator).getDisplayName() : moderator.getName()));
                    if (moderator.isOnline()) {
                        ((Player) moderator).sendMessage(ChatColor.RED + "You are no longer a moderator!");
                        Bukkit.getServer().getPluginManager().callEvent(new PlayerNameUpdateEvent((Player) moderator));
                    }
                } else {
                    throw new CommandException("The player specified is already not a moderator!");
                }
            } else {
                throw new CommandException("The player specified is already op!");
            }
        } else {
            throw new CommandException("Unknown player specified!");
        }
    }
}
