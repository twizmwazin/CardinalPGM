package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.rotation.LoadedMap;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.Contributor;
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
        sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_MAP_OBJECTIVE).getMessage(ChatUtils.getLocale(sender)) + ": " + ChatColor.RESET + "" + ChatColor.GOLD + mapInfo.getObjective());
        if (mapInfo.getAuthors().size() > 1) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_MAP_AUTHORS).getMessage(ChatUtils.getLocale(sender)) + ":");
            for (Contributor contributor : mapInfo.getAuthors()) {
                if (contributor.getContribution() != null) {
                    sender.sendMessage("* " + ChatColor.RED + contributor.getName() + ChatColor.RESET + " " + ChatColor.GREEN + "" + ChatColor.ITALIC + "(" + contributor.getContribution() + ")");
                } else {
                    sender.sendMessage("* " + ChatColor.RED + contributor.getName());
                }
            }
        } else {
            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_MAP_AUTHOR).getMessage(ChatUtils.getLocale(sender)) + ": " + ChatColor.RESET + ChatColor.GOLD + mapInfo.getAuthors().get(0).getName());
        }
        if (mapInfo.getContributors().size() > 0) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_MAP_CONTRIBUTORS).getMessage(ChatUtils.getLocale(sender)) + ":");
            for (Contributor contributor : mapInfo.getContributors()) {
                if (contributor.getContribution() != null) {
                    sender.sendMessage("* " + ChatColor.RED + contributor.getName() + ChatColor.RESET + ChatColor.GREEN + "" + ChatColor.ITALIC + " (" + contributor.getContribution() + ")");
                } else {
                    sender.sendMessage("* " + ChatColor.RED + contributor.getName());
                }
            }
        }
        if (mapInfo.getRules().size() > 0) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_MAP_RULES).getMessage(ChatUtils.getLocale(sender)) + ":");
            for (int i = 1; i <= mapInfo.getRules().size(); i++) {
                sender.sendMessage(ChatColor.WHITE + "" + i + ") " + ChatColor.GOLD + mapInfo.getRules().get(i - 1));
            }
        }
        sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + new LocalizedChatMessage(ChatConstant.UI_MAP_MAX).getMessage(ChatUtils.getLocale(sender)) + ": " + ChatColor.RESET + "" + ChatColor.GOLD + mapInfo.getMaxPlayers());
    }

    @Command(aliases = {"next", "nextmap", "nm", "mn"}, desc = "Shows next map.", usage = "")
    public static void next(final CommandContext cmd, CommandSender sender) {
        LoadedMap next = GameHandler.getGameHandler().getRotation().getNext();
        if (next.getAuthors().size() == 1) {
            sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.GENERIC_MAP_NEXT, ChatColor.GOLD + next.getName() + ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_BY).getMessage(ChatUtils.getLocale(sender)) + " " + ChatColor.RED + next.getAuthors().get(0).getName()).getMessage(ChatUtils.getLocale(sender)));
        } else if (next.getAuthors().size() > 1) {
            String result = ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.GENERIC_MAP_NEXT, ChatColor.GOLD + next.getName() + ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_BY).getMessage(ChatUtils.getLocale(sender)) + " ").getMessage(ChatUtils.getLocale(sender));
            for (Contributor author: next.getAuthors()) {
                if (next.getAuthors().indexOf(author) < next.getAuthors().size() - 2) {
                    result = result + ChatColor.RED + author.getName() + ChatColor.DARK_PURPLE + ", ";
                } else if (next.getAuthors().indexOf(author) == next.getAuthors().size() - 2) {
                    result = result + ChatColor.RED + author.getName() + ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_AND).getMessage(ChatUtils.getLocale(sender)) + " ";
                } else if (next.getAuthors().indexOf(author) == next.getAuthors().size() - 1) {
                    result = result + ChatColor.RED + author.getName();
                }
            }
            sender.sendMessage(result);
        }
    }
}
