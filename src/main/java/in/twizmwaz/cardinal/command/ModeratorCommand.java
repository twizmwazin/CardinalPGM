package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModeratorCommand {
    @Command(aliases = {"mod"}, desc = "Give a player the rank of moderator", usage = "<player>", min = 1, max = 1)
    @CommandPermissions({"cardinal.admin.mod"})
    public static void mod(CommandContext cmd, CommandSender sender)
            throws CommandException {
        Player moderator = Bukkit.getPlayer(cmd.getString(0));
        if (moderator != null) {
            if (!moderator.isOp()) {
                if (!PermissionModule.isMod(moderator.getUniqueId())) {
                    List<String> players = new ArrayList();
                    players.addAll(GameHandler.getGameHandler().getPlugin().getConfig().getStringList("permissions.Moderator.players"));
                    players.add(moderator.getUniqueId().toString());
                    GameHandler.getGameHandler().getPlugin().getConfig().set("permissions.Moderator.players", players);
                    GameHandler.getGameHandler().getPlugin().saveConfig();
                    sender.sendMessage(ChatColor.GOLD + "You gave moderator permissions to " + TeamUtils.getTeamByPlayer(moderator).getColor() + moderator.getDisplayName());
                    moderator.sendMessage(ChatColor.GOLD + "You are now a moderator!");
                } else {
                    throw new CommandException("Player is already moderator");
                }
            } else {
                throw new CommandException("Player is already op");
            }
        }
    }

    @Command(aliases = {"demod", "unmod"}, desc = "Remove a player from the rank of moderator", usage = "<player>", min = 1, max = 1)
    @CommandPermissions({"cardinal.admin.demod"})
    public static void demod(CommandContext cmd, CommandSender sender) throws CommandException {
        Player moderator = Bukkit.getPlayer(cmd.getString(0));
        if (moderator != null) {
            if (!moderator.isOp()) {
                if (PermissionModule.isMod(moderator.getUniqueId())) {
                    GameHandler.getGameHandler().getPlugin().getConfig().set("permissions.Moderator.players", Boolean.valueOf(GameHandler.getGameHandler().getPlugin().getConfig().getStringList("permissions.Moderator.players").remove(moderator.getUniqueId().toString())));
                    GameHandler.getGameHandler().getPlugin().saveConfig();
                    sender.sendMessage(ChatColor.GOLD + "You gave removed moderator permissions from " + TeamUtils.getTeamByPlayer(moderator).getColor() + moderator.getDisplayName());
                    moderator.sendMessage(ChatColor.GOLD + "You are no longer a moderator!");
                } else {
                    throw new CommandException("Player is not already moderator");
                }
            } else {
                throw new CommandException("Player is op");
            }
        }
    }
}
