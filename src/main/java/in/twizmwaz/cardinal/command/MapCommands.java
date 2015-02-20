package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.rotation.LoadedMap;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class MapCommands {

    @Command(aliases = {"map"}, desc = "Shows information about the currently playing map.", usage = "")
    public static void map(final CommandContext args, CommandSender sender) throws CommandException {
        LoadedMap mapInfo;
        if (args.argsLength() == 0) mapInfo = GameHandler.getGameHandler().getMatch().getLoadedMap();
        else {
            String search = "";
            for (int a = 0; a < args.argsLength(); a++) {
                search = search + args.getString(a) + " ";
            }
            mapInfo = GameHandler.getGameHandler().getRotation().getMap(search.trim());
            if (mapInfo == null) throw new CommandException("No maps matched query");
        }
        sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "----------" + ChatColor.DARK_AQUA + " " + mapInfo.getName() + " " + ChatColor.GRAY + mapInfo.getVersion() + ChatColor.RED + " " + ChatColor.STRIKETHROUGH + "----------");
        sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_MAP_OBJECTIVE).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + ": " + ChatColor.RESET + "" + ChatColor.GOLD + mapInfo.getObjective());
        if (mapInfo.getAuthors().size() > 1) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_MAP_AUTHORS).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + ":");
            for (Pair<String, String> contributor : mapInfo.getAuthors()) {
                if (contributor.getRight() != null) {
                    sender.sendMessage("* " + ChatColor.RED + contributor.getLeft() + ChatColor.RESET + " " + ChatColor.GREEN + "" + ChatColor.ITALIC + "(" + contributor.getRight() + ")");
                } else {
                    sender.sendMessage("* " + ChatColor.RED + contributor.getLeft());
                }
            }
        } else {
            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_MAP_AUTHOR).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + ": " + ChatColor.RESET + ChatColor.GOLD + mapInfo.getAuthors().get(0).getLeft());
        }
        if (mapInfo.getContributors().size() > 0) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_MAP_CONTRIBUTORS).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + ":");
            for (Pair<String, String> contributor : mapInfo.getContributors()) {
                if (contributor.getRight() != null) {
                    sender.sendMessage("* " + ChatColor.RED + contributor.getLeft() + ChatColor.RESET + ChatColor.GREEN + "" + ChatColor.ITALIC + " (" + contributor.getRight() + ")");
                } else {
                    sender.sendMessage("* " + ChatColor.RED + contributor.getLeft());
                }
            }
        }
        if (mapInfo.getRules().size() > 0) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_MAP_RULES).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + ":");
            for (int i = 1; i <= mapInfo.getRules().size(); i++) {
                sender.sendMessage(ChatColor.WHITE + "" + i + ") " + ChatColor.GOLD + mapInfo.getRules().get(i - 1));
            }
        }
        sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_MAP_MAX).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + ": " + ChatColor.RESET + "" + ChatColor.GOLD + mapInfo.getMaxPlayers());
    }

    @Command(aliases = {"next", "nextmap", "nm", "mn"}, desc = "Shows next map.", usage = "")
    public static void next(final CommandContext cmd, CommandSender sender) {
        LoadedMap next = GameHandler.getGameHandler().getRotation().getNext();
        if (next.getAuthors().size() == 1) {
            sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.GENERIC_MAP_NEXT, ChatColor.GOLD + next.getName() + ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_BY).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + " " + ChatColor.RED + next.getAuthors().get(0).getLeft()).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()));
        } else if (next.getAuthors().size() > 1) {
            String result = ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.GENERIC_MAP_NEXT, ChatColor.GOLD + next.getName() + ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_BY).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + " ").getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString());
            for (Pair<String, String> author: next.getAuthors()) {
                if (next.getAuthors().indexOf(author) < next.getAuthors().size() - 2) {
                    result = result + ChatColor.RED + author.getLeft() + ChatColor.DARK_PURPLE + ", ";
                } else if (next.getAuthors().indexOf(author) == next.getAuthors().size() - 2) {
                    result = result + ChatColor.RED + author.getLeft() + ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_AND).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + " ";
                } else if (next.getAuthors().indexOf(author) == next.getAuthors().size() - 1) {
                    result = result + ChatColor.RED + author.getLeft();
                }
            }
            sender.sendMessage(result);
        }
    }
}
