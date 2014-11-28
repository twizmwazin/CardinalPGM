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

/**
 * Created by kevin on 11/16/14.
 */
public class MapCommands {

    private static MapInfo mapInfo;

    public static void refreshMapInfo() {
        mapInfo = GameHandler.getGameHandler().getMatch().getMapInfo();
    }

    @Command(aliases = {"map"}, desc = "Shows information about the currently playing map.", usage = "", min = 0, max = 0)
    public static void map(final CommandContext args, CommandSender sender) throws CommandException {
        refreshMapInfo();
        sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "----------" + ChatColor.DARK_AQUA + " " + mapInfo.getName() + " " + ChatColor.GRAY + mapInfo.getVersion() + ChatColor.RED + " " + ChatColor.STRIKETHROUGH + "----------");
        sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Objective: " + ChatColor.RESET + "" + ChatColor.GOLD + mapInfo.getObjective());
        if (mapInfo.getAuthors().size() > 1) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Authors:");
            for (Contributor contributor : mapInfo.getAuthors()) {
                if (contributor.hasContribution()) {
                    sender.sendMessage("* " + ChatColor.GOLD + contributor.getName() + ChatColor.RESET + " " + ChatColor.GREEN + "" + ChatColor.ITALIC + "(" + contributor.getContribution() + ")");
                } else {
                    sender.sendMessage("* " + ChatColor.GOLD + contributor.getName());
                }
            }
        } else {
            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Author: " + ChatColor.RESET + ChatColor.GOLD + mapInfo.getAuthors().get(0).getName());
        }
        if (mapInfo.getContributors().size() > 0) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Contributors:");
            for (Contributor contributor : mapInfo.getContributors()) {
                if (contributor.hasContribution()) {
                    sender.sendMessage("* " + ChatColor.GOLD + contributor.getName() + ChatColor.RESET + ChatColor.GREEN + "" + ChatColor.ITALIC + " (" + contributor.getContribution() + ")");
                } else {
                    sender.sendMessage("* " + ChatColor.GOLD + contributor.getName());
                }
            }
        }

    }

    @Command(aliases = {"next", "nextmap"}, desc = "Shows next map.", usage = "")
    public static void next(final CommandContext cmd, CommandSender sender) {
        String nextMap = GameHandler.getGameHandler().getRotation().getNext();
        try {
            Document doc = DomUtil.parse(new File("maps/" + nextMap + "/map.xml"));
            MapInfo mapInfo = new MapInfo(doc);
            if (mapInfo.getAuthors().size() == 1) {
                sender.sendMessage(ChatColor.DARK_PURPLE + "Next map: " + ChatColor.GOLD + mapInfo.getName() + ChatColor.DARK_PURPLE + " by " + ChatColor.RED + mapInfo.getAuthors().get(0).getName());
            } else if (mapInfo.getAuthors().size() > 1) {
                int size = mapInfo.getAuthors().size();
                String result = ChatColor.DARK_PURPLE + "Next map: " + ChatColor.GOLD + mapInfo.getName() + ChatColor.DARK_PURPLE + " by ";
                for (Contributor author : mapInfo.getAuthors()) {
                    if (mapInfo.getAuthors().indexOf(author) < mapInfo.getAuthors().size() - 2) {
                        result = result + ChatColor.RED + author.getName() + ChatColor.DARK_PURPLE + ", ";
                    } else if (mapInfo.getAuthors().indexOf(author) == mapInfo.getAuthors().size() - 2) {
                        result = result + ChatColor.RED + author.getName() + ChatColor.DARK_PURPLE + " and ";
                    } else if (mapInfo.getAuthors().indexOf(author) == mapInfo.getAuthors().size() - 1) {
                        result = result + ChatColor.RED + author.getName();
                    }

                }

                sender.sendMessage(result);
            }

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
