package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.PlayerVisibilityChangeEvent;
import in.twizmwaz.cardinal.settings.Setting;
import in.twizmwaz.cardinal.settings.SettingValue;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingCommands {

    @Command(aliases = {"settings"}, desc = "List all settings.", usage = "[page]")
    public static void settings(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (cmd.argsLength() == 0) {
            Bukkit.dispatchCommand(sender, "settings 1");
        } else {
            int page = cmd.getInteger(0);
            if (page > (Settings.getSettings().size() + 7) / 8) {
                throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_INVALID_PAGE_NUMBER, "" + (Settings.getSettings().size() + 7) / 8).getMessage(ChatUtil.getLocale(sender)));
            }
            sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "--------------" + ChatColor.YELLOW + " Settings (Page " + page + " of " + ((Settings.getSettings().size() + 7) / 8) + ") " + ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "--------------");
            for (int i = (page - 1) * 8; i < page * 8; i++) {
                if (i < Settings.getSettings().size()) {
                    sender.sendMessage(ChatColor.YELLOW + Settings.getSettings().get(i).getNames().get(0) + ": " + ChatColor.WHITE + Settings.getSettings().get(i).getDescription());
                }
            }
        }
    }

    @Command(aliases = {"set"}, desc = "Set a setting to a specific value.", usage = "<setting> <value>", min = 2)
    public static void set(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        Setting setting = Settings.getSettingByName(cmd.getString(0));
        if (setting == null) {
            throw new CommandException(ChatConstant.ERROR_NO_SETTING_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        SettingValue value = setting.getSettingValueByName(cmd.getString(1));
        if (value == null) {
            throw new CommandException(ChatConstant.ERROR_NO_VALUE_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        setting.setValueByPlayer((Player) sender, value);
        sender.sendMessage(ChatColor.YELLOW + setting.getNames().get(0) + ": " + ChatColor.WHITE + value.getValue());
        if (Settings.getSettingByName("Observers") != null && setting.equals(Settings.getSettingByName("Observers"))) {
            Bukkit.getServer().getPluginManager().callEvent(new PlayerVisibilityChangeEvent((Player) sender));
        }
    }

    @Command(aliases = {"toggle"}, desc = "Toggle a setting.", usage = "<setting>", min = 1)
    public static void toggle(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        Setting setting = Settings.getSettingByName(cmd.getString(0));
        if (setting == null) {
            throw new CommandException(ChatConstant.ERROR_NO_SETTING_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        int index = setting.getValues().indexOf(setting.getValueByPlayer((Player) sender));
        index ++;
        if (index >= setting.getValues().size()) {
            index = 0;
        }
        Bukkit.dispatchCommand(sender, "set " + cmd.getString(0) + " " + setting.getValues().get(index).getValue());
    }

}
