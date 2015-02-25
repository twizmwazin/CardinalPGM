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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PunishmentCommands {
    @Command(aliases = {"kick", "k"}, desc = "Kick a player", usage = "<player> <reason>", min = 2)
    @CommandPermissions("cardinal.punish.kick")
    public static void kick(CommandContext cmd, CommandSender sender)
            throws CommandException {
        Player kicked = Bukkit.getPlayer(cmd.getString(0));
        if (kicked.isOnline()) {
            String reason = "";
            for (int i = 1; i < cmd.argsLength(); i++) {
                reason = reason + cmd.getString(i) + " ";
            }
            reason = reason.trim();
            Bukkit.broadcastMessage(ChatColor.GOLD + "*Console" + ChatColor.GOLD + " Kicked " + TeamUtils.getTeamByPlayer(kicked).getColor() + kicked.getDisplayName() + ChatColor.GOLD + " for " + ChatColor.DARK_AQUA + reason);
            kicked.kickPlayer(ChatColor.DARK_PURPLE + "You have been " + ChatColor.YELLOW + "kicked" + ChatColor.DARK_PURPLE + "for \n" + ChatColor.YELLOW + ChatColor.BOLD + reason + "\n " + ChatColor.DARK_PURPLE + "by " + ((sender instanceof Player) ? TeamUtils.getTeamByPlayer((Player) sender).getColor() + ((Player) sender).getDisplayName() : new StringBuilder().append(ChatColor.GOLD).append("*Console").toString()));
        } else {
            throw new CommandException("Player must be online!");
        }
    }

    @Command(aliases = {"warn", "w"}, usage = "<player> <reason>", desc = "Warn a player", min = 2)
    @CommandPermissions("cardinal.punish.warn")
    public static void warn(CommandContext cmd, CommandSender sender)
            throws CommandException {
        Player warned = Bukkit.getPlayer(cmd.getString(0));
        if (warned.isOnline()) {
            String reason = "";
            for (int i = 1; i < cmd.argsLength(); i++) {
                reason = reason + cmd.getString(i) + " ";
            }
            reason = reason.trim();
            Bukkit.broadcastMessage(ChatColor.GOLD + "*Console" + ChatColor.GOLD + " Kicked " + TeamUtils.getTeamByPlayer(warned).getColor() + warned.getDisplayName() + ChatColor.GOLD + " for " + ChatColor.DARK_AQUA + reason);
            ChatChannelModule channel = (ChatChannelModule) GameHandler.getGameHandler().getMatch().getModules().getModule(AdminChannel.class);
            channel.sendMessage("[" + ChatColor.YELLOW + "A" + ChatColor.WHITE + "] " + ((sender instanceof Player) ? TeamUtils.getTeamByPlayer((Player) sender).getColor() + ((Player) sender).getDisplayName() : new StringBuilder().append(ChatColor.GOLD).append("*Console").append(ChatColor.GOLD).append(" Kicked ").append(TeamUtils.getTeamByPlayer(warned).getColor()).append(warned.getDisplayName()).append(ChatColor.GOLD).append(" for ").append(ChatColor.DARK_AQUA).append(reason).toString()));
            warned.sendMessage(ChatColor.RED + "" + ChatColor.MAGIC + "-------" + ChatColor.YELLOW + "WARNING" + ChatColor.RED + ChatColor.MAGIC + "-------");
            warned.sendMessage(ChatColor.GREEN + reason);
            warned.sendMessage(ChatColor.YELLOW + reason);
            warned.sendMessage(ChatColor.RED + reason);
            warned.sendMessage(ChatColor.RED + "" + ChatColor.MAGIC + "-------" + ChatColor.YELLOW + "WARNING" + ChatColor.RED + ChatColor.MAGIC + "-------");
        } else {
            throw new CommandException("Player must be online!");
        }
    }

    @Command(aliases = {"ban"}, usage = "<player> [reason]", desc = "Ban a player from the server", min = 2)
    @CommandPermissions("cardinal.punish.ban")
    public static void ban(CommandContext cmd, CommandSender sender)
            throws CommandException {
        Player banned = Bukkit.getPlayer(cmd.getString(0));
        String reason = "";
        for (int i = 1; i < cmd.argsLength(); i++) {
            reason = reason + cmd.getString(i) + " ";
        }
        reason = reason.trim();
        Bukkit.broadcastMessage(ChatColor.GOLD + "*Console" + ChatColor.GOLD + " Kicked " + TeamUtils.getTeamByPlayer(banned).getColor() + banned.getDisplayName() + ChatColor.GOLD + " for " + ChatColor.DARK_AQUA + reason);
        banned.kickPlayer(ChatColor.DARK_PURPLE + "You have been " + ChatColor.RED + "banned" + ChatColor.DARK_PURPLE + "for \n" + ChatColor.YELLOW + ChatColor.BOLD + reason + "\n " + ChatColor.DARK_PURPLE + "by " + ((sender instanceof Player) ? TeamUtils.getTeamByPlayer((Player) sender).getColor() + ((Player) sender).getDisplayName() : new StringBuilder().append(ChatColor.GOLD).append("*Console").toString()));
    }
}
