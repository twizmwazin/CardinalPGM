package in.twizmwaz.cardinal.command;

import in.twizmwaz.cardinal.event.PlayerNameUpdateEvent;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.NestedCommand;

public class RankCommands {

    public static class RankParentCommand {

        @Command(aliases = { "rank", "ranks" }, desc = "Commands for managing ranks.", min = 1, max = -1)
        @NestedCommand({ RankCommands.class })
        public static void rank(final CommandContext args, CommandSender sender) throws CommandException {
            
        }

    }

    @Command(aliases = { "give", "add" }, desc = "Gives the specified player a rank.", min = 2, max = 2)
    @CommandPermissions("cardinal.ranks.give")
    public static void give(final CommandContext args, final CommandSender sender) throws CommandException {
        if (!sender.hasPermission("cardinal.ranks.give." + args.getString(1))) {
            throw new CommandPermissionsException();
        }

        Player target = Bukkit.getPlayer(args.getString(0));
        if (!PermissionModule.rankExists(args.getString(1))) {
            sender.sendMessage(ChatColor.RED + "The specified rank does not exist!");
            return;
        }

        if (PermissionModule.hasRank(target.getUniqueId(), args.getString(1))) {
            sender.sendMessage(ChatColor.RED + "The specified player is already a member of that rank!");
            return;
        }
        
        if (target.isOp()) {
            if (PermissionModule.isStaffRank(args.getString(1))) {
                sender.sendMessage(ChatColor.RED + "The specified player is opped!");
                return;
            }
        }
        
        PermissionModule.giveRank(target.getUniqueId(), args.getString(1));
        Bukkit.getServer().getPluginManager().callEvent(new PlayerNameUpdateEvent(target));
    }

    @Command(aliases = { "take", "remove" }, desc = "Gives the specified player a rank.", min = 2, max = 2)
    @CommandPermissions("cardinal.ranks.take")
    public static void take(final CommandContext args, final CommandSender sender) throws CommandException {
        Player target = Bukkit.getPlayer(args.getString(0));
        if (!sender.hasPermission("cardinal.ranks.take." + args.getString(1))) {
            throw new CommandPermissionsException();
        }

        if (!PermissionModule.rankExists(args.getString(1))) {
            sender.sendMessage(ChatColor.RED + "The specified rank does not exist!");
            return;
        }

        if (!PermissionModule.hasRank(target.getUniqueId(), args.getString(1))) {
            sender.sendMessage(ChatColor.RED + "The specified player is not a member of that rank!");
            return;
        }
        
        PermissionModule.takeRank(target.getUniqueId(), args.getString(1));
        Bukkit.getServer().getPluginManager().callEvent(new PlayerNameUpdateEvent(target));
    }

    @Command(aliases = { "list", "all" }, desc = "Lists all the ranks.", min = 0, max = 0)
    @CommandPermissions("cardinal.ranks.list")
    public static void list(final CommandContext args, final CommandSender sender) throws CommandException {
        if (PermissionModule.listRanks().size() == 0) {
            sender.sendMessage(ChatColor.RED + "There are no ranks.");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "------" + ChatColor.AQUA + "[Ranks]" + ChatColor.YELLOW + "------");
        for (String rank : PermissionModule.listRanks()) {
            sender.sendMessage(ChatColor.AQUA + rank);
        }
    }

}
