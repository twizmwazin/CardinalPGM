package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Players;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class PrivateMessageCommands {

    @Command(aliases = {"msg", "message", "pm", "privatemessage", "whisper", "tell"}, desc = "Send a private message to a player.", usage = "<player> <message>", min = 2)
    public static void pm(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_PLAYER_COMMAND.getMessage(ChatUtil.getLocale(sender)));
        }
        Player target = Bukkit.getPlayer(cmd.getString(0), sender);
        if (target == null) {
            throw new CommandException(ChatConstant.ERROR_PLAYER_NOT_FOUND.getMessage(ChatUtil.getLocale(sender)));
        }
        if (Settings.getSettingByName("PrivateMessages") == null || Settings.getSettingByName("PrivateMessages").getValueByPlayer(target).getValue().equalsIgnoreCase("all")) {
            target.sendMessage(ChatColor.GRAY + "From " + Players.getName(sender) + ChatColor.GRAY + ": " + ChatColor.RESET + cmd.getJoinedStrings(1));
            sender.sendMessage(ChatColor.GRAY + "To " + Players.getName(target) + ChatColor.GRAY + ": " + ChatColor.RESET + cmd.getJoinedStrings(1));
            if (Settings.getSettingByName("PrivateMessageSounds") == null || Settings.getSettingByName("PrivateMessageSounds").getValueByPlayer(target).getValue().equalsIgnoreCase("on")) {
                target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2F);
            }
            target.setMetadata("reply", new FixedMetadataValue(Cardinal.getInstance(), sender));
        } else {
            sender.sendMessage(new LocalizedChatMessage(ChatConstant.ERROR_PLAYER_DISABLED_PMS, Players.getName(target) + ChatColor.RED).getMessage(ChatUtil.getLocale(sender)));
        }
    }

    @Command(aliases = {"reply", "r"}, desc = "Reply to a private message", min = 1)
    public static void reply(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_PLAYER_COMMAND.getMessage(ChatUtil.getLocale(sender)));
        }
        Player player = (Player) sender;
        if (!player.hasMetadata("reply")) {
            throw new CommandException(ChatConstant.ERROR_NO_MESSAGES.getMessage(ChatUtil.getLocale(sender)));
        }
        Player target = (Player) player.getMetadata("reply").get(0).value();
        if (target == null) {
            throw new CommandException(ChatConstant.ERROR_PLAYER_NOT_FOUND.getMessage(ChatUtil.getLocale(sender)));
        }
        if (Settings.getSettingByName("PrivateMessages") == null || Settings.getSettingByName("PrivateMessages").getValueByPlayer(target).getValue().equalsIgnoreCase("all")) {
            target.sendMessage(ChatColor.GRAY + "From " + Players.getName(sender) + ChatColor.GRAY + ": " + ChatColor.RESET + cmd.getJoinedStrings(0));
            sender.sendMessage(ChatColor.GRAY + "To " + Players.getName(target) + ChatColor.GRAY + ": " + ChatColor.RESET + cmd.getJoinedStrings(0));
            if (Settings.getSettingByName("PrivateMessageSounds") == null || Settings.getSettingByName("PrivateMessageSounds").getValueByPlayer(target).getValue().equalsIgnoreCase("on")) {
                target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2F);
            }
            target.setMetadata("reply", new FixedMetadataValue(Cardinal.getInstance(), sender));
        } else {
            sender.sendMessage(new LocalizedChatMessage(ChatConstant.ERROR_PLAYER_DISABLED_PMS, Players.getName(target) + ChatColor.RED).getMessage(ChatUtil.getLocale(sender)));
        }
    }
}
