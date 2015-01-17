package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.mapInfo.Info;
import in.twizmwaz.cardinal.module.modules.mapInfo.contributor.Contributor;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.DomUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapCommands {

    private static Info mapInfo;

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
                    sender.sendMessage("* " + ChatColor.RED + contributor.getName() + ChatColor.RESET + " " + ChatColor.GREEN + "" + ChatColor.ITALIC + "(" + contributor.getContribution() + ")");
                } else {
                    sender.sendMessage("* " + ChatColor.RED + contributor.getName());
                }
            }
        } else {
            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Author: " + ChatColor.RESET + ChatColor.GOLD + mapInfo.getAuthors().get(0).getName());
        }
        if (mapInfo.getContributors().size() > 0) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Contributors:");
            for (Contributor contributor : mapInfo.getContributors()) {
                if (contributor.hasContribution()) {
                    sender.sendMessage("* " + ChatColor.RED + contributor.getName() + ChatColor.RESET + ChatColor.GREEN + "" + ChatColor.ITALIC + " (" + contributor.getContribution() + ")");
                } else {
                    sender.sendMessage("* " + ChatColor.RED + contributor.getName());
                }
            }
        }
        if (mapInfo.getRules().size() > 0) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Rules:");
            for (int i = 1; i <= mapInfo.getRules().size(); i++) {
                sender.sendMessage(ChatColor.WHITE + "" + i + ") " + ChatColor.GOLD + mapInfo.getRules().get(i - 1));
            }
        }
        int total = 0;
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.isObserver()) continue;
            total = total + team.getMax();
        }
        sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Max players: " + ChatColor.RESET + "" + ChatColor.GOLD + total);
    }

    @Command(aliases = {"next", "nextmap", "nm", "mn"}, desc = "Shows next map.", usage = "")
    public static void next(final CommandContext cmd, CommandSender sender) {
        String nextMap = GameHandler.getGameHandler().getRotation().getNext().getName();
        try {
            Document doc = DomUtil.parse(new File("maps/" + nextMap + "/map.xml"));
            List<Contributor> authors;
            String name = doc.getRootElement().getChild("name").getText();
            authors = new ArrayList<>();
            for (Element element : doc.getRootElement().getChildren("authors")) {
                for (Element author : element.getChildren()) {
                    if (author.hasAttributes()) {
                        authors.add(new Contributor(author.getText(), author.getAttribute("contribution").getValue()));
                    } else {
                        authors.add(new Contributor(author.getText()));
                    }
                }
            }
            if (authors.size() == 1) {
                sender.sendMessage(ChatColor.DARK_PURPLE + "Next map: " + ChatColor.GOLD + name + ChatColor.DARK_PURPLE + " by " + ChatColor.RED + authors.get(0).getName());
            } else if (authors.size() > 1) {
                int size = authors.size();
                String result = ChatColor.DARK_PURPLE + "Next map: " + ChatColor.GOLD + name + ChatColor.DARK_PURPLE + " by ";
                for (Contributor author : authors) {
                    if (authors.indexOf(author) < authors.size() - 2) {
                        result = result + ChatColor.RED + author.getName() + ChatColor.DARK_PURPLE + ", ";
                    } else if (authors.indexOf(author) == authors.size() - 2) {
                        result = result + ChatColor.RED + author.getName() + ChatColor.DARK_PURPLE + " and ";
                    } else if (authors.indexOf(author) == authors.size() - 1) {
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
