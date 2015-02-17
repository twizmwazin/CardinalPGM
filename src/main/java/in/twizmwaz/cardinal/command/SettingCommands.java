package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.settings.Settings;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingCommands {

    @Command(aliases = {"settings"}, desc = "List all settings.", usage = "[page]")
    public static void settings(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (cmd.argsLength() == 0) {
            int page = 1;
            if (page > (Settings.getSettings().size() + 7) / 8) throw new CommandException("Unknown page selected! " + ((Settings.getSettings().size() + 7) / 8) + " total pages.");
            sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "--------------" + ChatColor.YELLOW + " Settings (Page " + page + " of " + ((Settings.getSettings().size() + 7) / 8) + ") " + ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "--------------");
            for (int i = (page - 1) * 8; i < page * 8; i ++) {
                if (i < Settings.getSettings().size()) sender.sendMessage(ChatColor.YELLOW + Settings.getSettings().get(i).getNames().get(0) + ": " + ChatColor.WHITE + Settings.getSettings().get(i).getDescription());
            }
        } else {
            int page = cmd.getInteger(0);
            if (page > (Settings.getSettings().size() + 7) / 8) throw new CommandException("Unknown page selected! " + ((Settings.getSettings().size() + 7) / 8) + " total pages.");
            sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "--------------" + ChatColor.YELLOW + " Settings (Page " + page + " of " + ((Settings.getSettings().size() + 7) / 8) + ") " + ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "--------------");
            for (int i = (page - 1) * 8; i < page * 8; i ++) {
                if (i < Settings.getSettings().size()) sender.sendMessage(ChatColor.YELLOW + Settings.getSettings().get(i).getNames().get(0) + ": " + ChatColor.WHITE + Settings.getSettings().get(i).getDescription());
            }
        }
    }

    @Command(aliases = {"set"}, desc = "Set a setting to a specific value.", usage = "<setting> <value>", min = 2)
    public static void set(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (Settings.getSettingByName(cmd.getString(0)) != null) {
            if (Settings.getSettingByName(cmd.getString(0)).getSettingValueByName(cmd.getString(1)) != null) {
                Settings.getSettingByName(cmd.getString(0)).setValueByPlayer((Player) sender, Settings.getSettingByName(cmd.getString(0)).getSettingValueByName(cmd.getString(1)));
                sender.sendMessage(ChatColor.YELLOW + Settings.getSettingByName(cmd.getString(0)).getNames().get(0) + ": " + ChatColor.WHITE + Settings.getSettingByName(cmd.getString(0)).getSettingValueByName(cmd.getString(1)).getValue());
            } else throw new CommandException("No value by this name!");
        } else throw new CommandException("No setting by this name!");
    }

}
