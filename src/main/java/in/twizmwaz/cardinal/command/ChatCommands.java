package in.twizmwaz.cardinal.command;

import com.google.common.base.Optional;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.modules.chatChannels.ChatChannel;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Players;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCommands {

    @Command(aliases = {"g", "global", "shout"}, desc = "Talk in global chat.", usage = "<message>", anyFlags = true)
    @CommandPermissions("cardinal.chat.global")
    public static void global(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        if (cmd.argsLength() == 0) {
            if (Settings.getSettingByName("ChatChannel").getValueByPlayer((Player) sender).getValue().equals("global")) {
                throw new CommandException(ChatConstant.ERROR_GLOBAL_ALREADY_DEAFULT.getMessage(ChatUtil.getLocale(sender)));
            }
            Settings.getSettingByName("ChatChannel").setValueByPlayer((Player) sender, Settings.getSettingByName("ChatChannel").getSettingValueByName("global"));
            sender.sendMessage(ChatColor.YELLOW + ChatConstant.UI_DEFAULT_CHANNEL_GLOBAL.getMessage(ChatUtil.getLocale(sender)));
        } else {
            if (GameHandler.getGameHandler().getGlobalMute() && !sender.hasPermission("cardinal.globalmute.override")) {
                throw new CommandException(ChatConstant.ERROR_GLOBAL_MUTE_ENABLED.asMessage().getMessage(ChatUtil.getLocale(sender)));
            }
            ChatUtil.getGlobalChannel().sendMessage("<" + Players.getName(sender) + ChatColor.RESET + ">: " + cmd.getJoinedStrings(0));
        }
    }

    @Command(aliases = {"a", "admin"}, desc = "Talk in admin chat.", usage = "<message>", anyFlags = true)
    @CommandPermissions("cardinal.chat.admin")
    public static void admin(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        if (cmd.argsLength() == 0) {
            if (Settings.getSettingByName("ChatChannel").getValueByPlayer((Player) sender).getValue().equals("admin")) {
                throw new CommandException(ChatConstant.ERROR_ADMIN_ALREADY_DEAFULT.getMessage(ChatUtil.getLocale(sender)));
            }
            Settings.getSettingByName("ChatChannel").setValueByPlayer((Player) sender, Settings.getSettingByName("ChatChannel").getSettingValueByName("admin"));
            sender.sendMessage(ChatColor.YELLOW + ChatConstant.UI_DEFAULT_CHANNEL_ADMIN.getMessage(ChatUtil.getLocale(sender)));
        } else {
            String message = cmd.getJoinedStrings(0);
            ChatUtil.getAdminChannel().sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + Players.getName(sender) + ChatColor.RESET + ": " + message);
            Bukkit.getConsoleSender().sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + Players.getName(sender) + ChatColor.RESET + ": " + message);
        }
    }

    @Command(aliases = {"t"}, desc = "Talk in team chat.", usage = "<message>", anyFlags = true)
    @CommandPermissions("cardinal.chat.team")
    public static void team(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        if (cmd.argsLength() == 0) {
            if (Settings.getSettingByName("ChatChannel").getValueByPlayer((Player) sender).getValue().equals("team")) {
                throw new CommandException(ChatConstant.ERROR_TEAM_ALREADY_DEAFULT.getMessage(ChatUtil.getLocale(sender)));
            }
            Settings.getSettingByName("ChatChannel").setValueByPlayer((Player) sender, Settings.getSettingByName("ChatChannel").getSettingValueByName("team"));
            sender.sendMessage(ChatColor.YELLOW + ChatConstant.UI_DEFAULT_CHANNEL_TEAM.getMessage(ChatUtil.getLocale(sender)));
        } else {
            if (GameHandler.getGameHandler().getGlobalMute() && !sender.hasPermission("cardinal.globalmute.override")) {
                throw new CommandException(ChatConstant.ERROR_GLOBAL_MUTE_ENABLED.getMessage(ChatUtil.getLocale(sender)));
            }
            Optional<TeamModule> team = Teams.getTeamByPlayer((Player) sender);
            String message = cmd.getJoinedStrings(0);
            if (team.isPresent()) {
                ChatChannel channel = Teams.getTeamChannel(team);
                channel.sendLocalizedMessage(new UnlocalizedChatMessage(Players.getName(sender) + ChatColor.RESET + ": " + message));
                Bukkit.getConsoleSender().sendMessage(team.get().getColor() + "[" + team.get().getName() + "] " + Players.getName(sender) + ChatColor.RESET + ": " + message);
            } else {
                Bukkit.dispatchCommand(sender, "g " + message);
            }
        }
    }

}
