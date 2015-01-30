package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.*;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.monumentModes.MonumentModes;
import in.twizmwaz.cardinal.util.MiscUtils;
import in.twizmwaz.cardinal.util.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ModesCommand {

    @Command(aliases = {"modes", "mode"}, desc = "Lists information about the map's monument modes.", usage = "[list, next, push] [page]", max = 2)
    public static void modes(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().getModules().getModule(MonumentModes.class) != null) {
            ModuleCollection<MonumentModes> modes = GameHandler.getGameHandler().getMatch().getModules().getModules(MonumentModes.class);
            HashMap<MonumentModes, Integer> modesWithTime = new HashMap<>();
            for (MonumentModes modeForTime : modes) {
                modesWithTime.put(modeForTime, modeForTime.getTimeAfter());
            }
            List<MonumentModes> sortedModes = MiscUtils.getSortedHashMapKeyset(modesWithTime);
            Collections.reverse(sortedModes);
            if (cmd.argsLength() == 0) {
                int page = 1;
                sender.sendMessage(ChatColor.RED + "-------------- " + ChatColor.RESET + "Monument Modes" + ChatColor.DARK_AQUA + " (" + ChatColor.AQUA + page + ChatColor.DARK_AQUA + " of " + ChatColor.AQUA + ((modes.size() + 7) / 8) + ChatColor.DARK_AQUA + ")" + ChatColor.RED + " --------------");
                int count = 1;
                for (MonumentModes mode : sortedModes) {
                    if ((count + 7) / 8 == page) {
                        sender.sendMessage(ChatColor.GOLD + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + count + ". " + ChatColor.LIGHT_PURPLE + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + mode.getType().name().replaceAll("_", " ") + " - " + ChatColor.AQUA + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + StringUtils.formatTime(mode.getTimeAfter()) + ChatColor.DARK_AQUA + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + (GameHandler.getGameHandler().getMatch().isRunning() ? " (" + StringUtils.formatTime((mode.getTimeAfter() - MatchTimer.getTimeInSeconds() < 0) ? 0 : mode.getTimeAfter() - MatchTimer.getTimeInSeconds()) + " left)" : ""));
                    }
                    count ++;
                }
            } else if (cmd.argsLength() == 1) {
                if (cmd.getString(0).equalsIgnoreCase("list")) {
                    int page = 1;
                    sender.sendMessage(ChatColor.RED + "-------------- " + ChatColor.RESET + "Monument Modes" + ChatColor.DARK_AQUA + " (" + ChatColor.AQUA + page + ChatColor.DARK_AQUA + " of " + ChatColor.AQUA + ((modes.size() + 7) / 8) + ChatColor.DARK_AQUA + ")" + ChatColor.RED + " --------------");
                    int count = 1;
                    for (MonumentModes mode : sortedModes) {
                        if ((count + 7) / 8 == page) {
                            sender.sendMessage(ChatColor.GOLD + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + count + ". " + ChatColor.LIGHT_PURPLE + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + mode.getType().name().replaceAll("_", " ") + " - " + ChatColor.AQUA + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + StringUtils.formatTime(mode.getTimeAfter()) + ChatColor.DARK_AQUA + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + (GameHandler.getGameHandler().getMatch().isRunning() ? " (" + StringUtils.formatTime((mode.getTimeAfter() - MatchTimer.getTimeInSeconds() < 0) ? 0 : mode.getTimeAfter() - MatchTimer.getTimeInSeconds()) + " left)" : ""));
                        }
                        count ++;
                    }
                } else if (cmd.getString(0).equalsIgnoreCase("next")) {
                    for (MonumentModes mode : sortedModes) {
                        if (!mode.hasRan()) {
                            sender.sendMessage(ChatColor.DARK_PURPLE + "Next mode: " + ChatColor.GOLD + mode.getType().name().replaceAll("_", " ") + ChatColor.AQUA + " (" + StringUtils.formatTime(mode.getTimeAfter() - MatchTimer.getTimeInSeconds()) + " left)");
                            return;
                        }
                    }
                    throw new CommandException("No results match!");
                } else if (cmd.getString(0).equalsIgnoreCase("push")) {
                    if (!sender.hasPermission("cardinal.modes.push")) throw new CommandPermissionsException();
                    throw new CommandUsageException("Too few arguments.", "/modes push <time period>");
                } else {
                    int page;
                    try {
                        page = Integer.parseInt(cmd.getString(0));
                    } catch (NumberFormatException e) { throw new CommandException("Number expected, string received instead."); }
                    if (page <= ((modes.size() + 7) / 8)) {
                        sender.sendMessage(ChatColor.RED + "-------------- " + ChatColor.RESET + "Monument Modes" + ChatColor.DARK_AQUA + " (" + ChatColor.AQUA + page + ChatColor.DARK_AQUA + " of " + ChatColor.AQUA + ((modes.size() + 7) / 8) + ChatColor.DARK_AQUA + ")" + ChatColor.RED + " --------------");
                        int count = 1;
                        for (MonumentModes mode : sortedModes) {
                            if ((count + 7) / 8 == page) {
                                sender.sendMessage(ChatColor.GOLD + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + count + ". " + ChatColor.LIGHT_PURPLE + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + mode.getType().name().replaceAll("_", " ") + " - " + ChatColor.AQUA + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + StringUtils.formatTime(mode.getTimeAfter()) + ChatColor.DARK_AQUA + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + (GameHandler.getGameHandler().getMatch().isRunning() ? " (" + StringUtils.formatTime((mode.getTimeAfter() - MatchTimer.getTimeInSeconds() < 0) ? 0 : mode.getTimeAfter() - MatchTimer.getTimeInSeconds()) + " left)" : ""));
                            }
                            count++;
                        }
                    } else throw new CommandException("Unknown page selected! " + ((modes.size() + 7) / 8) + " total pages.");
                }
            } else {
                if (cmd.getString(0).equalsIgnoreCase("list")) {
                    int page;
                    try {
                        page = Integer.parseInt(cmd.getString(1));
                    } catch (NumberFormatException e) { throw new CommandException("Number expected, string received instead."); }
                    if (page <= ((modes.size() + 7) / 8)) {
                        sender.sendMessage(ChatColor.RED + "-------------- " + ChatColor.RESET + "Monument Modes" + ChatColor.DARK_AQUA + " (" + ChatColor.AQUA + page + ChatColor.DARK_AQUA + " of " + ChatColor.AQUA + ((GameHandler.getGameHandler().getMatch().getModules().getModules(MonumentModes.class).size() + 7) / 8) + ChatColor.DARK_AQUA + ")" + ChatColor.RED + " --------------");
                        int count = 1;
                        for (MonumentModes mode : sortedModes) {
                            if ((count + 7) / 8 == page) {
                                sender.sendMessage(ChatColor.GOLD + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + count + ". " + ChatColor.LIGHT_PURPLE + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + mode.getType().name().replaceAll("_", " ") + " - " + ChatColor.AQUA + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + StringUtils.formatTime(mode.getTimeAfter()) + ChatColor.DARK_AQUA + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + (GameHandler.getGameHandler().getMatch().isRunning() ? " (" + StringUtils.formatTime((mode.getTimeAfter() - MatchTimer.getTimeInSeconds() < 0) ? 0 : mode.getTimeAfter() - MatchTimer.getTimeInSeconds()) + " left)" : ""));
                            }
                            count++;
                        }
                    } else throw new CommandException("Unknown page selected! " + ((modes.size() + 7) / 8) + " total pages.");
                } else if (cmd.getString(0).equalsIgnoreCase("push")) {
                    if (!sender.hasPermission("cardinal.modes.push")) throw new CommandPermissionsException();
                    int time = StringUtils.timeStringToSeconds(cmd.getString(1));
                    for (MonumentModes mode : GameHandler.getGameHandler().getMatch().getModules().getModules(MonumentModes.class)) {
                        mode.setTimeAfter(mode.getTimeAfter() + time);
                    }
                    sender.sendMessage(ChatColor.GOLD + "All modes have been pushed " + (time < 0 ? "backwards" : "forwards") + " by " + StringUtils.formatTime(time));
                }
            }
        } else throw new CommandException("No results match!");
    }

}
