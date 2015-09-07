package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.modules.chatChannels.AdminChannel;
import in.twizmwaz.cardinal.module.modules.chatChannels.ChatChannel;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Players;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PunishmentCommands {

    @Command(aliases = {"kick", "k"}, desc = "Kick a player.", usage = "<player> [reason]", min = 1)
    @CommandPermissions("cardinal.punish.kick")
    public static void kick(CommandContext cmd, CommandSender sender) throws CommandException {
        Player kicked = Bukkit.getPlayer(cmd.getString(0));
        if (kicked == null) {
            throw new CommandException(ChatConstant.ERROR_PLAYER_NOT_FOUND.getMessage(ChatUtil.getLocale(sender)));
        }
        if (!sender.isOp() && kicked.isOp()) {
            throw new CommandException(ChatConstant.ERROR_PLAYER_NOT_AFFECTED.getMessage(ChatUtil.getLocale(sender)));
        }
        String reason = cmd.argsLength() > 1 ? cmd.getJoinedStrings(1) : "You have been kicked!";
        Bukkit.broadcastMessage(Players.getName(sender) + ChatColor.GOLD + " \u00BB Kicked \u00BB " + Players.getName(kicked) + ChatColor.GOLD + " \u00BB " + reason);
        kicked.kickPlayer(ChatColor.RED + "Kicked" + ChatColor.GOLD + "  \u00BB  " + ChatColor.AQUA + reason);
    }

    @Command(aliases = {"warn", "w"}, usage = "<player> [reason]", desc = "Warn a player.", min = 1)
    @CommandPermissions("cardinal.punish.warn")
    public static void warn(CommandContext cmd, CommandSender sender) throws CommandException {
        Player warned = Bukkit.getPlayer(cmd.getString(0));
        if (warned == null) {
            throw new CommandException(ChatConstant.ERROR_NO_PLAYER_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        String reason = cmd.argsLength() > 1 ? cmd.getJoinedStrings(1) : "You have been warned!";
        ChatChannel channel = GameHandler.getGameHandler().getMatch().getModules().getModule(AdminChannel.class);
        channel.sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + ((sender instanceof Player) ? Teams.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() : ChatColor.YELLOW + "*Console") + ChatColor.GOLD + " warned " + Teams.getTeamColorByPlayer(warned) + warned.getDisplayName() + ChatColor.GOLD + " for " + reason);
        warned.sendMessage(ChatColor.RED + "" + ChatColor.MAGIC + "-------" + ChatColor.YELLOW + "WARNING" + ChatColor.RED + ChatColor.MAGIC + "-------");
        warned.sendMessage(ChatColor.GREEN + reason);
        warned.sendMessage(ChatColor.YELLOW + reason);
        warned.sendMessage(ChatColor.RED + reason);
        warned.sendMessage(ChatColor.RED + "" + ChatColor.MAGIC + "-------" + ChatColor.YELLOW + "WARNING" + ChatColor.RED + ChatColor.MAGIC + "-------");
    }

    @Command(aliases = {"ban", "pb"}, usage = "<player> [reason]", desc = "Ban a player.", min = 1)
    @CommandPermissions("cardinal.punish.ban")
    public static void ban(CommandContext cmd, CommandSender sender) throws CommandException {
        OfflinePlayer banned = Bukkit.getOfflinePlayer(cmd.getString(0));
        if (!sender.isOp() && banned.isOp()) {
            throw new CommandException(ChatConstant.ERROR_PLAYER_NOT_AFFECTED.getMessage(ChatUtil.getLocale(sender)));
        }
        if (banned.isBanned()) {
            throw new CommandException(ChatConstant.ERROR_PLAYER_ALREADY_BANNED.getMessage(ChatUtil.getLocale(sender)));
        }
        String reason = cmd.argsLength() > 1 ? cmd.getJoinedStrings(1) : "You have been banned!";
        if (banned.isOnline()) {
            banned.getPlayer().kickPlayer(ChatColor.RED + "Permanently Banned" + ChatColor.GOLD + "  \u00BB  " + ChatColor.AQUA + reason);
        }
        Bukkit.broadcastMessage(Players.getName(sender) + ChatColor.GOLD + " \u00BB Permanent Ban \u00BB " + Players.getName(banned) + ChatColor.GOLD + " \u00BB " + reason);
        Bukkit.getBanList(BanList.Type.NAME).addBan(cmd.getString(0), ChatColor.RED + "Permanently Banned" + ChatColor.GOLD + "  \u00BB  " + reason, null, sender.getName());
    }

    @Command(aliases = {"mute"}, usage = "<player>", desc = "Prevents a player from talking.", min = 1)
    @CommandPermissions("cardinal.punish.mute")
    public static void mute(CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = Bukkit.getPlayer(cmd.getString(0));
        if (player == null) {
            throw new CommandException(ChatConstant.ERROR_NO_PLAYER_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        if (!sender.isOp() && player.isOp()) {
            throw new CommandException(ChatConstant.ERROR_PLAYER_NOT_AFFECTED.getMessage(ChatUtil.getLocale(sender)));
        }
        if (GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).isMuted(player)) {
            throw new CommandException(ChatConstant.ERROR_PLAYER_ALREADY_MUTED.getMessage(ChatUtil.getLocale(sender)));
        }
        sender.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_MUTED, Players.getName(player))).getMessage(ChatUtil.getLocale(sender)));
        player.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_MUTED_BY, Players.getName(sender))).getMessage(ChatUtil.getLocale(player)));
        GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).mute(player);
    }

    @Command(aliases = {"unmute"}, usage = "<player>", desc = "Allows a player to talk after being muted.", min = 1)
    @CommandPermissions("cardinal.punish.mute")
    public static void unmute(CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = Bukkit.getPlayer(cmd.getString(0));
        if (player == null) {
            throw new CommandException(ChatConstant.ERROR_NO_PLAYER_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        if (!sender.isOp() && player.isOp()) {
            throw new CommandException(ChatConstant.ERROR_PLAYER_NOT_AFFECTED.getMessage(ChatUtil.getLocale(sender)));
        }
        if (!GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).isMuted(player)) {
            throw new CommandException(ChatConstant.ERROR_PLAYER_NOT_MUTED.getMessage(ChatUtil.getLocale(sender)));
        }
        sender.sendMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_UNMUTED, Players.getName(player))).getMessage(ChatUtil.getLocale(sender)));
        player.sendMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_UNMUTED_BY, Players.getName(sender))).getMessage(ChatUtil.getLocale(player)));
        GameHandler.getGameHandler().getMatch().getModules().getModule(PermissionModule.class).unmute(player);
    }

    @Command(aliases = {"gmute"}, desc = "Enable/disable global mute.")
    @CommandPermissions("cardinal.punish.mute")
    public static void gmute(CommandContext cmd, CommandSender sender) {
        boolean state = GameHandler.getGameHandler().toggleGlobalMute();
        if (state) {
            ChatUtil.getGlobalChannel().sendLocalizedMessage(ChatConstant.UI_GLOBAL_MUTE_ENABLED.asMessage(ChatColor.AQUA));
        } else {
            ChatUtil.getGlobalChannel().sendLocalizedMessage(ChatConstant.UI_GLOBAL_MUTE_DISABLED.asMessage(ChatColor.AQUA));
        }
    }
}
