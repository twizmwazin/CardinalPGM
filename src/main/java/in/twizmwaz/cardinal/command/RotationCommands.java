package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.data.Contributor;
import in.twizmwaz.cardinal.data.MapInfo;
import in.twizmwaz.cardinal.util.DomUtil;
import org.bukkit.command.CommandSender;
import org.jdom2.Document;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by kevin on 11/27/14.
 */
public class RotationCommands {

    @Command(aliases = {"rotation", "rot"}, desc = "Shows the current rotation.", usage = "[page]")
    public static void rotation(final CommandContext cmd, CommandSender sender) throws CommandException {
        int index;
        try {
            index = cmd.getInteger(0);
        } catch (IndexOutOfBoundsException ex) {
            index = 1;
        }
        String[] rot = GameHandler.getGameHandler().getRotation().getRotation();
        int pages = (int) Math.ceil((rot.length + 8) / 8);
        if (index > pages)
            throw new CommandException("Invalid page number specified! Maximum page number is " + pages + ".");
        sender.sendMessage(ChatColor.RED + "------------- " + ChatColor.WHITE + "Current Rotation " + ChatColor.DARK_AQUA + "(" + ChatColor.AQUA + index + ChatColor.DARK_AQUA + " of " + ChatColor.AQUA + pages + ChatColor.DARK_AQUA + ") " + ChatColor.RED + "-------------");
        String[] maps = {"", "", "", "", "", "", "", ""};
        for (int i = 0; i <= maps.length - 1; i++) {
            int position = 8 * (index - 1) + i;
            try {
                Document doc = DomUtil.parse(new File("maps/" + rot[position] + "/map.xml"));
                MapInfo mapInfo = new MapInfo(doc);
                if (mapInfo.getAuthors().size() == 1) {
                    maps[i] = maps[i] + ChatColor.GOLD + mapInfo.getName() + ChatColor.DARK_PURPLE + " by " + ChatColor.RED + mapInfo.getAuthors().get(0).getName();
                } else if (mapInfo.getAuthors().size() > 1) {
                    maps[i] = maps[i] + ChatColor.GOLD + mapInfo.getName() + ChatColor.DARK_PURPLE + " by ";
                    for (Contributor author : mapInfo.getAuthors()) {
                        if (mapInfo.getAuthors().indexOf(author) < mapInfo.getAuthors().size() - 2) {
                            maps[i] = maps[i] + ChatColor.RED + author.getName() + ChatColor.DARK_PURPLE + ", ";
                        } else if (mapInfo.getAuthors().indexOf(author) == mapInfo.getAuthors().size() - 2) {
                            maps[i] = maps[i] + ChatColor.RED + author.getName() + ChatColor.DARK_PURPLE + " and ";
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

            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {

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
        List<String> loadedList = GameHandler.getGameHandler().getRotation().getLoaded();
        Collections.sort(loadedList);
        String[] loaded = loadedList.toArray(new String[loadedList.size()]);
        int pages = (int) Math.ceil((loaded.length + 8) / 8);
        if (index > pages)
            throw new CommandException("Invalid page number specified! Maximum page number is " + pages + ".");
        sender.sendMessage(ChatColor.RED + "--------------- " + ChatColor.WHITE + "Loaded Maps " + ChatColor.DARK_AQUA + "(" + ChatColor.AQUA + index + ChatColor.DARK_AQUA + " of " + ChatColor.AQUA + pages + ChatColor.DARK_AQUA + ") " + ChatColor.RED + "---------------");
        String[] maps = {"", "", "", "", "", "", "", ""};
        for (int i = 0; i <= maps.length - 1; i++) {
            int position = 8 * (index - 1) + i;
            try {
                Document doc = DomUtil.parse(new File("maps/" + loaded[position] + "/map.xml"));
                MapInfo mapInfo = new MapInfo(doc);
                if (mapInfo.getAuthors().size() == 1) {
                    maps[i] = maps[i] + ChatColor.GOLD + mapInfo.getName() + ChatColor.DARK_PURPLE + " by " + ChatColor.RED + mapInfo.getAuthors().get(0).getName();
                } else if (mapInfo.getAuthors().size() > 1) {
                    maps[i] = maps[i] + ChatColor.GOLD + mapInfo.getName() + ChatColor.DARK_PURPLE + " by ";
                    for (Contributor author : mapInfo.getAuthors()) {
                        if (mapInfo.getAuthors().indexOf(author) < mapInfo.getAuthors().size() - 2) {
                            maps[i] = maps[i] + ChatColor.RED + author.getName() + ChatColor.DARK_PURPLE + ", ";
                        } else if (mapInfo.getAuthors().indexOf(author) == mapInfo.getAuthors().size() - 2) {
                            maps[i] = maps[i] + ChatColor.RED + author.getName() + ChatColor.DARK_PURPLE + " and ";
                        } else if (mapInfo.getAuthors().indexOf(author) == mapInfo.getAuthors().size() - 1) {
                            maps[i] = maps[i] + ChatColor.RED + author.getName();
                        }

                    }

                }
                maps[i] = ChatColor.WHITE + "" + (position + 1) + ". " + maps[i];


            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {

            }

        }

        for (String map : maps) {
            if (!map.equalsIgnoreCase("")) {
                sender.sendMessage(map);
            }
        }
    }

}
