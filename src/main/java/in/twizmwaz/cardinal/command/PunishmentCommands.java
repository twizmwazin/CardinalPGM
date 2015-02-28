package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.chatChannels.AdminChannel;
import in.twizmwaz.cardinal.module.modules.chatChannels.ChatChannelModule;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PunishmentCommands {

    @Command(aliases = {"kick", "k"}, desc = "Kick a player", usage = "<player> <reason>", min = 2)
    @CommandPermissions("cardinal.punish.kick")
    public static void kick(CommandContext cmd, CommandSender sender) throws CommandException {
        Player kicked = Bukkit.getPlayer(cmd.getString(0));
        if (kicked != null) {
            String reason = "";
            for (int i = 1; i < cmd.argsLength(); i++) {
                reason = reason + cmd.getString(i) + " ";
            }
            reason = reason.trim();
            Bukkit.broadcastMessage((sender instanceof Player ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console") + ChatColor.GOLD + " Kicked " + TeamUtils.getTeamByPlayer(kicked).getColor() + kicked.getDisplayName() + ChatColor.GOLD + " for " + ChatColor.DARK_AQUA + reason);
            kicked.kickPlayer(ChatColor.DARK_PURPLE + "You have been " + ChatColor.YELLOW + "kicked" + ChatColor.DARK_PURPLE + "for \n" + ChatColor.YELLOW + ChatColor.BOLD + reason + "\n " + ChatColor.DARK_PURPLE + "by " + ((sender instanceof Player) ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console"));
        } else {
            throw new CommandException("Player must be online!");
        }
    }

    @Command(aliases = {"warn", "w"}, usage = "<player> <reason>", desc = "Warn a player", min = 2)
    @CommandPermissions("cardinal.punish.warn")
    public static void warn(CommandContext cmd, CommandSender sender) throws CommandException {
        Player warned = Bukkit.getPlayer(cmd.getString(0));
        if (warned != null) {
            String reason = "";
            for (int i = 1; i < cmd.argsLength(); i++) {
                reason = reason + cmd.getString(i) + " ";
            }
            reason = reason.trim();
            ChatChannelModule channel = GameHandler.getGameHandler().getMatch().getModules().getModule(AdminChannel.class);
            channel.sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + ((sender instanceof Player) ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console") + ChatColor.GOLD + " warned " + TeamUtils.getTeamColorByPlayer(warned) + warned.getDisplayName() + ChatColor.GOLD + " for " + ChatColor.DARK_AQUA + reason);
            warned.sendMessage(ChatColor.RED + "" + ChatColor.MAGIC + "-------" + ChatColor.YELLOW + "WARNING" + ChatColor.RED + ChatColor.MAGIC + "-------");
            warned.sendMessage(ChatColor.GREEN + reason);
            warned.sendMessage(ChatColor.YELLOW + reason);
            warned.sendMessage(ChatColor.RED + reason);
            warned.sendMessage(ChatColor.RED + "" + ChatColor.MAGIC + "-------" + ChatColor.YELLOW + "WARNING" + ChatColor.RED + ChatColor.MAGIC + "-------");
        } else {
            throw new CommandException("Player must be online!");
        }
    }

    @Command(aliases = {"ban", "pb"}, usage = "<player> [reason]", desc = "Ban a player from the server", min = 2)
    @CommandPermissions("cardinal.punish.ban")
    public static void ban(CommandContext cmd, CommandSender sender) throws CommandException {
        OfflinePlayer banned = Bukkit.getOfflinePlayer(cmd.getString(0));
        String reason = "";
        for (int i = 1; i < cmd.argsLength(); i++) {
            reason = reason + cmd.getString(i) + " ";
        }
        reason = reason.trim();
        if (banned.isOnline()) {
            Player onlineBanned = (Player) banned;
            onlineBanned.kickPlayer(ChatColor.DARK_PURPLE + "You have been " + ChatColor.RED + "banned" + ChatColor.DARK_PURPLE + "for \n" + ChatColor.YELLOW + ChatColor.BOLD + reason + "\n " + ChatColor.DARK_PURPLE + "by " + ((sender instanceof Player) ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console"));
            Bukkit.broadcastMessage((sender instanceof Player ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console") + ChatColor.GOLD + " banned " + TeamUtils.getTeamColorByPlayer(banned) + onlineBanned.getDisplayName() + ChatColor.GOLD + " for " + ChatColor.DARK_AQUA + reason);
        } else {
            Bukkit.broadcastMessage((sender instanceof Player ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console") + ChatColor.GOLD + " banned " + TeamUtils.getTeamColorByPlayer(banned) + banned.getName() + ChatColor.GOLD + " for " + ChatColor.DARK_AQUA + reason);
        }
        banned.setBanned(true);
    }

    @Command(aliases = {"mute"}, usage = "<player>", desc = "Prevents a player from talking", min = 1, max = 1)
    @CommandPermissions("cardinal.punish.mute")
    public static void mute(CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = Bukkit.getPlayer(cmd.getString(0));
        if (player != null) {
            if (!(PermissionModule.isMod(player.getUniqueId()) || player.isOp())) {
                sender.sendMessage(ChatColor.RED + "You muted " + TeamUtils.getTeamColorByPlayer(player) + player.getDisplayName());
                player.sendMessage(ChatColor.RED + "You were muted by " + (sender instanceof Player ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console"));
                GameHandler.getGameHandler().getMatch().getModules().getModules(PermissionModule.class).get(0).disablePermission(Bukkit.getPlayer(cmd.getString(0)), "cardinal.chat.team");
                GameHandler.getGameHandler().getMatch().getModules().getModules(PermissionModule.class).get(0).disablePermission(Bukkit.getPlayer(cmd.getString(0)), "cardinal.chat.global");
            } else throw new CommandException("This player is not affected by the mute command");
        } else throw new CommandException("Player must be online");
    }

    @Command(aliases = {"unmute"}, usage = "<player>", desc = "Allow a player to talk after being muted", min = 1, max = 1)
    @CommandPermissions("cardinal.punish.mute")
    public static void unmute(CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = Bukkit.getPlayer(cmd.getString(0));
        if (player != null) {
            if (!(PermissionModule.isMod(player.getUniqueId()) || player.isOp())) {
                sender.sendMessage(ChatColor.GREEN + "You unmuted " + TeamUtils.getTeamColorByPlayer(player) + player.getDisplayName());
                player.sendMessage(ChatColor.GREEN + "You were unmuted by " + (sender instanceof Player ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console"));
                GameHandler.getGameHandler().getMatch().getModules().getModules(PermissionModule.class).get(0).enablePermission(Bukkit.getPlayer(cmd.getString(0)), "cardinal.chat.team");
                GameHandler.getGameHandler().getMatch().getModules().getModules(PermissionModule.class).get(0).enablePermission(Bukkit.getPlayer(cmd.getString(0)), "cardinal.chat.global");
            } else throw new CommandException("This player is not affected by the mute command");
        } else throw new CommandException("Player must be online");
    }
}
