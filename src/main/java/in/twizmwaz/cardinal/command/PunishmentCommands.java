package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.module.modules.chatChannels.AdminChannel;
import in.twizmwaz.cardinal.module.modules.chatChannels.ChatChannelModule;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PunishmentCommands {

    @Command(aliases = {"kick", "k"}, desc = "Kick a player.", usage = "<player> <reason>", min = 1)
    @CommandPermissions("cardinal.punish.kick")
    public static void kick(CommandContext cmd, CommandSender sender) throws CommandException {
        Player kicked = Bukkit.getPlayer(cmd.getString(0));
        if (sender instanceof Player) {
            if (PermissionModule.isMod(((Player) sender).getUniqueId()) && kicked.isOp()) {
                throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_PLAYER_NOT_AFFECTED).getMessage(ChatUtils.getLocale(sender)));
            }
        }
        if (kicked != null) {
            String reason = cmd.argsLength() > 1 ? cmd.getJoinedStrings(1) : "You have been kicked!";
            Bukkit.broadcastMessage((sender instanceof Player ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console") + ChatColor.GOLD + " \u00BB Kicked \u00BB " + TeamUtils.getTeamByPlayer(kicked).getColor() + kicked.getDisplayName() + ChatColor.GOLD + " \u00BB " + reason);
            kicked.kickPlayer(ChatColor.RED + "Kicked" + ChatColor.GOLD + "  \u00BB  " + ChatColor.AQUA + reason);
        } else {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_PLAYER_MATCH).getMessage(ChatUtils.getLocale(sender)));
        }
    }

    @Command(aliases = {"warn", "w"}, usage = "<player> <reason>", desc = "Warn a player.", min = 1)
    @CommandPermissions("cardinal.punish.warn")
    public static void warn(CommandContext cmd, CommandSender sender) throws CommandException {
        Player warned = Bukkit.getPlayer(cmd.getString(0));
        if (warned != null) {
            String reason = cmd.argsLength() > 1 ? cmd.getJoinedStrings(1) : "You have been warned!";
            ChatChannelModule channel = GameHandler.getGameHandler().getMatch().getModules().getModule(AdminChannel.class);
            channel.sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + ((sender instanceof Player) ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console") + ChatColor.GOLD + " warned " + TeamUtils.getTeamColorByPlayer(warned) + warned.getDisplayName() + ChatColor.GOLD + " for " + reason);
            warned.sendMessage(ChatColor.RED + "" + ChatColor.MAGIC + "-------" + ChatColor.YELLOW + "WARNING" + ChatColor.RED + ChatColor.MAGIC + "-------");
            warned.sendMessage(ChatColor.GREEN + reason);
            warned.sendMessage(ChatColor.YELLOW + reason);
            warned.sendMessage(ChatColor.RED + reason);
            warned.sendMessage(ChatColor.RED + "" + ChatColor.MAGIC + "-------" + ChatColor.YELLOW + "WARNING" + ChatColor.RED + ChatColor.MAGIC + "-------");
        } else {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_PLAYER_MATCH).getMessage(ChatUtils.getLocale(sender)));
        }
    }

    @Command(aliases = {"ban", "pb"}, usage = "<player> <reason>", desc = "Ban a player.", min = 1)
    @CommandPermissions("cardinal.punish.ban")
    public static void ban(CommandContext cmd, CommandSender sender) throws CommandException {
        OfflinePlayer banned = Bukkit.getOfflinePlayer(cmd.getString(0));
        if (sender instanceof Player) {
            if (PermissionModule.isMod(((Player) sender).getUniqueId()) && banned.isOp()) {
                throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_PLAYER_NOT_AFFECTED).getMessage(ChatUtils.getLocale(sender)));
            }
        }
        String reason = cmd.argsLength() > 1 ? cmd.getJoinedStrings(1) : "You have been banned!";
        if (banned.isOnline()) {
            Player onlineBanned = (Player) banned;
            onlineBanned.kickPlayer(ChatColor.RED + "Permanently Banned" + ChatColor.GOLD + "  \u00BB  " + ChatColor.AQUA + reason);
            Bukkit.broadcastMessage((sender instanceof Player ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console") + ChatColor.GOLD + " \u00BB Permanent Ban \u00BB " + TeamUtils.getTeamColorByPlayer(banned) + onlineBanned.getDisplayName() + ChatColor.GOLD + " \u00BB " + reason);
            Bukkit.getBanList(BanList.Type.NAME).addBan(cmd.getString(0), ChatColor.RED + "Permanently Banned" + ChatColor.GOLD + "  \u00BB  " + reason, null, sender.getName());
        } else if (!Bukkit.getBanList(BanList.Type.NAME).isBanned(banned.getName())){
            Bukkit.broadcastMessage((sender instanceof Player ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console") + ChatColor.GOLD + " \u00BB Permanent Ban \u00BB " + TeamUtils.getTeamColorByPlayer(banned) + banned.getName() + ChatColor.GOLD + " \u00BB " + reason);
            Bukkit.getBanList(BanList.Type.NAME).addBan(cmd.getString(0), ChatColor.RED + "Permanently Banned" + ChatColor.GOLD + "  \u00BB  " + reason, null, sender.getName());
        } else throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_PLAYER_ALREADY_BANNED).getMessage(ChatUtils.getLocale(sender)));

    }

    @Command(aliases = {"mute"}, usage = "<player>", desc = "Prevents a player from talking.", min = 1)
    @CommandPermissions("cardinal.punish.mute")
    public static void mute(CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = Bukkit.getPlayer(cmd.getString(0));
        if (player != null) {
            if (!(PermissionModule.isMod(player.getUniqueId()) || player.isOp())) {
                if (!GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).isMuted(player)) {
                    sender.sendMessage(ChatColor.RED + "You muted " + TeamUtils.getTeamColorByPlayer(player) + player.getDisplayName());
                    player.sendMessage(ChatColor.RED + "You were muted by " + (sender instanceof Player ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console"));
                    GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).mute(player);
                } else throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_PLAYER_ALREADY_MUTED).getMessage(ChatUtils.getLocale(sender)));
            } else throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_PLAYER_NOT_AFFECTED).getMessage(ChatUtils.getLocale(sender)));
        } else throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_PLAYER_MATCH).getMessage(ChatUtils.getLocale(sender)));
    }

    @Command(aliases = {"unmute"}, usage = "<player>", desc = "Allows a player to talk after being muted.", min = 1)
    @CommandPermissions("cardinal.punish.mute")
    public static void unmute(CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = Bukkit.getPlayer(cmd.getString(0));
        if (player != null) {
            if (!(PermissionModule.isMod(player.getUniqueId()) || player.isOp())) {
                if (GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).isMuted(player)) {
                    sender.sendMessage(ChatColor.GREEN + "You unmuted " + TeamUtils.getTeamColorByPlayer(player) + player.getDisplayName());
                    player.sendMessage(ChatColor.GREEN + "You were unmuted by " + (sender instanceof Player ? TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console"));
                    GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).unmute(player);
                } else throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_PLAYER_NOT_MUTED).getMessage(ChatUtils.getLocale(sender)));
            } else throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_PLAYER_NOT_AFFECTED).getMessage(ChatUtils.getLocale(sender)));
        } else throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_PLAYER_NOT_FOUND).getMessage(ChatUtils.getLocale(sender)));
    }

    @Command(aliases = {"gmute"}, desc = "Enable/disable global mute.")
    @CommandPermissions("cardinal.punish.mute")
    public static void gmute(CommandContext cmd, CommandSender sender) {
        boolean state = GameHandler.getGameHandler().toggleGlobalMute();
        if (state) {
            ChatUtils.getGlobalChannel().sendLocalizedMessage(ChatConstant.UI_GLOBAL_MUTE_ENABLED.asMessage(ChatColor.AQUA));
        } else {
            ChatUtils.getGlobalChannel().sendLocalizedMessage(ChatConstant.UI_GLOBAL_MUTE_DISABLED.asMessage(ChatColor.AQUA));
        }
    }
}
