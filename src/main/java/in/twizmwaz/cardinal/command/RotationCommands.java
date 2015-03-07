package in.twizmwaz.cardinal.command;

import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.util.Contributor;
import org.bukkit.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.rotation.LoadedMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class RotationCommands {

    @Command(aliases = {"rotation", "rot"}, desc = "Shows the current rotation.", usage = "[page]")
    public static void rotation(final CommandContext cmd, CommandSender sender) throws CommandException {
        int index = cmd.argsLength() == 0 ? 1 : cmd.getInteger(0);
        List<LoadedMap> rot = GameHandler.getGameHandler().getRotation().getRotation();
        int pages = (int) Math.ceil((rot.size() + 7) / 8);
        if (index > pages)
            throw new CommandException("Invalid page number specified! Maximum page number is " + pages + ".");
        sender.sendMessage(ChatColor.RED + "------------- " + ChatColor.WHITE + new LocalizedChatMessage(ChatConstant.UI_ROTATION_CURRENT).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + ChatColor.DARK_AQUA + " (" + ChatColor.AQUA + index + ChatColor.DARK_AQUA + " of " + ChatColor.AQUA + pages + ChatColor.DARK_AQUA + ") " + ChatColor.RED + "-------------");
        String[] maps = {"", "", "", "", "", "", "", ""};
        for (int i = 0; i <= maps.length - 1; i++) {
            int position = 8 * (index - 1) + i;
            try {
                LoadedMap mapInfo = rot.get(position);
                if (mapInfo.getAuthors().size() == 1) {
                    maps[i] = maps[i] + ChatColor.GOLD + mapInfo.getName() + ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_BY).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + " " + ChatColor.RED + mapInfo.getAuthors().get(0).getName();
                } else if (mapInfo.getAuthors().size() > 1) {
                    maps[i] = maps[i] + ChatColor.GOLD + mapInfo.getName() + ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_BY).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + " ";
                    for (Contributor author : mapInfo.getAuthors()) {
                        if (mapInfo.getAuthors().indexOf(author) < mapInfo.getAuthors().size() - 2) {
                            maps[i] = maps[i] + ChatColor.RED + author.getName() + ChatColor.DARK_PURPLE + ", ";
                        } else if (mapInfo.getAuthors().indexOf(author) == mapInfo.getAuthors().size() - 2) {
                            maps[i] = maps[i] + ChatColor.RED + author.getName() + ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_AND).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + " ";
                        } else if (mapInfo.getAuthors().indexOf(author) == mapInfo.getAuthors().size() - 1) {
                            maps[i] = maps[i] + ChatColor.RED + author.getName();
                        }
                    }
                }
                if (GameHandler.getGameHandler().getRotation().getNextIndex() == position) {
                    maps[i] = ChatColor.DARK_AQUA + "" + (position + 1) + ". " + maps[i];
                } else {
                    maps[i] = ChatColor.WHITE + "" + (position + 1) + ". " + maps[i];
                }

            } catch (IndexOutOfBoundsException e) {
            }
        }
        for (String map : maps) {
            if (!map.equalsIgnoreCase("")) {
                sender.sendMessage(map);
            }
        }
    }

    @Command(aliases = {"maps"}, desc = "Shows all currently loaded maps.", usage = "[page]")
    public static void maps(final CommandContext cmd, CommandSender sender) throws CommandException {
        int index;
        try {
            index = cmd.getInteger(0);
        } catch (IndexOutOfBoundsException ex) {
            index = 1;
        }
        List<LoadedMap> loadedList = GameHandler.getGameHandler().getRotation().getLoaded();
        List<String> mapNames = new ArrayList<>();
        for (LoadedMap map : loadedList) {
            mapNames.add(map.getName());
        }
        Collections.sort(mapNames);
        List<LoadedMap> ordered = new ArrayList<>();
        for (String map : mapNames) {
            for (LoadedMap loadedMap : loadedList) {
                if (loadedMap.getName().equals(map)) {
                    ordered.add(loadedMap);
                    break;
                }
            }
        }
        int pages = (int) Math.ceil((loadedList.size() + 7) / 8);
        if (index > pages)
            throw new CommandException("Invalid page number specified! Maximum page number is " + pages + ".");
        sender.sendMessage(ChatColor.RED + "--------------- " + ChatColor.WHITE + new LocalizedChatMessage(ChatConstant.UI_MAPLOADED).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + ChatColor.DARK_AQUA + " (" + ChatColor.AQUA + index + ChatColor.DARK_AQUA + " of " + ChatColor.AQUA + pages + ChatColor.DARK_AQUA + ") " + ChatColor.RED + "---------------");
        String[] maps = {"", "", "", "", "", "", "", ""};
        for (int i = 0; i <= maps.length - 1; i++) {
            int position = 8 * (index - 1) + i;
            if (position < ordered.size()) {
                LoadedMap mapInfo = ordered.get(position);
                if (mapInfo.getAuthors().size() == 1) {
                    maps[i] = maps[i] + ChatColor.GOLD + mapInfo.getName() + ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_BY).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + " " + ChatColor.RED + mapInfo.getAuthors().get(0).getName();
                } else if (mapInfo.getAuthors().size() > 1) {
                    maps[i] = maps[i] + ChatColor.GOLD + mapInfo.getName() + ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_BY).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + " ";
                    for (Contributor author : mapInfo.getAuthors()) {
                        if (mapInfo.getAuthors().indexOf(author) < mapInfo.getAuthors().size() - 2) {
                            maps[i] = maps[i] + ChatColor.RED + author.getName() + ChatColor.DARK_PURPLE + ", ";
                        } else if (mapInfo.getAuthors().indexOf(author) == mapInfo.getAuthors().size() - 2) {
                            maps[i] = maps[i] + ChatColor.RED + author.getName() + ChatColor.DARK_PURPLE + " " + new LocalizedChatMessage(ChatConstant.MISC_AND).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()) + " ";
                        } else if (mapInfo.getAuthors().indexOf(author) == mapInfo.getAuthors().size() - 1) {
                            maps[i] = maps[i] + ChatColor.RED + author.getName();
                        }
                    }
                }
                maps[i] = ChatColor.WHITE + "" + (position + 1) + ". " + maps[i];
            }
        }
        for (String map : maps) {
            if (!map.equalsIgnoreCase("")) {
                sender.sendMessage(map);
            }
        }
    }

}
