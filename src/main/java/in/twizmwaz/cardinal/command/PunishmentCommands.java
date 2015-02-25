package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.chatChannels.AdminChannel;
import in.twizmwaz.cardinal.module.modules.chatChannels.ChatChannelModule;
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
            Bukkit.broadcastMessage((sender instanceof Player ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.GOLD + "*" + ChatColor.AQUA + "Console") + ChatColor.GOLD + " >> Kicked " + TeamUtils.getTeamByPlayer(kicked).getColor() + kicked.getDisplayName() + ChatColor.GOLD + " >> " + ChatColor.GOLD + reason);
            kicked.kickPlayer(ChatColor.RED + "Kicked " + ChatColor.GOLD + ">> " + ChatColorAQUA  + reason + "\n " + ChatColor.DARK_PURPLE + "by " + ((sender instanceof Player) ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.GOLD + "*" + ChatColor.AQUA + "Console"));
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
            channel.sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + ((sender instanceof Player) ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*" + ChatColor.AQUA + "Console") + ChatColor.GOLD + " >> Warned " + TeamUtils.getTeamColorByPlayer(warned) + warned.getDisplayName() + ChatColor.GOLD + " >> " + ChatColor.DARK_AQUA + reason);
            warned.sendMessage(ChatColor.DARK_RED + "- - - - - - -" + ChatColor.RED + " WARNING " + ChatColor.DARK_RED + "- - - - - - -");
            warned.sendMessage(ChatColor.WHITE + "" + ChatColor.MAGIC + "- - - " + ChatColor.GOLD + reason + ChatColor.WHITE + "" + ChatColor.MAGIC + "- - - ");
            warned.sendMessage(ChatColor.WHITE + "" + ChatColor.MAGIC + "- - - " + ChatColor.GREEN + reason + ChatColor.WHITE + "" + ChatColor.MAGIC + "- - - ");
            warned.sendMessage(ChatColor.WHITE + "" + ChatColor.MAGIC + "- - - " + ChatColor.YELLOW + reason + ChatColor.WHITE + "" + ChatColor.MAGIC + "- - - ");
            warned.sendMessage(ChatColor.DARK_RED + "- - - - - - -" + ChatColor.RED + " WARNING " + ChatColor.DARK_RED + "- - - - - - -");
        } else {
            throw new CommandException("Player must be online!");
        }
    }

    @Command(aliases = {"ban"}, usage = "<player> [reason]", desc = "Ban a player from the server", min = 2)
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
            onlineBanned.kickPlayer(ChatColor.RED + "Banned " + ChatColor.GOLD + ">> " + ChatColor.AQUA + reason + "\n " + ChatColor.GOLD + "by " + ((sender instanceof Player) ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.GOLD + "*" + ChatColor.AQUA + "Console"));
            Bukkit.broadcastMessage((sender instanceof Player ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.GOLD + "*" + ChatColor.AQUA + "Console") + ChatColor.GOLD + " >> banned " + TeamUtils.getTeamColorByPlayer(banned) + onlineBanned.getDisplayName() + ChatColor.GOLD + " >> " + ChatColor.GOLD + reason);
        } else {
            Bukkit.broadcastMessage((sender instanceof Player ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.GOLD + "*" + ChatColor.AQUA + "Console") + ChatColor.GOLD + " >> banned " + TeamUtils.getTeamColorByPlayer(banned) + banned.getName() + ChatColor.GOLD + " >> " + ChatColor.GOLD + reason);
        }
        banned.setBanned(true);
    }
}
