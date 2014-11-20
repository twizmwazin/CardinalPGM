package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.data.Contributor;
import in.twizmwaz.cardinal.data.MapInfo;
import org.bukkit.command.CommandSender;

/**
 * Created by kevin on 11/16/14.
 */
public class MapCommand {

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

}
